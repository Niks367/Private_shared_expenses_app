package com.example.myapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.dao.ExpenseDao
import com.example.myapplication.entities.Group

@Composable
fun GroupScreen(
    navController: NavController,
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    expenseDao: ExpenseDao,
    userId: Long // Added userId
) {
    val expenses by expenseDao.getAllExpenses().collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    var lastShownTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(expenses) {
        val newExpense = expenses.firstOrNull { it.timestamp > lastShownTimestamp }
        if (newExpense != null) {
            snackbarHostState.showSnackbar("New expense added!")
            lastShownTimestamp = newExpense.timestamp
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Text(
                text = "Group Management",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Updated onClick to include userId
            Button(onClick = { navController.navigate("createGroup/$userId") }) {
                Text("Create New Group")
            }
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(groups) { group ->
                    GroupListItem(group = group, onClick = { onGroupClick(group) })
                }
            }
        }
    }
}

@Composable
fun GroupListItem(group: Group, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(text = group.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = group.description, style = MaterialTheme.typography.bodyMedium)
    }
}
