package com.example.studybuddy.data.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface SessionParticipantsDao {
    @Insert
    suspend fun insertSessionParticipants(sessionParticipants: SessionParticipants)

    // Add other session participants related queries here
}