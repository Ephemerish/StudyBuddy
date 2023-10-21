package com.example.studybuddy.data.database

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["sessionId", "userId"],
    indices = [Index(value = ["userId"])]
)
data class SessionParticipants(
    val sessionId: Int,
    val userId: Int
)
