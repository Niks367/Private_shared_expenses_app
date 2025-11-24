package com.example.myapplication.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(entity = Group::class, parentColumns = ["id"], childColumns = ["groupId"]),
        ForeignKey(entity = Profile::class, parentColumns = ["id"], childColumns = ["paidBy"])
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val groupId: Long,
    val description: String,
    val amount: Double,
    val paidBy: Long,
    val date: String,
    val timestamp: Long = System.currentTimeMillis()
)