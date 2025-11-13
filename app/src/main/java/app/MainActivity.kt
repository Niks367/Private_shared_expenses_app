package app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

// Defining User data class for the prototype
data class User(
    val userId: String,
    val username: String,
    val email: String,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Create a sample user for the prototype
                    val sampleUser = User(
                        userId = "user01",
                        username = "Jane Doe",
                        email = "jane.doe@email.com"
                    )
                    // Call new profile screen composable
                    ProfileScreen(
                        user = sampleUser,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

 // Create the "Edit Profile" screen Composable
@Composable
fun ProfileScreen(user: User, modifier: Modifier = Modifier) {
    // These 'remember' states will hold the text field values
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Edit Your Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        // Text field for the username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        // Text field for the email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = {
                // Logic for sending data to backend here
                println("Saving profile: Username=$username, Email=$email")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}

// Preview for the new ProfileScreen
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MyApplicationTheme {
        val fakeUser = User(userId = "123", username = "Frederik", email = "frederik@example.com")
        ProfileScreen(user = fakeUser, modifier = Modifier.padding(16.dp))
    }
}

