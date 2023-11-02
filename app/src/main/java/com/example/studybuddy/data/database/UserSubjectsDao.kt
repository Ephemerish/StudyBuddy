package com.example.studybuddy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSubjectsDao {
    @Upsert
    suspend fun upsertUserSubjects(userSubjects: UserSubjects)

    @Query("SELECT * FROM UserSubjects")
    fun getAllUserSubjects(): Flow<List<UserSubjects>>

    @Query("SELECT * FROM UserSubjects WHERE userId = :userId")
    fun getUserSubjectsByUserId(userId: String): Flow<List<UserSubjects>>

    @Query("SELECT * FROM UserSubjects WHERE subjectId = :subjectId")
    fun getUserSubjectsBySubjectId(subjectId: Int): Flow<List<UserSubjects>>

    @Delete
    suspend fun deleteUserSubject(userSubjects: UserSubjects)
}
