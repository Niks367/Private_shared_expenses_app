package com.example.myapplication.model

import com.example.myapplication.interfaces.UserBalance

data class UserBalanceDto(
    val id: String,
    val balance: BalanceDto,
    val transactions: List<TransactionDto>? = null
)
fun UserBalanceDto.toDomain(): UserBalance = UserBalance(
    id = id,
    balance = BalanceDto(
        total = balance.total,
        income = balance.income,
        expense = balance.expense
    ),
    transactions = transactions?.map { it.toDomain() } ?: emptyList()
)