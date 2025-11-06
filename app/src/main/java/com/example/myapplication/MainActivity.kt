package com.example.myapplication

import SignupScreen
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.security.MessageDigest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SignupScreen(
                        onProfileCreated = { profile ->
                            // This runs on the UI thread; switch to a background thread for DB work
                            lifecycleScope.launch {
                                // Example: store in Room (uncomment when you have a DAO)
                                // db.profileDao().insert(profile)

                                // For now we just log / toast
                                val ctx = LocalContext.current
                                android.widget.Toast.makeText(
                                    ctx,
                                    "Welcome, ${profile.name}!",
                                    android.widget.Toast.LENGTH_LONG
                                ).show()

                                // TODO: navigate to your main/home screen here
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}