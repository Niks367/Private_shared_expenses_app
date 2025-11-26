package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.entities.Group
import com.example.myapplication.ui.theme.PrimaryTeal
import com.example.myapplication.ui.theme.White

@Composable
fun ExpensesScreen(
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
            
            // HEADER with curved bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Curved background using Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(0f, height - 60f)
                        
                        // Curve at the bottom
                        quadraticBezierTo(
                            width / 2f, height + 20f,  // Control point
                            width, height - 60f         // End point
                        )
                        
                        lineTo(width, 0f)
                        close()
                    }
                    
                    drawPath(
                        path = path,
                        color = PrimaryTeal,
                        style = Fill
                    )
                }

                // Decorative circles in background
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

                // Top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 26.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(48.dp))
                    
                    Text(
                        text = "Expenses",
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

            // BODY CONTENT
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-60).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ICON
                Box(
                    modifier = Modifier
                        .offset(y = (-10).dp)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💸", fontSize = 48.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select a Group to Add an Expense",
                    fontSize = 20.sp,
                    color = Color(0xFF222222),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Group list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(groups) { group ->
                        GroupListItem(group = group, onClick = { onGroupClick(group) })
                    }
                }
            }
        }
    }
}