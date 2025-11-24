package com.example.myapplication.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "category") val category: String? = null,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "date") val date: String
)
