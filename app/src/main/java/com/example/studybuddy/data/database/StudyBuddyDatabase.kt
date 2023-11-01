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

package com.example.studybuddy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

///**
// * Database class with a singleton Instance object.
// */
//@Database(entities = [Item::class], version = 1, exportSchema = false)
//abstract class StudyBuddyDatabase : RoomDatabase() {
//
//    abstract fun itemDao(): ItemDao
//
//    companion object {
//        @Volatile
//        private var Instance: StudyBuddyDatabase? = null
//
//        fun getDatabase(context: Context): StudyBuddyDatabase {
//            // if the Instance is not null, return it, otherwise create a new database instance.
//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(context, StudyBuddyDatabase::class.java, "studyBuddy_database")
//                    /**
//                     * Setting this option in your app's database builder means that Room
//                     * permanently deletes all data from the tables in your database when it
//                     * attempts to perform a migration with no defined migration path.
//                     */
//                    .fallbackToDestructiveMigration()
//                    .build()
//                    .also { Instance = it }
//            }
//        }
//    }
//}

@Database(
    entities = [User::class, Subject::class, UserSubjects::class, StudySession::class, SessionParticipants::class, Review::class],
    version = 5
)
abstract class StudyBuddyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun subjectDao(): SubjectDao
    abstract fun userSubjectsDao(): UserSubjectsDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun sessionParticipantsDao(): SessionParticipantsDao
    abstract fun reviewsDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: StudyBuddyDatabase? = null

        fun getDatabase(context: Context): StudyBuddyDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StudyBuddyDatabase::class.java,
                    "study_buddy_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}
