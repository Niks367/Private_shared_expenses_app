package com.example.myapplication.database

import android.content.Context
import android.util.Base64
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.dao.ExpenseDao
import com.example.myapplication.dao.GroupDao
import com.example.myapplication.dao.GroupMemberDao
import com.example.myapplication.dao.ProfileDao
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.GroupMember
import com.example.myapplication.entities.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Database(entities = [Profile::class, Group::class, GroupMember::class, Expense::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun groupDao(): GroupDao
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private fun hashPassword(password: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(bytes, Base64.NO_WRAP)
        }

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "userData.db"
                    )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate database with dummy users
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dummyUsers = listOf(
                                        Profile(
                                            firstName = "Alice",
                                            lastName = "Johnson",
                                            email = "alice@test.com",
                                            phone = "081234567890",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "Bob",
                                            lastName = "Smith",
                                            email = "bob@test.com",
                                            phone = "081234567891",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "Johny",
                                            lastName = "Doe",
                                            email = "test@test.com",
                                            phone = "081234567892",
                                            passwordHash = hashPassword("test123")
                                        )
                                    )
                                    dummyUsers.forEach { profile ->
                                        database.profileDao().insert(profile)
                                    }
                                }
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
                }
    }
}