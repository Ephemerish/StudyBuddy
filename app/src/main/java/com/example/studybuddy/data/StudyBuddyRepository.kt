/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.studybuddy.data

import com.example.studybuddy.data.database.Subject
import com.example.studybuddy.data.database.SubjectDao
import com.example.studybuddy.data.database.User
import com.example.studybuddy.data.database.UserSubjects
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface StudyBuddyRepository {
    // User-related operations

    suspend fun upsertUser(user: User)

    fun getUser(): Flow<User?>

    suspend fun deleteUser()

    // Subject-related operations

    suspend fun upsertSubject(subject: Subject)

    fun getSubject(subjectId: Int): Flow<Subject?>

    fun getSubjectWithName(subjectName: String):Flow<Subject?>

    // userSubject
    suspend fun upsertUserSubject(userSubjects: UserSubjects)

    fun getAllUserSubjects(): Flow<List<UserSubjects>>

    fun getUserSubjectsByUserId(userId: String): Flow<List<UserSubjects>>

    fun getUserSubjectsBySubjectId(subjectId: Int): Flow<List<UserSubjects>>

    suspend fun deleteUserSubject(userSubjects: UserSubjects)

    // Top-rated subjects

    fun getTopRatedSubjects(): Flow<List<SubjectDao.SubjectWithAverageRating>>

    suspend fun upsertSampleSubject()

    suspend fun clearAllSubjects()
}
