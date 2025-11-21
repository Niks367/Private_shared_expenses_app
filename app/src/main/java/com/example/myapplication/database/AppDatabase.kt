package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.dao.ExpenseDao
import com.example.myapplication.dao.GroupDao
import com.example.myapplication.dao.GroupMemberDao
import com.example.myapplication.dao.ProfileDao
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.GroupMember
import com.example.myapplication.entities.Profile

@Database(entities = [Profile::class, Group::class, GroupMember::class, Expense::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun groupDao(): GroupDao
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "userData.db"
                    ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
                }
    }
}