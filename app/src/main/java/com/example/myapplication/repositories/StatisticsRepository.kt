package com.example.myapplication.repositories

import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.entities.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatisticsRepository(
    private val transactionDao: TransactionDao
) {

    suspend fun getAllTransactions(): List<TransactionEntity> =
        withContext(Dispatchers.IO) {
            transactionDao.getAll()
        }
}
