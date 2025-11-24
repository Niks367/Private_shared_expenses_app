package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.entities.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert
    suspend fun insert(group: Group): Long

    @Query("SELECT * FROM groups")
    fun getAllGroups(): Flow<List<Group>>

    @Query("SELECT * FROM groups WHERE id = :groupId")
    fun getGroupById(groupId: Long): Flow<Group>

    @Query("""
        SELECT g.* FROM groups g
        INNER JOIN group_members gm ON g.id = gm.groupId
        WHERE gm.profileId = :userId
    """)
    fun getAllGroupsForUser(userId: Long): Flow<List<Group>>
}
