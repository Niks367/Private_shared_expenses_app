package com.example.myapplication

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
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.GroupMember
import com.example.myapplication.entities.Profile
import com.example.myapplication.model.User
import com.example.myapplication.ui.AddExpenseScreen
import com.example.myapplication.ui.BillingAccountScreen
import com.example.myapplication.ui.CreateGroupScreen
import com.example.myapplication.ui.ExpensesScreen
import com.example.myapplication.ui.GroupDetailsScreen
import com.example.myapplication.ui.GroupScreen
import com.example.myapplication.ui.HomeScreen
import com.example.myapplication.ui.InviteMembersScreen
import com.example.myapplication.ui.LoginScreen
import com.example.myapplication.ui.PersonalInformationScreen
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.ui.SignupScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.GroupDetailsViewModel
import kotlinx.coroutines.launch
import java.security.MessageDigest

class MainActivity : ComponentActivity() {
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val ctx = LocalContext.current
                val database = remember { AppDatabase.getInstance(ctx) }
                var signupError by remember { mutableStateOf<String?>(null) }

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("signup") {
                        SignupScreen(
                            onSignupClick = { firstName, lastName, email, phone, password ->
                                lifecycleScope.launch {
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
                                                passwordHash = hashPassword(password)
                                            )
                                            database.profileDao().insert(profile)
                                            val savedProfile = database.profileDao().findByEmail(email)!!

                                            navController.navigate("main/${savedProfile.id}") {
                                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                            }
                                            Toast.makeText(ctx, "Welcome, ${savedProfile.firstName}!", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            },
                            onNavigateToLogin = { navController.navigate("login") },
                            modifier = Modifier.fillMaxSize(),
                            errorMessage = signupError
                        )
                    }
                    
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { email, password ->
                                lifecycleScope.launch {
                                    val profile = database.profileDao().findByEmail(email)
                                    if (profile != null && hashPassword(password) == profile.passwordHash) {
                                        navController.navigate("main/${profile.id}") {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        }
                                        Toast.makeText(ctx, "Login successful!", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(ctx, "Incorrect password or user not found.", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            onNavigateToSignup = { navController.navigate("signup") },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    composable(
                        route = "main/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.LongType })
                    ) {
                        val userId = it.arguments?.getLong("userId")
                        if (userId == null) {
                            navController.navigate("login")
                            return@composable
                        }
                        MainScreen(mainNavController = navController, userId = userId)
                    }
                    
                    composable(
                        route = "profile/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.LongType })
                    ) {
                        val userId = it.arguments?.getLong("userId") ?: return@composable
                        var currentUser by remember(userId) { mutableStateOf<User?>(null) }
                        LaunchedEffect(userId) {
                            val profile = database.profileDao().findById(userId)
                            if (profile != null) {
                                currentUser = User(profile.id.toString(), "${profile.firstName} ${profile.lastName}", profile.email, profile.phone)
                            }
                        }

                        currentUser?.let { user ->
                            ProfileScreen(
                                user = user,
                                onBackClick = { navController.popBackStack() },
                                onPersonalInfoClick = { navController.navigate("personalInfo/${user.userId}") },
                                onBillingAccountClick = { navController.navigate("billingAccount/${user.userId}") },
                                onLogoutClick = {
                                    navController.navigate("login") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                    
                    composable(
                        route = "personalInfo/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.LongType })
                    ) {
                        val userId = it.arguments?.getLong("userId") ?: return@composable
                        var currentUser by remember(userId) { mutableStateOf<User?>(null) }
                        LaunchedEffect(userId) {
                            val profile = database.profileDao().findById(userId)
                            if (profile != null) {
                                currentUser = User(profile.id.toString(), "${profile.firstName} ${profile.lastName}", profile.email, profile.phone)
                            }
                        }
                        currentUser?.let { user ->
                            PersonalInformationScreen(
                                user = user,
                                onBackClick = { navController.popBackStack() },
                                onSaveClick = { name, email, phone ->
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                    
                    composable(
                        route = "billingAccount/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.LongType })
                    ) {
                        BillingAccountScreen(
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { _, _, _, _ ->
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Expenses : BottomNavItem("expenses", Icons.Default.AddShoppingCart, "Expenses")
    object Groups : BottomNavItem("groups", Icons.Default.Group, "Groups")
}

@Composable
fun MainScreen(mainNavController: NavHostController, userId: Long) {
    val navController = rememberNavController()
    val database = AppDatabase.getInstance(LocalContext.current)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        bottomBar = { AppBottomNavigation(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                var currentUser by remember(userId) { mutableStateOf<User?>(null) }
                var profile by remember(userId) { mutableStateOf<Profile?>(null) }

                LaunchedEffect(userId) {
                    val fetchedProfile = database.profileDao().findById(userId)
                    if (fetchedProfile != null) {
                        profile = fetchedProfile
                        currentUser = User(
                            userId = fetchedProfile.id.toString(),
                            username = "${fetchedProfile.firstName} ${fetchedProfile.lastName}",
                            email = fetchedProfile.email,
                            phone = fetchedProfile.phone
                        )
                    } else {
                        mainNavController.navigate("login")
                    }
                }

                val user = currentUser
                val userProfile = profile

                if (user != null && userProfile != null) {
                    val initials = (userProfile.firstName.firstOrNull()?.toString() ?: "") +
                            (userProfile.lastName.firstOrNull()?.toString() ?: "")

                    HomeScreen(
                        userName = userProfile.firstName,
                        userId = user.userId,
                        userInitials = initials,
                        onProfileClick = {
                            mainNavController.navigate("profile/${user.userId}")
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            
            composable(BottomNavItem.Expenses.route) {
                val groups by database.groupDao().getAllGroups().collectAsState(initial = emptyList())

                ExpensesScreen(navController = navController, groups = groups) { group ->
                    navController.navigate("addExpense/${group.id}")
                }
            }
            
            composable(BottomNavItem.Groups.route) {
                val groups by database.groupDao().getAllGroups().collectAsState(initial = emptyList())

                GroupScreen(navController = navController, groups = groups) { group ->
                    navController.navigate("groupDetails/${group.id}")
                }
            }

            composable("createGroup") {
                CreateGroupScreen(navController = navController) {
                        groupName, groupDescription ->
                    scope.launch {
                        val newGroup = Group(name = groupName, description = groupDescription)
                        val groupId = database.groupDao().insert(newGroup)
                        val groupMember = GroupMember(groupId = groupId, profileId = userId, status = "joined")
                        database.groupMemberDao().insert(groupMember)

                        navController.navigate("inviteMembers/$groupId") {
                            popUpTo("createGroup") { inclusive = true }
                        }
                    }
                }
            }
            
            composable(
                route = "inviteMembers/{groupId}",
                arguments = listOf(navArgument("groupId") { type = NavType.LongType })
            ) {
                val groupId = it.arguments?.getLong("groupId") ?: return@composable
                InviteMembersScreen(
                    onInviteMember = { invitee ->
                        scope.launch {
                            val profile = database.profileDao().findByEmailOrPhone(invitee)
                            if (profile != null) {
                                val groupMember = GroupMember(groupId = groupId, profileId = profile.id, status = "invited")
                                database.groupMemberDao().insert(groupMember)
                                Toast.makeText(context, "${profile.firstName} invited.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "User not found.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onDone = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "addExpense/{groupId}",
                arguments = listOf(navArgument("groupId") { type = NavType.LongType })
            ) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getLong("groupId") ?: return@composable

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
                            Toast.makeText(context, "Expense added", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "groupDetails/{groupId}",
                arguments = listOf(navArgument("groupId") { type = NavType.LongType })
            ) {
                val groupId = it.arguments?.getLong("groupId") ?: return@composable
                val viewModel: GroupDetailsViewModel = viewModel(
                    factory = GroupDetailsViewModel.Factory(database, groupId)
                )
                GroupDetailsScreen(
                    groupId = groupId, 
                    onAddExpense = {
                        navController.navigate("addExpense/$groupId")
                    }, 
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Expenses,
        BottomNavItem.Groups
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
