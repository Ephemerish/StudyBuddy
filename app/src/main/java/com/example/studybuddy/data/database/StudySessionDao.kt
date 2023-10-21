package com.example.studybuddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
    @Insert
    suspend fun insertStudySession(session: StudySession)

    @Query("SELECT * FROM StudySession WHERE sessionId = :sessionId")
    fun getStudySession(sessionId: Int): Flow<StudySession?>

    // Add other study session-related queries here
}
