package com.example.myapplication.ui

import android.os.Build
import android.util.Base64
import android.util.Patterns
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresExtension
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.Profile
import com.example.myapplication.model.User
import kotlinx.coroutines.launch
import java.security.MessageDigest

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainScreen(userName: String, userId: String) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Statistics,
        BottomNavScreen.CardInfo,
        BottomNavScreen.Profile
    )

    Scaffold(
        topBar = { GreetingBar(userName = userName) },
        bottomBar = {
            NavigationBar {
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(id = screen.title)) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            // Avoid building up a huge back stack
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("signup") {
                var signupError by remember { mutableStateOf<String?>(null) }
                val ctx = LocalContext.current
                val database = remember { AppDatabase.getInstance(ctx) }
                val coroutineScope = rememberCoroutineScope()
                SignupScreen(
                    onSignupClick = { firstName, lastName, email, phone, password ->
                        coroutineScope.launch {
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

                                    navController.navigate("home") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                    android.widget.Toast.makeText(ctx, "Welcome, ${savedProfile.firstName}!", android.widget.Toast.LENGTH_LONG).show()
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
                val ctx = LocalContext.current
                val database = remember { AppDatabase.getInstance(ctx) }
                val coroutineScope = rememberCoroutineScope()
                LoginScreen(
                    onLoginSuccess = { email, password ->
                        coroutineScope.launch {
                            val profile = database.profileDao().findByEmail(email)
                            if (profile != null && hashPassword(password) == profile.passwordHash) {
                                navController.navigate("home") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                                android.widget.Toast.makeText(ctx, "Login successful!", android.widget.Toast.LENGTH_LONG).show()
                            } else {
                                android.widget.Toast.makeText(ctx, "Incorrect password or user not found.", android.widget.Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onNavigateToSignup = { navController.navigate("signup") },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(BottomNavScreen.Home.route) {
                Homepage(
                    userName = userName,
                    userId = userId,
                )
            }
            composable(BottomNavScreen.Statistics.route) {

            }
            composable(BottomNavScreen.CardInfo.route) {
                BillingAccountScreen()
            }
            composable(BottomNavScreen.Profile.route) {
                val ctx = LocalContext.current
                val userId = it.arguments?.getLong("userId") ?: return@composable
                var currentUser by remember(userId) { mutableStateOf<User?>(null) }
                val database = remember { AppDatabase.getInstance(ctx) }
                LaunchedEffect(userId) {
                    val profile = database.profileDao().findById(userId)
                    if (profile != null) {
                        currentUser = User(profile.id.toString(), "${profile.firstName} ${profile.lastName}", profile.email, profile.phone)
                    }
                }
                currentUser?.let {
                    ProfileScreen(
                        user = it,
                        onBackClick = { navController.popBackStack() },
                        onPersonalInfoClick = { navController.navigate("personalInfo/${userId}") },
                        onBillingAccountClick = { navController.navigate("billingAccount/${userId}") },
                        onLogoutClick = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
sealed class BottomNavScreen(val route: String, @StringRes val title: Int, @DrawableRes val icon: Int) {
    object Home : BottomNavScreen("home", R.string.home, R.drawable.home)
    object Statistics : BottomNavScreen("statistics", R.string.statistics, R.drawable.stats)
    object CardInfo : BottomNavScreen("card_info", R.string.card_info, R.drawable.wallet)
    object Profile : BottomNavScreen("profile", R.string.profile, R.drawable.profile)
}
private fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}