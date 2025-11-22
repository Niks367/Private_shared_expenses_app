package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    groupId: Long,
    onAddExpense: (groupId: Long, description: String, amount: Double, date: String) -> Unit,
    onBackClick: () -> Unit // Add a parameter for the back navigation
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { // Use the passed-in navigation action
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Add Expense to Group $groupId")
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
                isError = showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0,
                supportingText = {
                    if (showError && (amount.toDoubleOrNull() ?: 0.0) <= 0.0) {
                        Text("Amount must be greater than 0", color = Color.Red)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = { /* TODO: Handle receipt attachment */ }) {
                Text("Attach Receipt")
            }
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    
                    // Validate inputs
                    if (description.isBlank() || amountDouble <= 0.0) {
                        showError = true
                        return@Button
                    }
                    
                    // Format date as readable string (e.g., "2025-11-22")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentDate = dateFormat.format(Date())
                    onAddExpense(groupId, description, amountDouble, currentDate)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Expense")
            }
        }
    }
}
