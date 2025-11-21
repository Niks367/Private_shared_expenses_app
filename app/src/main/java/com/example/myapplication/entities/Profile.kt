package com.example.myapplication.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val passwordHash: String
)
@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile)

    @Query("SELECT * FROM profile WHERE id = :id")
    suspend fun getById(id: Long): Profile?
}