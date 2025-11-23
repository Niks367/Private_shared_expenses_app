package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.PrimaryTeal
import com.example.myapplication.ui.theme.White
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    groupId: Long,
    onAddExpense: (groupId: Long, description: String, amount: Double, date: String) -> Unit,
    onBackClick: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        // ---------------- HEADER ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(PrimaryTeal)
        ) {

            // background circles
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .offset(x = (-50).dp, y = (-20).dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B5C58).copy(alpha = 0.25f))
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(x = 80.dp, y = (-10).dp)
                    .clip(CircleShape)
                    .background(Color(0xFF438883).copy(alpha = 0.25f))
            )

            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 20.dp, top = 50.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = White
                )
            }

            // Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Expense",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------------- INPUT CARD ----------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {

                Text(
                    text = "Expense for Group $groupId",
                    fontSize = 16.sp,
                    color = Color(0xFF2D3748),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Description ————————————
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        showError = false
                    },
                    label = { Text("Description") },
                    isError = showError && description.isBlank(),
                    supportingText = {
                        if (showError && description.isBlank()) {
                            Text("Description cannot be empty", color = Color.Red)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Amount ————————————
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
                    isError = showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0,
                    supportingText = {
                        if (showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0) {
                            Text("Amount must be greater than 0", color = Color.Red)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Attach Receipt ————————————
                Button(
                    onClick = { /* handle later */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
                ) {
                    Text("Attach Receipt", color = White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Add Expense ————————————
                Button(
                    onClick = {
                        val amountDouble = amount.toDoubleOrNull() ?: 0.0

                        if (description.isBlank() || amountDouble <= 0) {
                            showError = true
                            return@Button
                        }

                        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                        onAddExpense(groupId, description, amountDouble, date)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
                ) {
                    Text("Add Expense", color = White)
                }
            }
        }
    }
}
