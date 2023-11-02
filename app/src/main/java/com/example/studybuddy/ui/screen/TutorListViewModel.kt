package com.example.studybuddy.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage

class TutorListViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
): ViewModel(){
    val UserSubjectList = mutableStateOf<List<UserSubjectFirebase>>(emptyList())

    // Add a function to fetch data
    fun fetchDataFromDatabase() {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/userSubjects")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userSubjectData = dataSnapshot.value as Map<String, Any>?
                    if (userSubjectData != null) {
                        // Create a UserSubjectData object from the retrieved data
                        val userSubject = UserSubjectFirebase(
                            userId = userSubjectData["userId"] as String,
                            userName = userSubjectData["userName"] as String,
                            userProfilePic = userSubjectData["userProfilePic"] as String,
                            userEmail = userSubjectData["userEmail"] as String,
                            subjectName = userSubjectData["subjectName"] as String
                        )

                        // Update the UserSubjectList with the new data
                        UserSubjectList.value = UserSubjectList.value + listOf(userSubject)
                    }
                } else {
                    // Handle the case where the data doesn't exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here, if any
            }
        })

    }
}

data class UserSubjectFirebase(
    val userId: String? = null,
    val userName: String? = null,
    val userProfilePic: String? = null,
    val userEmail: String? = null,
    val subjectName: String? = null
)