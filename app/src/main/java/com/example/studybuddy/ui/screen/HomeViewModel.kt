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

package com.example.studybuddy.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.database.SubjectDao
import com.example.studybuddy.presentation.sign_in.UserProfileFirebase
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.crashlytics.internal.Logger.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(private val studyBuddyRepository: StudyBuddyRepository): ViewModel() {
    val homeSearchBarState = mutableStateOf(HomeSearchBarState())
    val subjectList = mutableStateOf<List<SubjectFirebase>>(emptyList())

    //    fun updateUiState(newValue: String) {
//        homeSearchBarState = HomeSearchBarState(newValue)
//    }
    // Add a function to fetch data
    fun fetchDataFromDatabase() {
        val database =
            Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/subjects")
        val storage = Firebase.storage("gs://study-buddy-79089.appspot.com/")
        val storageRef = storage.getReference("StudyBuddy")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val subjects = mutableListOf<SubjectFirebase>()
                for (subjectData in snapshot.children) {
                    val subject = subjectData.getValue(SubjectFirebase::class.java)
                    subject?.let {
                        subjects.add(it)
                        val subjectImgReference =
                            storageRef.child("subjects/${it.subjectName}/image.jpg")
                        subjectImgReference.downloadUrl
                            .addOnSuccessListener { uri ->
                                val downloadUrl = uri.toString()
                                // Now, downloadUrl contains the URL of your file
                                val SubjectReference = myRef.child("${it.subjectName}")
                                val subjectData = mapOf(
                                    "subjectName" to it.subjectName,
                                    "imgLink" to downloadUrl
                                )
                                SubjectReference.setValue(subjectData)
                            }
                    }
                }
                // Update subjectList.value when new data is available
                subjectList.value = subjects
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read subjects value.", error.toException())
            }
        })
    }

    val homeUiState: StateFlow<SubjectUiState> =
        studyBuddyRepository.getTopRatedSubjects().map {SubjectUiState(it)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SubjectUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val userProfileList = mutableStateOf<List<UserProfileFirebase>>(emptyList())
    fun fetchProfileDataFromDatabase(userID: String) {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/profile")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userProfile = mutableListOf<UserProfileFirebase>()
                for (userProfileData in dataSnapshot.children) {
                    val profile = userProfileData.getValue(UserProfileFirebase::class.java)
                    profile?.let {
                        if(it.userId == userID){
                            userProfile.add(it)
                        }
                    }
                }
                userProfileList.value = userProfile
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Logger.TAG, "Failed to read userSubject value.", error.toException())
            }
        })
    }

    fun toastMessage(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }



}

/**
 * Ui State for HomeScreen
 */
data class SubjectUiState(val itemList: List<SubjectDao.SubjectWithAverageRating> = listOf())
data class SubjectFirebase(
    val subjectName: String? = null,
    val imgLink: String? = null
)

data class HomeSearchBarState(
    val subjectFilter: String = ""
)
data class NotificationRequestFirebase(
    val senderUserId: String? = null,
    val receiverUserId: String? = null,
    val senderUserName: String? = null,
    val selectedSubjectName: String? = null,
    val notificationType: String? = null
)