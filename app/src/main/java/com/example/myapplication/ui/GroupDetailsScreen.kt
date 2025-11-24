package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.entities.Expense
import com.example.myapplication.ui.viewmodels.GroupDetailsViewModel

@Composable
fun GroupDetailsScreen(groupId: Long, onAddExpense: (Long) -> Unit, viewModel: GroupDetailsViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        uiState.group?.let {
            Text(text = "Group: ${it.name}")
        }

        Button(onClick = { onAddExpense(groupId) }) {
            Text("Add Expense")
        }

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
