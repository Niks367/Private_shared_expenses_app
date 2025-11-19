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
import com.example.myapplication.ui.Homepage
import com.example.myapplication.ui.LoginScreen
import com.example.myapplication.ui.PersonalInformationScreen
import com.example.myapplication.ui.ProfileScreen
import com.example.myapplication.ui.SignupScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.Homepage
import kotlinx.coroutines.launch
import java.security.MessageDigest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment


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

                            // State for both the generic User and the specific Profile
                            var currentUser by remember(userId) { mutableStateOf<User?>(null) }
                            var profile by remember(userId) { mutableStateOf<Profile?>(null) }

                            LaunchedEffect(userId) {
                                val fetchedProfile = database.profileDao().findById(userId)
                                if (fetchedProfile != null) {
                                    // Update both state variables once the data is fetched
                                    profile = fetchedProfile
                                    currentUser = User(
                                        userId = fetchedProfile.id.toString(),
                                        username = "${fetchedProfile.firstName} ${fetchedProfile.lastName}",
                                        email = fetchedProfile.email,
                                        phone = fetchedProfile.phone
                                    )
                                } else {
                                    // If the user ID from the route is invalid, navigate back to login
                                    navController.navigate("login")
                                }
                            }

                            // Use local variables for smart-casting and safety
                            val user = currentUser
                            val userProfile = profile

                            // Only proceed if both user and profile data are loaded
                            if (user != null && userProfile != null) {
                                // Calculate initials from the profile data
                                val initials = (userProfile.firstName.firstOrNull()?.toString() ?: "") +
                                        (userProfile.lastName.firstOrNull()?.toString() ?: "")

                                // Call Homepage with all the required parameters
                                Homepage(
                                    userName = userProfile.firstName, // Pass just the first name for the greeting
                                    userId = user.userId,
                                    userInitials = initials,
                                    onProfileClick = {
                                        navController.navigate("profile/${user.userId}")
                                    }
                                )
                            } else {
                                // Display a loading indicator while fetching data
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
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
