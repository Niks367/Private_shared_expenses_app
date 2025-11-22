package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.entities.WalletTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletTransactionDao {
    @Insert
    suspend fun insert(transaction: WalletTransaction): Long

    @Query("SELECT * FROM wallet_transactions WHERE userId = :userId ORDER BY date DESC")
    fun getTransactionsForUser(userId: Long): Flow<List<WalletTransaction>>

    @Query("SELECT * FROM wallet_transactions WHERE userId = :userId ORDER BY date DESC")
    suspend fun getUserTransactions(userId: Long): List<WalletTransaction>

    @Query("SELECT SUM(CASE WHEN type IN ('add', 'income') THEN amount ELSE -amount END) FROM wallet_transactions WHERE userId = :userId")
    fun getUserBalance(userId: Long): Flow<Double?>
    
    @Query("SELECT SUM(CASE WHEN type IN ('add', 'income') THEN amount ELSE -amount END) FROM wallet_transactions WHERE userId = :userId")
    suspend fun getBalance(userId: Long): Double?
}

