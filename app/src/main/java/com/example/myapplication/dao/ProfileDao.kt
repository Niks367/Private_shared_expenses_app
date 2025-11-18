package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.entities.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): Profile?

    @Query("SELECT * FROM profile WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): Profile?

    @Insert
    suspend fun insert(profile: Profile)
}
