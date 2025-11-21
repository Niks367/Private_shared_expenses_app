package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.dao.ProfileDao
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.entities.Profile
import com.example.myapplication.entities.TransactionEntity

@Database(entities = [Profile::class, TransactionEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "userData.db"
                ).fallbackToDestructiveMigration(true).build().also { INSTANCE = it }
            }
    }
}
