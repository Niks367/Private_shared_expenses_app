package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): Profile?

    @Query("SELECT * FROM profile WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): Profile?
    @Query("SELECT groupId FROM group_members WHERE profileId = :profileId")
    suspend fun getGroupIdsForProfile(profileId: Long): List<Long>

    @Insert
    suspend fun insert(profile: Profile)
    @Query("SELECT * FROM profile WHERE firstName = :firstName")
    suspend fun findByFullName(firstName: String): Profile?
    @Query("SELECT * FROM profile WHERE email = :emailOrPhone OR phone = :emailOrPhone LIMIT 1")
    suspend fun findByEmailOrPhone(emailOrPhone: String): Profile?
}
