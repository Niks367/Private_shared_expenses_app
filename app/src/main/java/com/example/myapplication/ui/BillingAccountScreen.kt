package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun BillingAccountScreen(
    userName: String = "",
    onBackClick: () -> Unit = {},
    onSaveClick: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var zip by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Cards, 1 = Accounts

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
            // Header with curved bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // Curved background
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(0f, height - 40f)
                        
                        quadraticBezierTo(
                            width / 2f, height + 20f,
                            width, height - 40f
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
                
                // Decorative circles
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .offset(x = (-40).dp, y = (-10).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B5C58).copy(alpha = 0.3f))
                    )
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = 50.dp, y = (-10).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF438883).copy(alpha = 0.25f))
                    )
                }
                
                // Top bar content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 26.dp, start = 24.dp, end = 24.dp),
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
                        text = "Connect Wallet",
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
            
            // Content with offset
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-40).dp)
                    .padding(horizontal = 24.dp)
            ) {
                // Tab Selector
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Cards Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(28.dp))
                                .background(if (selectedTab == 0) White else Color.Transparent)
                                .clickable { selectedTab = 0 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Cards",
                                fontSize = 16.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selectedTab == 0) Color(0xFF2D3748) else Color(0xFF718096)
                            )
                        }
                        
                        // Accounts Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(28.dp))
                                .background(if (selectedTab == 1) White else Color.Transparent)
                                .clickable { selectedTab = 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Accounts",
                                fontSize = 16.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selectedTab == 1) Color(0xFF2D3748) else Color(0xFF718096)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Card Preview
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PrimaryTeal
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Card gradient circles decoration
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.1f),
                                radius = 200f,
                                center = Offset(size.width * 0.8f, size.height * 0.3f)
                            )
                            drawCircle(
                                color = Color.White.copy(alpha = 0.08f),
                                radius = 150f,
                                center = Offset(size.width * 0.2f, size.height * 0.7f)
                            )
                        }
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text(
                                        text = "Debit",
                                        color = White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Card",
                                        color = White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                
                                Text(
                                    text = "Mono",
                                    color = White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            Column {
                                // Card chip icon
                                Box(
                                    modifier = Modifier
                                        .size(40.dp, 30.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFE0C097))
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Card number
                                Text(
                                    text = if (cardNumber.isNotEmpty()) cardNumber else "6219  8610  2888  8075",
                                    color = White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 2.sp
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Card holder and expiry
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (cardHolder.isNotEmpty()) cardHolder.uppercase() else userName.uppercase(),
                                        color = White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = if (expiryDate.isNotEmpty()) expiryDate else "22/01",
                                        color = White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Form Section
                Text(
                    text = "Add your debit Card",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
                
                Text(
                    text = "This card must be connected to a bank account under your name",
                    fontSize = 13.sp,
                    color = Color(0xFF718096),
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // NAME ON CARD
                OutlinedTextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text("NAME ON CARD", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryTeal,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedLabelColor = PrimaryTeal,
                        unfocusedLabelColor = Color(0xFF718096)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // DEBIT CARD NUMBER and CVC
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = { Text("DEBIT CARD NUMBER", fontSize = 12.sp) },
                        modifier = Modifier.weight(2f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = Color(0xFF718096)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { cvv = it },
                        label = { Text("CVC", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = Color(0xFF718096)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // EXPIRATION MM/YY and ZIP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { expiryDate = it },
                        label = { Text("EXPIRATION MM/YY", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = Color(0xFF718096)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        placeholder = { Text("MM/YY", fontSize = 14.sp) }
                    )
                    
                    OutlinedTextField(
                        value = zip,
                        onValueChange = { zip = it },
                        label = { Text("ZIP", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PrimaryTeal,
                            unfocusedLabelColor = Color(0xFF718096)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Save button (maintain original functionality)
                Button(
                    onClick = {
                        onSaveClick(cardNumber, cardHolder, expiryDate, cvv)
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
                        text = "Add Card",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BillingAccountScreenPreview() {
    MaterialTheme {
        BillingAccountScreen(
            userName = "John Doe",
            onBackClick = {},
            onSaveClick = { _, _, _, _ -> }
        )
    }
}

