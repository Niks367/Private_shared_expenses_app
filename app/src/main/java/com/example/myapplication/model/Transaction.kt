package com.example.myapplication.model

import androidx.annotation.DrawableRes
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.WalletTransaction

data class Transaction(
    @DrawableRes val iconRes: Int,
    val title: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean,
    val walletTransaction: WalletTransaction? = null,
    val expense: Expense? = null
)