package com.example.studybuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val userId: String,
    val email: String,
    val fullName: String?,
    val bio: String?,
    val profilePicture: String?,
    val location: String?,
    val createdAt: Long?,
    val lastLogin: Long?
)

