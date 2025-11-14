package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.User
import com.example.myapplication.ui.theme.*

@Composable
fun PersonalInformationScreen(
    user: User,
    onBackClick: () -> Unit = {},
    onSaveClick: (String, String, String?) -> Unit = { _, _, _ -> }
) {
    var name by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "← Back",
                    color = PrimaryTeal,
                    fontSize = 16.sp
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PrimaryTeal,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Personal Information",
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(
                        "Full Name",
                        color = GrayText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryTeal,
                    unfocusedBorderColor = LightTeal,
                    focusedLabelColor = PrimaryTeal,
                    unfocusedLabelColor = GrayText
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "Email",
                        color = GrayText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryTeal,
                    unfocusedBorderColor = LightTeal,
                    focusedLabelColor = PrimaryTeal,
                    unfocusedLabelColor = GrayText
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Phone field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = {
                    Text(
                        "Phone Number",
                        color = GrayText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryTeal,
                    unfocusedBorderColor = LightTeal,
                    focusedLabelColor = PrimaryTeal,
                    unfocusedLabelColor = GrayText
                ),
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text("Optional")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            Button(
                onClick = {
                    onSaveClick(name, email, phone.takeIf { it.isNotBlank() })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryTeal
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Save Changes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalInformationScreenPreview() {
    val fakeUser = User(
        userId = "123",
        username = "Jane Doe",
        email = "jane.doe@example.com",
        phone = "+1 234 567 8900"
    )
    MaterialTheme {
        PersonalInformationScreen(
            user = fakeUser,
            onBackClick = {},
            onSaveClick = { _, _, _ -> }
        )
    }
}

