package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.PrimaryTeal
import com.example.myapplication.ui.theme.White

@Composable
fun CreateGroupScreen(
    navController: NavController,
    onCreateGroup: (String, String) -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(287.dp)
                    .background(PrimaryTeal)
            ) {

                // Decorative background circles
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(212.dp)
                            .offset((-55).dp, (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B5C58).copy(alpha = 0.3f))
                    )
                    Box(
                        modifier = Modifier
                            .size(127.dp)
                            .offset(59.dp, (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF438883).copy(alpha = 0.25f))
                    )
                    Box(
                        modifier = Modifier
                            .size(85.dp)
                            .offset(127.dp, (-22).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5BA89E).copy(alpha = 0.2f))
                    )
                }

                // TOP BAR — shifted VERY slightly to the right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp, start = 24.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←", color = White, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.weight(0.75f)) // slightly more right than before

                    Text(
                        text = "Create Group",
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.weight(1.25f))
                }
            }

            // BODY
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-115).dp)
                    .padding(horizontal = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ICON – kept same height
                Box(
                    modifier = Modifier
                        .offset(y = (-55).dp)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧑‍🤝‍🧑", fontSize = 48.sp)
                }

                Spacer(Modifier.height(10.dp))

                // Title
                Text(
                    text = "Create a New Group",
                    fontSize = 20.sp,
                    color = Color(0xFF222222),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(30.dp))

                OutlinedTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = { Text("Group Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = groupDescription,
                    onValueChange = { groupDescription = it },
                    label = { Text("Group Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = { onCreateGroup(groupName, groupDescription) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryTeal
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Create Group", fontSize = 16.sp, color = White)
                }
            }
        }
    }
}
