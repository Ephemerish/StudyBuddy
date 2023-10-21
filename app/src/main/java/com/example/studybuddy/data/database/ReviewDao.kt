package com.example.studybuddy.data.database

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface ReviewDao {
    @Upsert
    suspend fun upsertReview(review: Review)

    // Add other review-related queries here
}