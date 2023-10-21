package com.example.studybuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Review(
    @PrimaryKey(autoGenerate = true) val reviewId: Int,
    val fromUserId: Int,
    val toUserId: Int,
    val rating: Int,
    val comment: String?,
    val createdAt: Long
)
