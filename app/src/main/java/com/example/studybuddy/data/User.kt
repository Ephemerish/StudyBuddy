package com.example.studybuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val bio: String?,
    val profilePicture: String?,
    val location: String?,
    val createdAt: Long,
    val lastLogin: Long?
)

