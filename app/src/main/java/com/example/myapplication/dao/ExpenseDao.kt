package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense): Long

    @Query("SELECT * FROM expenses WHERE groupId = :groupId")
    fun getExpensesForGroup(groupId: Long): Flow<List<Expense>>
    
    @Query("SELECT * FROM expenses WHERE paidBy = :userId ORDER BY date DESC")
    fun getExpensesForUser(userId: Long): Flow<List<Expense>>
    
    @Query("SELECT * FROM expenses WHERE paidBy = :userId ORDER BY date DESC")
    suspend fun getUserExpenses(userId: Long): List<Expense>
    
    @Query("SELECT * FROM expenses WHERE groupId = :groupId ORDER BY date DESC")
    suspend fun getExpensesForGroupSync(groupId: Long): List<Expense>
}