package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.LoginScreen
import com.example.myapplication.ui.SignupScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val ctx = LocalContext.current

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->  // ✅ Safe and correct lambda parameter

                    // ✅ Apply innerPadding to the NavHost content
                    NavHost(
                        navController = navController,
                        startDestination = "signup",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // ---------- SIGNUP SCREEN ----------
                        composable("signup") {
                            SignupScreen(
                                onProfileCreated = { profile ->
                                    lifecycleScope.launch {
                                        android.widget.Toast.makeText(
                                            ctx,
                                            "Welcome, ${profile.name}!",
                                            android.widget.Toast.LENGTH_LONG
                                        ).show()

                                        // Navigate to Login screen
                                        navController.navigate("login") {
                                            popUpTo("signup") { inclusive = true }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // ---------- LOGIN SCREEN ----------
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    lifecycleScope.launch {
                                        android.widget.Toast.makeText(
                                            ctx,
                                            "Login successful!",
                                            android.widget.Toast.LENGTH_LONG
                                        ).show()
                                        // TODO: Navigate to home/main screen
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
