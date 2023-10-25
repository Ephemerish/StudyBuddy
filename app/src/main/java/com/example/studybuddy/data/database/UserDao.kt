package com.example.studybuddy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM User WHERE userId > :userId")
    fun getUser(userId: Int): Flow<User?>

    @Query("DELETE FROM User")
    suspend fun deleteUser()

    // Add other user-related queries here
}
