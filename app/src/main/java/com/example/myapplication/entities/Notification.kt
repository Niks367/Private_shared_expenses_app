package com.example.myapplication.entities

data class Notification(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
