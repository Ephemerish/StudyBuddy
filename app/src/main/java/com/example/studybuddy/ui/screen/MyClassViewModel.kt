package com.example.studybuddy.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.database.User
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.firstOrNull

class MyClassViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
):ViewModel(){
    val userSubjectList = mutableStateOf<List<UserSubjectFirebase>>(emptyList())
    val subjectList = mutableStateOf<List<SubjectFirebase>>(emptyList())
    // Add a function to fetch data
    fun fetchSubjectsFromDatabase() {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/subjects")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val subjects = mutableListOf<SubjectFirebase>()
                for (subjectData in snapshot.children) {
                    val subject = subjectData.getValue(SubjectFirebase::class.java)
                    subject?.let {
                        subjects.add(it)
                    }
                }
                subjectList.value = subjects
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Logger.TAG, "Failed to read subjects value.", error.toException())
            }
        })
    }
    // Add a function to fetch data
    fun fetchUserSubjectFromDatabase(currentUser: String?, subjectList: List<SubjectFirebase>) {
        userSubjectList.value = emptyList()
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        subjectList.forEach() { subject->
            val myRef = database.getReference("StudyBuddy/userSubjects/${subject.subjectName}")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userSubjects = mutableListOf<UserSubjectFirebase>()
                    for (subjectData in dataSnapshot.children) {
                        val subject = subjectData.getValue(UserSubjectFirebase::class.java)
                        subject?.let {
                            if (it.userId == currentUser) {
                                userSubjects.add(it)
                            }
                        }
                    }
                    userSubjectList.value += userSubjects
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(Logger.TAG, "Failed to read userSubject value.", error.toException())
                }
            })
        }
    }
    fun deleteUserSubjectFromDatabase(subjectName: String?, currentUser: String, context: Context) {
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/userSubjects/$subjectName/$currentUser")
        // Use the removeValue method to delete the child node
        myRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Child node deleted successfully", Toast.LENGTH_SHORT).show()
                userSubjectList.value = userSubjectList.value.filter { it.subjectName != subjectName }
            } else {
                val exception = task.exception
                if (exception != null) {
                    Toast.makeText(context, "Failed to delete child node: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    suspend fun getUser(): User? {
        return studyBuddyRepository.getUser().firstOrNull()
    }
}