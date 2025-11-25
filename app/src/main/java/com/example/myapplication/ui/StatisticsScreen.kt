package com.example.myapplication.ui

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.entities.WalletTransaction
import com.example.myapplication.entities.Expense
import com.example.myapplication.repositories.StatisticsRepository
import com.example.myapplication.ui.viewmodels.StatisticsViewModel
import com.example.myapplication.ui.viewmodels.StatisticsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatisticsScreen(
    onBack: () -> Unit = {},
    userId: Long = 1L // Default user for now
) {
    val tabs = listOf("Day", "Week", "Month", "Year")
    var selectedTab by remember { mutableStateOf(0) }
    var isExpense by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    
    // Get wallet transactions and expenses from local database
    val walletTransactions by db.walletTransactionDao()
        .getTransactionsForUser(userId)
        .collectAsState(initial = emptyList())
    
    val expenses by db.expenseDao()
        .getExpensesForUser(userId)
        .collectAsState(initial = emptyList())

    // Combine and convert to display format
    val transactions = remember(walletTransactions, expenses, isExpense) {
        if (isExpense) {
            expenses.map { 
                TransactionEntity(
                    id = it.id,
                    userId = userId.toString(),
                    title = it.description,
                    category = it.description,
                    amount = it.amount,
                    type = "expense",
                    createdAt = System.currentTimeMillis(),
                    date = it.date
                )
            }
        } else {
            walletTransactions.filter { it.type != "send" }.map {
                TransactionEntity(
                    id = it.id,
                    userId = userId.toString(),
                    title = it.description,
                    category = it.type,
                    amount = it.amount,
                    type = "income",
                    createdAt = System.currentTimeMillis(),
                    date = it.date
                )
            }
        }
    }

    val chartValues = transactions.map { it.amount.toFloat() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(modifier = Modifier.weight(1f)) {
                    tabs.forEachIndexed { index, title ->
                        val selected = index == selectedTab
                        val bg by animateColorAsState(
                            if (selected) Color(0xFF2F9E89) else Color(0xFFEFF6F4)
                        )
                        val contentColor =
                            if (selected) Color.White else Color(0xFF2F9E89)

                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(36.dp)
                                .background(bg, shape = RoundedCornerShape(12.dp))
                                .clickable { selectedTab = index }
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(title, color = contentColor, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                var expanded by remember { mutableStateOf(false) }

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(if (isExpense) "Expense" else "Income")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Expense") },
                            onClick = {
                                isExpense = true
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Income") },
                            onClick = {
                                isExpense = false
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    ) {
                        SmoothLineChart(points = chartValues)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Mar")
                        Text("Apr")
                        Text("May", color = Color(0xFF2F9E89), fontWeight = FontWeight.Bold)
                        Text("Jun")
                        Text("Jul")
                        Text("Aug")
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Top Spending", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(transactions) { item ->
                    SpendingRow(item)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun SpendingRow(item: TransactionEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFEFF6F4), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.title.take(1), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.SemiBold)
                Text(item.date, color = Color.Gray, fontSize = 12.sp)
            }

            Text(
                (if (item.type == "expense") "- $" else "+ $") +
                        String.format("%.2f", item.amount),
                color = if (item.type == "expense") Color(0xFFFF6B6B) else Color(0xFF2F9E89),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SmoothLineChart(points: List<Float>) {
    if (points.isEmpty()) {
        // Show placeholder when no data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No data available",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        return
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val max = points.maxOrNull() ?: 1f
        val min = points.minOrNull() ?: 0f
        val range = (max - min).takeIf { it != 0f } ?: 1f

        // Handle case with only 1 point
        val step = if (points.size > 1) w / (points.size - 1) else w / 2f

        val pts = points.mapIndexed { idx, v ->
            val x = if (points.size == 1) w / 2f else idx * step
            Offset(x, h - ((v - min) / range) * h)
        }

        // Only draw area and line if we have at least 2 points
        if (pts.size >= 2) {
            val area = Path().apply {
                moveTo(pts.first().x, pts.first().y)
                for (i in 0 until pts.lastIndex) {
                    val p0 = pts[i]
                    val p1 = pts[i + 1]
                    val mid = (p0.x + p1.x) / 2f
                    cubicTo(mid, p0.y, mid, p1.y, p1.x, p1.y)
                }
                lineTo(pts.last().x, h)
                lineTo(pts.first().x, h)
                close()
            }

            drawPath(
                path = area,
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFBEEADE).copy(alpha = 0.6f), Color.Transparent)
                ),
                style = Fill
            )

            val line = Path().apply {
                moveTo(pts.first().x, pts.first().y)
                for (i in 0 until pts.lastIndex) {
                    val p0 = pts[i]
                    val p1 = pts[i + 1]
                    val mid = (p0.x + p1.x) / 2f
                    cubicTo(mid, p0.y, mid, p1.y, p1.x, p1.y)
                }
            }

            drawPath(
                path = line,
                color = Color(0xFF2F9E89),
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )
        }
        
        // Draw data points
        pts.forEach { pt ->
            drawCircle(
                color = Color(0xFF2F9E89),
                radius = 6f,
                center = pt,
                style = Fill
            )
            drawCircle(
                color = Color.White,
                radius = 3f,
                center = pt,
                style = Fill
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsPreview() {
    MaterialTheme {
        StatisticsScreen()
    }
}
