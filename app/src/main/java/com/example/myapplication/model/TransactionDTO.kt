package com.example.myapplication.model

import com.example.myapplication.R

data class TransactionDto(
    val icon: String,
    val title: String,
    val date: String,
    val amount: Double,
    val type: String
)
fun TransactionDto.toDomain(): Transaction = Transaction(
    iconRes = when (icon) {
        "upwork"   -> R.drawable.upwork
        "transfer" -> R.drawable.transfer
        else -> {R.drawable.transfer}
    },
    title = title,
    date = date,
    amount = amount,
    isIncome = type.equals("income", ignoreCase = true)
)