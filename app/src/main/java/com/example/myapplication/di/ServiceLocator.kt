package com.example.myapplication.di

import com.example.myapplication.RetrofitClient
import com.example.myapplication.repositories.UserRepository

object ServiceLocator {
    val userRepository: UserRepository by lazy {
        UserRepository(api = RetrofitClient.apiService)
    }
}