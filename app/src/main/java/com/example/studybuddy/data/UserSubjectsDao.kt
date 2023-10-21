package com.example.studybuddy.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UserSubjectsDao {
    @Insert
    suspend fun insertUserSubjects(userSubjects: UserSubjects)

    // Add other user-subjects related queries here
}
