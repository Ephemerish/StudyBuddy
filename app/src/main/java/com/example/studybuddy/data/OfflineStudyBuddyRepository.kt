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

import kotlinx.coroutines.flow.Flow

class OfflineStudyBuddyRepository(
    private val userDao: UserDao,
    private val subjectDao: SubjectDao,
    private val reviewDao: ReviewDao
): StudyBuddyRepository {
    // User-related operations
    override suspend fun insertUser(user: User) = userDao.upsertUser(user)

    override fun getUser(userId: Int): Flow<User?>  = userDao.getUser(userId)

    // Subject-related operations
    override suspend fun insertSubject(subject: Subject) = subjectDao.upsertSubject(subject)

    override fun getSubject(subjectId: Int): Flow<Subject?> = subjectDao.getSubject(subjectId)

    // Top-rated subjects
    override fun getTopRatedSubjects(): Flow<List<SubjectDao.SubjectWithAverageRating>>  = subjectDao.getTopRatedSubjects()

    override suspend fun insertSampleSubject() {
        val user1 = User(1, "user1", "user1@example.com", "password1", "User 1", "Bio 1", null, "Location 1", 0L, 0L)
        val user2 = User(2, "user2", "user2@example.com", "password2", "User 2", "Bio 2", null, "Location 2", 0L, 0L)

        userDao.upsertUser(user1)
        userDao.upsertUser(user2)

        val subject1 = Subject(1, "Math")
        val subject2 = Subject(2, "Science")
        val subject3 = Subject(3, "History")
        val subject4 = Subject(4, "English")
        val subject5 = Subject(5, "Computer Science")
        val subject6 = Subject(6, "Physics")
        val subject7 = Subject(7, "Biology")
        val subject8 = Subject(8, "Chemistry")
        val subject9 = Subject(9, "Geography")
        val subject10 = Subject(10, "Literature")

        subjectDao.upsertSubject(subject1)
        subjectDao.upsertSubject(subject2)
        subjectDao.upsertSubject(subject3)
        subjectDao.upsertSubject(subject4)
        subjectDao.upsertSubject(subject5)
        subjectDao.upsertSubject(subject6)
        subjectDao.upsertSubject(subject7)
        subjectDao.upsertSubject(subject8)
        subjectDao.upsertSubject(subject9)
        subjectDao.upsertSubject(subject10)

        val review1 = Review(1, 2, 1,1, "Great subject!", 0L)
        val review2 = Review(2, 1, 2,2, "Good subject.", 0L)
        val review3 = Review(3, 3, 3,3, "I enjoyed it.", 0L)
        val review4 = Review(4, 2, 4,4, "Not bad.", 0L)
        val review5 = Review(5, 4, 4,1, "Great subject!", 0L)
        val review6 = Review(6, 5, 6,2, "Good subject.", 0L)
        val review7 = Review(7, 2, 4,3, "I enjoyed it.", 0L)
        val review8 = Review(8, 1, 2,4, "Not bad.", 0L)
        val review9 = Review(9, 5, 1,1, "Great subject!", 0L)
        val review10 = Review(10, 7, 9,2, "Good subject.", 0L)
        val review11 = Review(11, 8, 1,3, "I enjoyed it.", 0L)
        val review12 = Review(12, 1, 9,4, "Not bad.", 0L)

        reviewDao.upsertReview(review1)
        reviewDao.upsertReview(review2)
        reviewDao.upsertReview(review3)
        reviewDao.upsertReview(review4)
        reviewDao.upsertReview(review5)
        reviewDao.upsertReview(review6)
        reviewDao.upsertReview(review7)
        reviewDao.upsertReview(review8)
        reviewDao.upsertReview(review9)
        reviewDao.upsertReview(review10)
        reviewDao.upsertReview(review11)
        reviewDao.upsertReview(review12)
        // Add more sample data as needed

        // Insert sample reviews and sessions if required for your testing
    }

    override suspend fun clearAllSubjects() = subjectDao.clearAllSubjects()
}

