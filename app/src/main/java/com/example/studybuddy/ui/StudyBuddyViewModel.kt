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
package com.example.studybuddy.ui.theme

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.NavDrawerType
import com.example.studybuddy.ui.StudyBuddyUiState
import com.example.studybuddy.ui.screen.NotificationRequestFirebase
import com.example.studybuddy.ui.screen.NotificationStateType
import com.example.studybuddy.ui.screen.UserSubjectFirebase
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StudyBuddyViewModel : ViewModel() {
    val currentUserSubject = mutableStateOf<UserSubjectFirebase>(UserSubjectFirebase())

    fun updateCurrentUserSubject(userSubjectFirebase: UserSubjectFirebase) {
        currentUserSubject.value = userSubjectFirebase
    }

    private val _uiState = MutableStateFlow(StudyBuddyUiState())
    val uiState: StateFlow<StudyBuddyUiState> = _uiState

    init {
        initializeUIState()
    }

    private fun initializeUIState() {

    }

    fun updateCurrentNavDrawer(
        navDrawerType: NavDrawerType,
    ) {
        _uiState.update {
            it.copy(
                currentNavDrawer = navDrawerType
            )
        }
    }

    fun updateSelectItemIndex(index: Int) {
        _uiState.update {
            it.copy(
                selectItemIndex = index
            )
        }
    }

    val notificationRequestRequestList = mutableStateOf<List<NotificationRequestFirebase>>(emptyList())
    val notificationRequestAcceptList = mutableStateOf<List<NotificationRequestFirebase>>(emptyList())
    fun fetchNotificationRequestDataFromDatabase(userID: String) {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/notificationRequest")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notificationRequestRequest = mutableListOf<NotificationRequestFirebase>()
                val notificationRequestAccept = mutableListOf<NotificationRequestFirebase>()
                for (notificationRequestData in dataSnapshot.children) {
                    val notification = notificationRequestData.getValue(NotificationRequestFirebase::class.java)
                    notification?.let {
                        if(it.receiverUserId == userID){
                            if(it.notificationType == NotificationStateType.REQUEST.toString()){
                                notificationRequestRequest.add(it)
                            }
                            if(it.notificationType == NotificationStateType.ACCEPT.toString()){
                                notificationRequestAccept.add(it)
                            }
                        }
                    }
                }
                notificationRequestRequestList.value = notificationRequestRequest
                notificationRequestAcceptList.value = notificationRequestAccept
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Logger.TAG, "Failed to read userSubject value.", error.toException())
            }
        })
    }
    fun deleteNotificationRequestFromDatabase(subjectName: String?, currentUser: String, context: Context) {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/notificationRequest/${currentUser} - $subjectName")
        // Use the removeValue method to delete the child node
        myRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                userSubjectList.value = userSubjectList.value.filter { it.subjectName != subjectName }
            } else {
                val exception = task.exception
                if (exception != null) {

                }
            }
        }
    }
}
