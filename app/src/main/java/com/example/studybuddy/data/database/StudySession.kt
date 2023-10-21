package com.example.studybuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudySession(
    @PrimaryKey(autoGenerate = true) val sessionId: Int,
    val hostUserId: Int,
    val subjectId: Int,
    val dateTime: Long,
    val duration: Int,
    val location: String?,
    val maxParticipants: Int
)
