package com.example.myapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.Profile
import com.example.myapplication.ui.viewmodels.GroupDetailsViewModel

@Composable
fun GroupDetailsScreen(
    groupId: Long, 
    onAddExpense: (Long) -> Unit, 
    onViewBalance: (Long) -> Unit = {},
    viewModel: GroupDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        uiState.group?.let {
            Text(text = "Group: ${it.name}")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Action buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onAddExpense(groupId) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A9B8E)
                )
            ) {
                Text("Add Expense")
            }
            
            OutlinedButton(
                onClick = { onViewBalance(groupId) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF4A9B8E)
                )
            ) {
                Text("View Balance")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Members:")
        LazyColumn {
            items(uiState.members) { member ->
                Text(text = member.firstName)
            }
        }

        Text(text = "Expenses:")
        LazyColumn {
            items(uiState.expenses) { expense ->
                Text(text = "${expense.description}: ${expense.amount}")
            }
        }
    }
}