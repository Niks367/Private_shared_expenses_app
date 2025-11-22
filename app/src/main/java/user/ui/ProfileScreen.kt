package user.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import user.model.User

// This is the main Composable for the profile screen
@Composable
fun ProfileScreen(user: User) {
    // These 'remember' states will hold the text field values
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }

    Column(
        modifier = Modifier
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
                // In a real app, this is where you would save the data.
                // For this prototype, we can just print it to the log.
                println("Saving profile: Username=$username, Email=$email")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}

// This preview allows you to see your UI without running the app
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // We create a fake user for the preview
    val fakeUser = User(userId = "123", username = "example", email = "example@example.com")
    MaterialTheme { // It's good practice to wrap previews in your app's theme
        ProfileScreen(user = fakeUser)
    }
}
