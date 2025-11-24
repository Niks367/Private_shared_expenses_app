package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.entities.GroupMember
import com.example.myapplication.entities.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(groupMember: GroupMember)

    @Query("""
        SELECT p.* FROM profile p
        INNER JOIN group_members gm ON p.id = gm.profileId
        WHERE gm.groupId = :groupId
    """)
    fun getGroupMembers(groupId: Long): Flow<List<Profile>>
    
    @Query("""
        SELECT p.* FROM profile p
        INNER JOIN group_members gm ON p.id = gm.profileId
        WHERE gm.groupId = :groupId
    """)
    suspend fun getGroupMembersSync(groupId: Long): List<Profile>
    
    @Query("""
        SELECT COUNT(*) FROM group_members
        WHERE groupId = :groupId
    """)
    suspend fun getGroupMemberCount(groupId: Long): Int
}