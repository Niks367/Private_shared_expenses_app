package com.example.myapplication

import com.example.myapplication.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Use 10.0.2.2 for Android Emulator (points to localhost on host machine)
    // Change to your computer's IP if using physical device
    private const val BASE_URL = "http://10.0.2.2:3000/"


    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}