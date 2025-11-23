package com.example.myapplication.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.example.myapplication.entities.WalletTransaction
import com.example.myapplication.ui.theme.PrimaryTeal
import com.example.myapplication.ui.theme.White
import java.text.NumberFormat
import java.util.Locale

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {

        // ---------------- HEADER ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(287.dp)
                .background(PrimaryTeal)
        ) {
            // circles
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

            // title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Wallet",
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // ---------------- BACK ARROW ----------------
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(top = 56.dp, start = 24.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text("←", color = White, fontSize = 28.sp)
                }
            }

            // bell
            IconButton(
                onClick = {},
                modifier = Modifier
                    .padding(top = 56.dp, end = 24.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = null,
                    tint = White
                )
            }

            // ADD / PAY / SEND inside header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
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
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ---------------- TOTAL BALANCE ----------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Balance:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3748)
                )
                Text(
                    text = formatCurrency(balance),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryTeal
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------------- TRANSACTIONS TITLE ----------------
        Text(
            "Transactions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )

        // ---------------- LIST ----------------
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No transactions yet", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            transactions.forEach { item ->
                WalletTransactionItem(item)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    // dialogs
    if (showAddMoneyDialog) {
        MoneyInputDialog(
            title = "Add Money",
            onDismiss = { showAddMoneyDialog = false },
            onConfirm = {
                onAddMoney(it)
                showAddMoneyDialog = false
            }
        )
    }

    if (showPayDialog) {
        PaymentDialog(
            title = "Pay",
            onDismiss = { showPayDialog = false },
            onConfirm = { receiver, amount ->
                onPay(receiver, amount)
                showPayDialog = false
            }
        )
    }

    if (showSendDialog) {
        PaymentDialog(
            title = "Send Money",
            onDismiss = { showSendDialog = false },
            onConfirm = { receiver, amount ->
                onSend(receiver, amount)
                showSendDialog = false
            }
        )
    }
}

@Composable
fun WalletActionButton(icon: Int, label: String, onClick: () -> Unit) {
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
                tint = PrimaryTeal,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
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

            Row(verticalAlignment = Alignment.CenterVertically) {

                val iconRes = when (transaction.type.lowercase()) {
                    "income", "add" -> R.drawable.income
                    "expense", "pay" -> R.drawable.expense
                    "transfer", "send" -> R.drawable.transfer
                    else -> R.drawable.expense
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F6F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = PrimaryTeal,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        transaction.description,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(transaction.date, fontSize = 13.sp, color = Color.Gray)
                }
            }

            val positive = transaction.type in listOf("income", "add")

            Text(
                text = "${if (positive) "+" else "-"} ${formatCurrency(transaction.amount)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (positive) Color(0xFF4CAF50) else Color(0xFFF44336)
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
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {

                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                            showError = false
                        }
                    },
                    label = { Text("Amount") },
                    prefix = { Text("$") },
                    isError = showError
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val value = amount.toDoubleOrNull() ?: 0.0
                        if (value <= 0) {
                            showError = true
                            return@Button
                        }
                        onConfirm(value)
                    }) {
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
    var receiver by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {

                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = receiver,
                    onValueChange = { receiver = it },
                    label = { Text("Recipient/Description") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                        }
                    },
                    label = { Text("Amount") },
                    prefix = { Text("$") }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val value = amount.toDoubleOrNull() ?: 0.0
                        if (receiver.isBlank() || value <= 0) {
                            return@Button
                        }
                        onConfirm(receiver, value)
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}
