package com.example.myapplication.model

import androidx.annotation.DrawableRes

data class Transaction(
    @DrawableRes val iconRes: Int,
    val title: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)