package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.PrimaryTeal
import com.example.myapplication.ui.theme.White
import com.example.myapplication.ui.viewmodels.GroupDetailsViewModel
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Profile

@Composable
fun GroupDetailsScreen(
    groupId: Long,
    onAddExpense: (Long) -> Unit,
    viewModel: GroupDetailsViewModel = viewModel(),
    navController: NavController? = null
) {
    val uiState by viewModel.uiState.collectAsState()

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

                // Centered header title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Group Details",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier
                        .padding(top = 56.dp, start = 24.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text("←", color = White, fontSize = 28.sp)
                }
            }

            // BODY
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-130).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ICON (same position as GroupScreen)
                Box(
                    modifier = Modifier
                        .offset(y = (-35).dp)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💲", fontSize = 48.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // GROUP NAME (NOT "Group Details")
                Text(
                    text = uiState.group?.name ?: "Group",
                    fontSize = 20.sp,
                    color = Color(0xFF222222)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = { onAddExpense(groupId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryTeal
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add Expense", fontSize = 16.sp, color = White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Members",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF444444),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                ) {
                    items(uiState.members) { member: Profile ->
                        Text(
                            text = "• ${member.firstName} ${member.lastName}",
                            modifier = Modifier.padding(vertical = 6.dp),
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    "Expenses",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF444444),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    contentPadding = PaddingValues(bottom = 40.dp)
                ) {
                    items(uiState.expenses) { exp: Expense ->
                        Text(
                            text = "• ${exp.description}: ${exp.amount}",
                            modifier = Modifier.padding(vertical = 6.dp),
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}
