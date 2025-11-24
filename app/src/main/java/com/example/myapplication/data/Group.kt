package com.example.myapplication.data

data class Group(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val members: List<String> = emptyList()
)