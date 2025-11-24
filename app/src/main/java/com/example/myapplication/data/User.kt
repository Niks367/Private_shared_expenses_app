package com.example.myapplication.data

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String? = null,
    val fcmToken: String? = null
)