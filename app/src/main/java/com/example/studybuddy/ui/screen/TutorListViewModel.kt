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

class TutorListViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
): ViewModel(){
    val UserSubjectList = mutableStateOf<List<UserSubjectFirebase>>(emptyList())

    // Add a function to fetch data
    fun fetchDataFromDatabase(selectedSubject: String) {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/userSubjects/${selectedSubject}")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userSubjects = mutableListOf<UserSubjectFirebase>()
                for (subjectData in dataSnapshot.children) {
                    val subject = subjectData.getValue(UserSubjectFirebase::class.java)
                    subject?.let {
                        userSubjects.add(it)
                    }
                }
                UserSubjectList.value = userSubjects
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Logger.TAG, "Failed to read userSubject value.", error.toException())
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