package com.example.myapplication.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
        @PrimaryKey(autoGenerate = true) val id: String = 0,
        val name: String,
        val email: String?,
        val phone: String?,
        val passwordHash: String   // store a hash, never plain text!
)