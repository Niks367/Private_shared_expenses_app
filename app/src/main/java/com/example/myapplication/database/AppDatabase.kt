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
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.dao.WalletTransactionDao
import com.example.myapplication.entities.Expense
import com.example.myapplication.entities.Group
import com.example.myapplication.entities.GroupMember
import com.example.myapplication.entities.Profile
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.entities.WalletTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Database(entities = [Profile::class, Group::class, GroupMember::class, Expense::class, WalletTransaction::class,TransactionEntity::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun groupDao(): GroupDao
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun walletTransactionDao(): WalletTransactionDao
    abstract fun transactionDao(): TransactionDao

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
                            populateDummyUsers()
                        }
                        
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // Populate dummy users every time if they don't exist
                            populateDummyUsers()
                        }
                        
                        private fun populateDummyUsers() {
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    // Check if users already exist
                                    val existingUser = database.profileDao().findByEmail("alice@test.com")
                                    if (existingUser != null) {
                                        // Users already exist, skip
                                        return@launch
                                    }
                                    
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
                                        ),
                                        Profile(
                                            firstName = "Charlie",
                                            lastName = "Brown",
                                            email = "charlie@test.com",
                                            phone = "081234567893",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "Diana",
                                            lastName = "Prince",
                                            email = "diana@test.com",
                                            phone = "081234567894",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "Edward",
                                            lastName = "Norton",
                                            email = "edward@test.com",
                                            phone = "081234567895",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "Fiona",
                                            lastName = "Garcia",
                                            email = "fiona@test.com",
                                            phone = "081234567896",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "George",
                                            lastName = "Martin",
                                            email = "george@test.com",
                                            phone = "081234567897",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "Hannah",
                                            lastName = "Lee",
                                            email = "hannah@test.com",
                                            phone = "081234567898",
                                            passwordHash = hashPassword("password123")
                                        ),
                                        Profile(
                                            firstName = "Ivan",
                                            lastName = "Rodriguez",
                                            email = "ivan@test.com",
                                            phone = "081234567899",
                                            passwordHash = hashPassword("password123")
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