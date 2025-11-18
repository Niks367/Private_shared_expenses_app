package com.example.myapplication.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.User
import com.example.myapplication.ui.theme.*

@Composable
fun ProfileScreen(
    user: User,
    onBackClick: () -> Unit = {},
    onPersonalInfoClick: () -> Unit = {},
    onBillingAccountClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    // Get initials for avatar
    val initials = user.username
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")
        .take(2)

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

        // Profile content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile header card with avatar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PrimaryTeal,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar circle with initials
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryTeal
                    )
                }

                // Name
                Text(
                    text = user.username,
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Email
                Text(
                    text = user.email,
                    color = LightTeal,
                    fontSize = 16.sp
                )

                // Phone number (if available)
                user.phone?.let { phone ->
                    Text(
                        text = phone,
                        color = LightTeal,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Personal Information Button
            Button(
                onClick = onPersonalInfoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryTeal
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personal Information",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Text(
                        text = "→",
                        fontSize = 20.sp,
                        color = White
                    )
                }
            }

            // Billing Account Button
            Button(
                onClick = onBillingAccountClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryTeal
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Billing Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Text(
                        text = "→",
                        fontSize = 20.sp,
                        color = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ExpenseRed
                ),
                border = BorderStroke(2.dp, ExpenseRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ExpenseRed
                )
            }
        }
    }
}

// This preview allows you to see your UI without running the app
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // We create a fake user for the preview
    val fakeUser = User(
        userId = "123",
        username = "Jane Doe",
        email = "jane.doe@example.com",
        phone = "+1 234 567 8900"
    )
    MaterialTheme {
        ProfileScreen(
            user = fakeUser,
            onBackClick = {},
            onPersonalInfoClick = {},
            onBillingAccountClick = {},
            onLogoutClick = {}
        )
    }
}
