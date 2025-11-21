package com.example.myapplication.repositories

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.myapplication.interfaces.ApiService
import com.example.myapplication.interfaces.UserBalance
import com.example.myapplication.model.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val api: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getUserBalance(userId: String): Flow<Result<UserBalance>> = flow {
        val response = api.getUserBalance(userId)
        if (response.isSuccessful) {
            val body = response.body()
            val dto = body?.firstOrNull { it.id == userId }
            if (dto != null) {
                emit(Result.success(dto.toDomain()))
            } else {
                emit(Result.failure(NoSuchElementException("User $userId not found")))
            }
        } else {
        }
    }.flowOn(dispatcher)
}