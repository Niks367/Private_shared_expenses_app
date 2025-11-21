package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.entities.TransactionEntity

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE user_id = :userId ORDER BY created_at DESC")
    suspend fun getTransactionsForUser(userId: String): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE user_id = :userId")
    suspend fun deleteForUser(userId: String)

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<TransactionEntity>


}
