package com.example.studybuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Review(
    @PrimaryKey(autoGenerate = true) val reviewId: Int,
    val fromUserId: String,
    val toUserId: String,
    val rating: Int,
    val comment: String?,
    val createdAt: Long
)
