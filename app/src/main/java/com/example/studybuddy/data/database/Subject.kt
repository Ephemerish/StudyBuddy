package com.example.studybuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int,
    val name: String
)

