package com.example.studybuddy.data

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["userId", "subjectId"],
    indices = [Index(value = ["subjectId"])]
)
data class UserSubjects(
    val userId: Int,
    val subjectId: Int
)

