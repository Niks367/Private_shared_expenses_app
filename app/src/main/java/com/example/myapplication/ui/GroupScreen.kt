package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.entities.Group
import com.example.myapplication.ui.theme.PrimaryTeal
import com.example.myapplication.ui.theme.White

@Composable
fun GroupScreen(
    navController: NavController,
    groups: List<Group>,
    onGroupClick: (Group) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // HEADER (copied style from ProfileScreen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(287.dp)
                    .background(PrimaryTeal)
            ) {

                // Decorative circles (same as profile)
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

                // Top bar (same layout as Profile)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←", color = White, fontSize = 28.sp)
                    }

                    Text(
                        text = "Groups",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Removed the bell icon → keep this empty space
                    Box(modifier = Modifier.size(24.dp))
                }
            }

            // BODY CONTENT (overlapping)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-120).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Avatar circle with emoji
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👥", fontSize = 48.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Your Groups",
                    fontSize = 20.sp,
                    color = Color(0xFF222222)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Create Group Button (same color as profile header)
                Button(
                    onClick = { navController.navigate("createGroup") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryTeal
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Create New Group",
                        fontSize = 16.sp,
                        color = White
                    )
                }

                Spacer(modifier = Modifier.height(35.dp))

                // Group list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(groups) { group ->
                        GroupListItem(group, onClick = { onGroupClick(group) })
                    }
                }
            }
        }
    }
}

@Composable
fun GroupListItem(group: Group, onClick: () -> Unit) {
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
            Text("📁", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(group.name, fontSize = 16.sp, color = Color.Black)
            Text(group.description, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupScreenPreview() {
    val sampleGroups = listOf(
        Group(1, "School Group", "Friends from school"),
        Group(2, "Work Team", "Colleagues from office")
    )
    GroupScreen(rememberNavController(), sampleGroups, {})
}
