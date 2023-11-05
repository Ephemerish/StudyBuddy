package com.example.studybuddy.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
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

class RequestViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
): ViewModel() {
    val userRequestList = mutableStateOf<List<UserRequestData>>(emptyList())

    // Add a function to fetch data
    suspend fun fetchDataFromDatabase(context: Context) {
        val currentUser = getCurrentUser()
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/userRequests/${currentUser?.userId}")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userRequests = mutableListOf<UserRequestData>()
                for (requestData in dataSnapshot.children) {
                    val request = requestData.getValue(UserRequestData::class.java)
                    request?.let {
                        userRequests.add(it)
                    }
                }
                userRequestList.value = userRequests
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Logger.TAG, "Failed to read userRequest value.", error.toException())
            }
        })
    }


    // Function to update a request
    suspend fun updateRequest(
        updatedData: UserRequestData,
        newRequestState: RequestStateType,
        context: Context
    ) {
        val currentUser = getCurrentUser()
        val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("StudyBuddy/userRequests" +
                "/${currentUser?.userId}/${updatedData.senderUserId}-${updatedData.selectedSubjectName}")

        // Check if the request exists
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Request exists, update the data
                    val data = mapOf(
                        "requestState" to newRequestState.name // Store the new requestState as a
                    // string
                    )
                    myRef.updateChildren(data).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Request updated successfully
                            // You can update your local userRequestList if needed
                            Toast.makeText(context, "Request updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle the update error
                            val exception = task.exception
                            if (exception != null) {
                                // Handle the exception
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(Logger.TAG, "Failed to read userRequest value.", error.toException())
            }
        })
    }

    private suspend fun getCurrentUser(): User? {
        return studyBuddyRepository.getUser().firstOrNull()
    }
}


data class UserRequestData(
    val senderUserId: String? = null,
    val receiverUserId: String? = null,
    val senderUserName: String? = null,
    val senderUserProfilePic: String? = null,
    val senderUserEmail: String? = null,
    val selectedSubjectName: String? = null,
    val requestState: RequestStateType = RequestStateType.PENDING
)
