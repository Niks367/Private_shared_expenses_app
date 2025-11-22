package com.example.myapplication.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with decorative circles and curved bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(287.dp)
                    .background(PrimaryTeal)
            ) {
                // Decorative circles in background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    // Large circle - bottom left
                    Box(
                        modifier = Modifier
                            .size(212.dp)
                            .offset(x = (-55).dp, y = (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B5C58).copy(alpha = 0.3f))
                    )
                    
                    // Medium circle - top center-left
                    Box(
                        modifier = Modifier
                            .size(127.dp)
                            .offset(x = 59.dp, y = (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF438883).copy(alpha = 0.25f))
                    )
                    
                    // Small circle - top center-right
                    Box(
                        modifier = Modifier
                            .size(85.dp)
                            .offset(x = 127.dp, y = (-22).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5BA89E).copy(alpha = 0.2f))
                    )
                }
                
                // Top bar content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "←",
                            color = White,
                            fontSize = 28.sp
                        )
                    }
                    
                    Text(
                        text = "Profile",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Text(
                            text = "🔔",
                            fontSize = 24.sp
                        )
                    }
                }
            }

            // White background for content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-80).dp)
            ) {
                // User avatar (overlapping header)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Large avatar
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryTeal
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Username
                    Text(
                        text = user.username,
                        color = Color(0xFF222222),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(40.dp))
                }
                
                // Menu items
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                ) {
                    ProfileMenuItem(
                        icon = "💎",
                        title = "Invite Friends",
                        onClick = { /* TODO */ }
                    )
                    
                    HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                    
                    ProfileMenuItem(
                        icon = "💳",
                        title = "Billing Account info",
                        onClick = onBillingAccountClick
                    )
                    
                    ProfileMenuItem(
                        icon = "👥",
                        title = "Personal profile",
                        onClick = onPersonalInfoClick
                    )
                    
                    ProfileMenuItem(
                        icon = "✉️",
                        title = "Message center",
                        onClick = { /* TODO */ }
                    )
                    
                    ProfileMenuItem(
                        icon = "🛡️",
                        title = "Login and security",
                        onClick = { /* TODO */ }
                    )
                    
                    ProfileMenuItem(
                        icon = "🔒",
                        title = "Data and privacy",
                        onClick = { /* TODO */ }
                    )
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    // Logout button
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
                    
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: String,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F6F5)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )
        }
        
        Spacer(modifier = Modifier.width(20.dp))
        
        // Title
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun OldProfileButtons(
    onPersonalInfoClick: () -> Unit,
    onBillingAccountClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
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

        Spacer(modifier = Modifier.height(8.dp))

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
