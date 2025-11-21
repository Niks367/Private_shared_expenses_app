package com.example.myapplication.interfaces

import com.example.myapplication.model.BalanceDto
import com.example.myapplication.model.Transaction
import com.example.myapplication.model.UserBalanceDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUserBalance(
        @Query("id") userId: String
    ): Response<List<UserBalanceDto>>
}

data class UserBalance(
    val id: String,
    val balance: BalanceDto,
    val transactions: List<Transaction> = emptyList()
)
