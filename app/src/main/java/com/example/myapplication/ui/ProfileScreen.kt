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

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(287.dp)
                    .background(PrimaryTeal)
            ) {
                // Decorative circles
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(212.dp)
                            .offset(x = (-55).dp, y = (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B5C58).copy(alpha = 0.3f))
                    )
                    Box(
                        modifier = Modifier
                            .size(127.dp)
                            .offset(x = 59.dp, y = (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF438883).copy(alpha = 0.25f))
                    )
                    Box(
                        modifier = Modifier
                            .size(85.dp)
                            .offset(x = 127.dp, y = (-22).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5BA89E).copy(alpha = 0.2f))
                    )
                }

                // --- FIXED HEADER TITLE ALIGNMENT ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Profile",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(top = 56.dp, start = 24.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "←",
                        color = White,
                        fontSize = 28.sp
                    )
                }

                // Bell Icon (unchanged)
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .padding(top = 56.dp, end = 24.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "🔔",
                        fontSize = 24.sp,
                        color = White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-120).dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // --- FIXED AVATAR POSITION ---
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .offset(y = (-35).dp)   // << same as GroupScreens
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
                        onClick = { }
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
                        onClick = { }
                    )

                    ProfileMenuItem(
                        icon = "🛡️",
                        title = "Login and security",
                        onClick = { }
                    )

                    ProfileMenuItem(
                        icon = "🔒",
                        title = "Data and privacy",
                        onClick = { }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

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

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val fakeUser = User(
        userId = "123",
        username = "Jane Doe",
        email = "jane.doe@example.com",
        phone = "+1 234 567 8900"
    )
    MaterialTheme {
        ProfileScreen(fakeUser)
    }
}
