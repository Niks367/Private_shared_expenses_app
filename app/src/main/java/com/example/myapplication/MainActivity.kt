package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.GroupMember
import com.example.myapplication.entities.Profile
import com.example.myapplication.ui.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.GroupDetailsViewModel
import kotlinx.coroutines.launch
import java.security.MessageDigest

// --------------------------------------
// PASSWORD HASHING (SAFE - NO REFLECTION)
// --------------------------------------
object PasswordUtil {
    fun hash(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}

class MainActivity : ComponentActivity() {

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Expense Notifications"
            val descriptionText = "Notifications for new expenses"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("expenses", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)

        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val ctx = LocalContext.current
    val database = remember { AppDatabase.getInstance(ctx) }
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // ---------------------------
        // SIGNUP
        // ---------------------------
        composable("signup") {
            var signupError by remember { mutableStateOf<String?>(null) }

            SignupScreen(
                onSignupClick = { firstName, lastName, email, phone, password ->
                    scope.launch {
                        when {
                            firstName.isBlank() -> signupError = "Please enter a first name"
                            lastName.isBlank() -> signupError = "Please enter a last name"
                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> signupError = "Invalid email address"
                            password.length < 6 -> signupError = "Password must be at least 6 characters"
                            database.profileDao().findByEmail(email) != null -> signupError = "Email is already registered"

                            else -> {
                                signupError = null

                                val profile = Profile(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    phone = phone.takeIf { it.isNotBlank() },
                                    passwordHash = PasswordUtil.hash(password)
                                )

                                val userId = database.profileDao().insert(profile)

                                navController.navigate("main/$userId") {
                                    popUpTo("login") { inclusive = true }
                                }

                                Toast.makeText(ctx, "Welcome, $firstName!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                onNavigateToLogin = { navController.navigate("login") },
                errorMessage = signupError
            )
        }

        // ---------------------------
        // LOGIN
        // ---------------------------
        composable("login") {
            LoginScreen(
                onLoginSuccess = { email, password ->
                    scope.launch {
                        val profile = database.profileDao().findByEmail(email)
                        val hashedPassword = PasswordUtil.hash(password)

                        if (profile != null && hashedPassword == profile.passwordHash) {
                            WebSocketManager.start(ctx.applicationContext)

                            navController.navigate("main/${profile.id}") {
                                popUpTo("login") { inclusive = true }
                            }
                            Toast.makeText(ctx, "Login successful!", Toast.LENGTH_LONG).show()

                        } else {
                            Toast.makeText(ctx, "Incorrect password or user not found.", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                onNavigateToSignup = { navController.navigate("signup") }
            )
        }

        // ---------------------------
        // MAIN SCREEN WRAPPER
        // ---------------------------
        composable(
            "main/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            MainScreen(mainNavController = navController, userId = userId)
        }

        // ---------------------------
        // GROUP DETAILS
        // ---------------------------
        composable(
            route = "groupDetails/{groupId}/{userId}",
            arguments = listOf(
                navArgument("groupId") { type = NavType.LongType },
                navArgument("userId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getLong("groupId") ?: return@composable
            val factory = remember(groupId) { GroupDetailsViewModel.Factory(database, groupId) }
            val viewModel: GroupDetailsViewModel = viewModel(factory = factory)

            val onAddExpenseCallback = remember {
                { _: Long ->
                    navController.navigate("addExpense/$groupId/${backStackEntry.arguments?.getLong("userId")}")
                }
            }

            GroupDetailsScreen(
                groupId = groupId,
                onAddExpense = onAddExpenseCallback,
                viewModel = viewModel
            )
        }

        // ---------------------------
        // CREATE GROUP
        // ---------------------------
        composable(
            route = "createGroup/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->

            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L

            CreateGroupScreen(
                navController = navController
            ) { groupName, groupDescription ->

                scope.launch {
                    val newGroup = Group(name = groupName, description = groupDescription)
                    val newGroupId = database.groupDao().insert(newGroup)

                    val groupMember = GroupMember(groupId = newGroupId, profileId = userId, status = "joined")
                    database.groupMemberDao().insert(groupMember)

                    navController.navigate("inviteMembers/$newGroupId")
                }
            }
        }

        // ---------------------------
        // INVITE MEMBERS
        // ---------------------------
        composable(
            "inviteMembers/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.LongType })
        ) { backStackEntry ->

            val groupId = backStackEntry.arguments?.getLong("groupId") ?: 0L

            InviteMembersScreen(
                onInviteMember = { inviteeEmail ->
                    scope.launch {
                        val profile = database.profileDao().findByEmail(inviteeEmail)

                        if (profile != null) {
                            val groupMember = GroupMember(
                                groupId = groupId,
                                profileId = profile.id,
                                status = "invited"
                            )
                            database.groupMemberDao().insert(groupMember)

                            Toast.makeText(ctx, "${profile.firstName} has been invited.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(ctx, "User with that email not found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onDone = { navController.popBackStack() }
            )
        }

        // ---------------------------
        // ADD EXPENSE
        // ---------------------------
        composable(
            "addExpense/{groupId}/{userId}",
            arguments = listOf(
                navArgument("groupId") { type = NavType.LongType },
                navArgument("userId") { type = NavType.LongType }
            )
        ) { backStackEntry ->

            val groupId = backStackEntry.arguments?.getLong("groupId") ?: 0L
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L

            AddExpenseScreen(
                groupId = groupId,
                onAddExpense = { gId, description, amount, date ->

                    scope.launch {
                        val newExpense = Expense(
                            groupId = gId,
                            paidBy = userId,
                            description = description,
                            amount = amount,
                            date = date
                        )

                        database.expenseDao().insert(newExpense)
                        WebSocketManager.triggerExpenseAddedNotification()

                        Toast.makeText(ctx, "Expense added", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}


// =========================================
// BOTTOM NAVIGATION + MAIN SCREEN
// =========================================

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Expenses : BottomNavItem("expenses", Icons.Default.AddShoppingCart, "Expenses")
    object Groups : BottomNavItem("groups", Icons.Default.Group, "Groups")
    object Wallet : BottomNavItem("wallet", Icons.Default.AccountBalanceWallet, "Wallet")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}

@Composable
fun MainScreen(mainNavController: NavHostController, userId: Long) {
    val bottomBarNavController = rememberNavController()
    val database = AppDatabase.getInstance(LocalContext.current)

    Scaffold(
        bottomBar = { AppBottomNavigation(navController = bottomBarNavController) }
    ) { innerPadding ->

        NavHost(
            navController = bottomBarNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(BottomNavItem.Home.route) {
                val userProfile by produceState<Profile?>(initialValue = null, userId) {
                    value = database.profileDao().findById(userId)
                }
                val walletBalance by database.walletTransactionDao().getUserBalance(userId)
                    .collectAsState(initial = 0.0)

                userProfile?.let { profile ->
                    HomeScreen(
                        userName = profile.firstName,
                        userId = userId.toString(),
                        walletBalance = walletBalance ?: 0.0,
                    )
                } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            composable(BottomNavItem.Expenses.route) {
                val groups by database.groupDao().getAllGroupsForUser(userId).collectAsState(initial = emptyList())

                ExpensesScreen(
                    navController = mainNavController,
                    groups = groups,
                    onGroupClick = { group ->
                        mainNavController.navigate("groupDetails/${group.id}/$userId")
                    }
                )
            }

            composable(BottomNavItem.Groups.route) {
                val groups by database.groupDao().getAllGroupsForUser(userId).collectAsState(initial = emptyList())

                GroupScreen(
                    navController = mainNavController,
                    groups = groups,
                    onGroupClick = { group ->
                        mainNavController.navigate("groupDetails/${group.id}/$userId")
                    },
                    expenseDao = database.expenseDao(),
                    userId = userId
                )
            }

            composable(BottomNavItem.Wallet.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Wallet Screen for user $userId")
                }
            }

            composable(BottomNavItem.Profile.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Profile Screen for user $userId")
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Expenses,
        BottomNavItem.Groups,
        BottomNavItem.Wallet,
        BottomNavItem.Profile
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
