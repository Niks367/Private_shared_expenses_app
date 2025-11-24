package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun TransactionDetailsScreen(
    transactionId: String,
    transactionType: String, // "income" or "expense"
    description: String,
    amount: Double,
    date: String,
    onBackClick: () -> Unit = {}
) {
    val isIncome = transactionType == "income" || transactionType == "add"
    val fee = 20.0 // Example fee
    val total = if (isIncome) amount - fee else amount + 0.99
    
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
                
                // Top bar
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
                        text = "Transaction Details",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    IconButton(onClick = { /* TODO: More options */ }) {
                        Text(
                            text = "⋮",
                            color = White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Content Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-40).dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon/Image
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(if (isIncome) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isIncome) "↑" else "↓",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Type Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isIncome) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    ) {
                        Text(
                            text = if (isIncome) "Income" else "Expense",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Amount
                    Text(
                        text = formatCurrency(amount),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Transaction Details Section
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Transaction details",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2D3748)
                            )
                            Text(
                                text = "^",
                                fontSize = 20.sp,
                                color = Color(0xFF718096)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Status
                        DetailRow(
                            label = "Status",
                            value = if (isIncome) "Income" else "Expense",
                            valueColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                        
                        // From/To
                        DetailRow(
                            label = if (isIncome) "From" else "To",
                            value = description
                        )
                        
                        // Time
                        DetailRow(
                            label = "Time",
                            value = "10:00 AM"
                        )
                        
                        // Date
                        DetailRow(
                            label = "Date",
                            value = date
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Breakdown
                        DetailRow(
                            label = if (isIncome) "Earnings" else "Spending",
                            value = formatCurrency(amount)
                        )
                        
                        DetailRow(
                            label = "Fee",
                            value = if (isIncome) "- ${formatCurrency(fee)}" else "- ${formatCurrency(0.99)}"
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2D3748)
                            )
                            Text(
                                text = formatCurrency(total),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = Color(0xFF2D3748)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF718096)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

