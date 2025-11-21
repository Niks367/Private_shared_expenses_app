package com.example.myapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.repositories.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val repo: StatisticsRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _transactions.value = repo.getAllTransactions()
        }
    }
}
