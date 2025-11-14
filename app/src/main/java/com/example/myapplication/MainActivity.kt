package com.example.myapplication

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.util.Base64
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.Profile
import com.example.myapplication.model.User
import com.example.myapplication.ui.BillingAccountScreen
import com.example.myapplication.ui.HomeScreen
import com.example.myapplication.ui.LoginScreen
import com.example.myapplication.ui.PersonalInformationScreen
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.ui.SignupScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.security.MessageDigest

class MainActivity : ComponentActivity() {
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val ctx = LocalContext.current
                val database = remember { AppDatabase.getInstance(ctx) }
                var signupError by remember { mutableStateOf<String?>(null) }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
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

                                                navController.navigate("home/${savedProfile.id}") {
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
                            LoginScreen(
                                onLoginSuccess = { email, password ->
                                    lifecycleScope.launch {
                                        val profile = database.profileDao().findByEmail(email)
                                        if (profile != null && hashPassword(password) == profile.passwordHash) {
                                            navController.navigate("home/${profile.id}") {
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
                        composable(
                            route = "home/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.LongType })
                        ) {
                            val userId = it.arguments?.getLong("userId")
                            if (userId == null) {
                                navController.navigate("login")
                                return@composable
                            }

                            var currentUser by remember(userId) { mutableStateOf<User?>(null) }

                            LaunchedEffect(userId) {
                                val profile = database.profileDao().findById(userId)
                                if (profile != null) {
                                    currentUser = User(
                                        userId = profile.id.toString(),
                                        username = "${profile.firstName} ${profile.lastName}",
                                        email = profile.email,
                                        phone = profile.phone
                                    )
                                } else {
                                    navController.navigate("login")
                                }
                            }

                            HomeScreen(
                                user = currentUser,
                                onProfileClick = { navController.navigate("profile/${userId}") }
                            )
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
}
