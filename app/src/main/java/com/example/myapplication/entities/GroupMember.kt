package com.example.myapplication.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "group_members",
    primaryKeys = ["groupId", "profileId"],
    foreignKeys = [
        ForeignKey(entity = Group::class, parentColumns = ["id"], childColumns = ["groupId"]),
        ForeignKey(entity = Profile::class, parentColumns = ["id"], childColumns = ["profileId"])
    ]
)
data class GroupMember(
    val groupId: Long,
    val profileId: Long,
    val status: String // e.g., "invited", "joined"
)