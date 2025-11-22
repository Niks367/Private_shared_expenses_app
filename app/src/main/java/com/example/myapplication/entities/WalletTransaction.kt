package com.example.myapplication.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "wallet_transactions",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WalletTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val type: String, // "add", "pay", "send", "income"
    val description: String,
    val amount: Double,
    val date: String
)

