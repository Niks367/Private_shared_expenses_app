package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.example.myapplication.entities.WalletTransaction
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    balance: Double,
    transactions: List<WalletTransaction>,
    onBackClick: () -> Unit,
    onAddMoney: (Double) -> Unit,
    onPay: (String, Double) -> Unit,
    onSend: (String, Double) -> Unit,
    showBackButton: Boolean = true
) {
    var showAddMoneyDialog by remember { mutableStateOf(false) }
    var showPayDialog by remember { mutableStateOf(false) }
    var showSendDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                    .height(320.dp)
            ) {
                // Curved background using Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(0f, height - 80f)
                        
                        // Curve di bagian bawah - lebih melengkung
                        quadraticBezierTo(
                            width / 2f, height + 40f,  // Control point lebih rendah
                            width, height - 80f         // End point
                        )
                        
                        lineTo(width, 0f)
                        close()
                    }
                    
                    drawPath(
                        path = path,
                        color = Color(0xFF4A9B8E),
                        style = Fill
                    )
                }
                
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
                            .background(Color(0xFF357A6E).copy(alpha = 0.3f))
                    )
                    
                    // Medium circle - top center-left
                    Box(
                        modifier = Modifier
                            .size(127.dp)
                            .offset(x = 59.dp, y = (-15).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5AA99A).copy(alpha = 0.25f))
                    )
                    
                    // Small circle - top center-right
                    Box(
                        modifier = Modifier
                            .size(85.dp)
                            .offset(x = 127.dp, y = (-22).dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6FBDAD).copy(alpha = 0.2f))
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
                    if (showBackButton) {
                        IconButton(onClick = onBackClick) {
                            Text(
                                text = "←",
                                color = Color.White,
                                fontSize = 28.sp
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    
                    Text(
                        text = "Wallet",
                        color = Color.White,
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
                
                // Balance info in header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 140.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Total Balance",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        formatCurrency(balance),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Action Buttons Card - naik lebih tinggi
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-60).dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WalletActionButton(
                        icon = R.drawable.income,
                        label = "Add",
                        onClick = { showAddMoneyDialog = true }
                    )
                    WalletActionButton(
                        icon = R.drawable.expense,
                        label = "Pay",
                        onClick = { showPayDialog = true }
                    )
                    WalletActionButton(
                        icon = R.drawable.transfer,
                        label = "Send",
                        onClick = { showSendDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(-30.dp))

            // Transactions Section
            Text(
                "Transactions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No transactions yet",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                transactions.forEach { transaction ->
                    WalletTransactionItem(transaction = transaction)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Dialogs
    if (showAddMoneyDialog) {
        MoneyInputDialog(
            title = "Add Money",
            onDismiss = { showAddMoneyDialog = false },
            onConfirm = { amount ->
                onAddMoney(amount)
                showAddMoneyDialog = false
            }
        )
    }

    if (showPayDialog) {
        PaymentDialog(
            title = "Pay",
            onDismiss = { showPayDialog = false },
            onConfirm = { recipient, amount ->
                onPay(recipient, amount)
                showPayDialog = false
            }
        )
    }

    if (showSendDialog) {
        PaymentDialog(
            title = "Send Money",
            onDismiss = { showSendDialog = false },
            onConfirm = { recipient, amount ->
                onSend(recipient, amount)
                showSendDialog = false
            }
        )
    }
}

@Composable
fun WalletActionButton(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F6F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = Color(0xFF4A9B8E),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D3748)
        )
    }
}

@Composable
fun WalletTransactionItem(transaction: WalletTransaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon based on transaction type
                val iconRes = when (transaction.type) {
                    "income", "add" -> R.drawable.income
                    "expense", "pay" -> R.drawable.expense
                    "transfer", "send" -> R.drawable.transfer
                    else -> R.drawable.expense
                }
                
                val iconBg = when (transaction.type) {
                    "income", "add" -> Color(0xFFE8F5E9)
                    "expense", "pay" -> Color(0xFFFFEBEE)
                    else -> Color(0xFFF0F6F5)
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF4A9B8E)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        transaction.description,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )
                    Text(
                        transaction.date,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            // Amount
            val isPositive = transaction.type in listOf("income", "add")
            Text(
                "${if (isPositive) "+" else "-"} ${formatCurrency(transaction.amount)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}

@Composable
fun MoneyInputDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { 
                        // Only allow numbers and decimal point
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                            showError = false
                        }
                    },
                    label = { Text("Amount") },
                    prefix = { Text("$") },
                    isError = showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0,
                    supportingText = {
                        if (showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0) {
                            Text("Amount must be greater than 0", color = Color.Red)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amountDouble = amount.toDoubleOrNull() ?: 0.0
                            
                            // Validate amount
                            if (amountDouble <= 0.0) {
                                showError = true
                                return@Button
                            }
                            
                            onConfirm(amountDouble)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A9B8E)
                        )
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit
) {
    var recipient by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = recipient,
                    onValueChange = { 
                        recipient = it
                        showError = false
                    },
                    label = { Text("Recipient/Description") },
                    isError = showError && recipient.isBlank(),
                    supportingText = {
                        if (showError && recipient.isBlank()) {
                            Text("Recipient/Description cannot be empty", color = Color.Red)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { 
                        // Only allow numbers and decimal point
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                            showError = false
                        }
                    },
                    label = { Text("Amount") },
                    prefix = { Text("$") },
                    isError = showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0,
                    supportingText = {
                        if (showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0) {
                            Text("Amount must be greater than 0", color = Color.Red)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amountDouble = amount.toDoubleOrNull() ?: 0.0
                            
                            // Validate inputs
                            if (recipient.isBlank() || amountDouble <= 0.0) {
                                showError = true
                                return@Button
                            }
                            
                            onConfirm(recipient, amountDouble)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A9B8E)
                        )
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}

