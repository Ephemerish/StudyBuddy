package com.example.studybuddy.data

import androidx.room.Dao
import androidx.room.Insert
import com.example.studybuddy.data.SessionParticipants

@Dao
interface SessionParticipantsDao {
    @Insert
    suspend fun insertSessionParticipants(sessionParticipants: SessionParticipants)

    // Add other session participants related queries here
}