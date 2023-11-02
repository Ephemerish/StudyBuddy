package com.example.studybuddy.data.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Upsert
    suspend fun upsertSubject(subject: Subject)

    @Query("SELECT * FROM Subject WHERE subjectId = :subjectId")
    fun getSubject(subjectId: Int):Flow<Subject?>

    @Query("SELECT * FROM Subject WHERE name = :subjectName")
    fun getSubjectWithName(subjectName: String):Flow<Subject?>

    @Query("SELECT s.*, AVG(r.rating) AS average_rating " +
            "FROM Subject s " +
            "LEFT JOIN Review r ON s.subjectId = r.toUserId " +
            "GROUP BY s.subjectId " +
            "ORDER BY average_rating DESC " +
            "LIMIT 9")
    fun getTopRatedSubjects(): Flow<List<SubjectWithAverageRating>>

    data class SubjectWithAverageRating(
        @Embedded val subject: Subject,
        @ColumnInfo(name = "average_rating") val averageRating: Double
    )

    @Query("DELETE FROM Subject")
    suspend fun clearAllSubjects()
}
