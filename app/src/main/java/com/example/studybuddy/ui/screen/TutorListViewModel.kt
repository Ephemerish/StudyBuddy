package com.example.studybuddy.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.presentation.sign_in.UserProfileFirebase
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
    val userProfileList = mutableStateOf<List<UserProfileFirebase>>(emptyList())

    val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")

    suspend fun beTutorToSubject(selectedSubject: String, profileList: List<UserProfileFirebase>) {
        val myRef = database.getReference("StudyBuddy")
        val userSubjectReference = myRef.child("userSubjects/${selectedSubject}/${profileList[0].userId}")
        val userSubjectData = mapOf(
            "userId" to profileList[0].userId,
            "userName" to profileList[0].userName,
            "userProfilePic" to profileList[0].userProfilePic,
            "userEmail" to profileList[0].userEmail,
            "subjectName" to selectedSubject
        )
        userSubjectReference.setValue(userSubjectData)
    }

    fun fetchProfileDataFromDatabase() {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/profile")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userProfile = mutableListOf<UserProfileFirebase>()
                for (userProfileData in dataSnapshot.children) {
                    val profile = userProfileData.getValue(UserProfileFirebase::class.java)
                    profile?.let {
                        userProfile.add(it)
                    }
                }
                userProfileList.value = userProfile
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Logger.TAG, "Failed to read userSubject value.", error.toException())
            }
        })
    }

    // Add a function to fetch data
    fun fetchSubjectDataFromDatabase(selectedSubject: String) {
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