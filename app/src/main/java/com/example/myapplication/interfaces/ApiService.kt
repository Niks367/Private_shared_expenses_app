package com.example.myapplication.interfaces

import com.example.myapplication.model.BalanceDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUserBalance(
        @Query("id") userId: String
    ): Response<List<UserBalance>>
}

data class ApiResponse(
    val users: List<UserBalance>
)

data class UserBalance(
    val id: String,
    val balance: BalanceDto
)