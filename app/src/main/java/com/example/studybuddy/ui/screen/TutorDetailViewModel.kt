package com.example.studybuddy.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.database.User
import com.example.studybuddy.presentation.sign_in.UserProfileFirebase
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.firstOrNull

enum class RequestStateType {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELED
}
enum class NotificationStateType {
    REQUEST,
    ACCEPT
}


class TutorDetailViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
): ViewModel() {
    var currentUser = mutableStateOf("id not queried")
    val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val myRef = database.getReference("StudyBuddy")

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

    suspend fun upsertUserRequest(receiverUserSubject: UserSubjectFirebase) {
        val senderUser = getCurrentUser()
        val userRequestReference = myRef.child("userRequests" +
                "/${receiverUserSubject.userId}/${senderUser?.userId}-${receiverUserSubject.subjectName}")
        val userRequestData = mapOf(
            "senderUserId" to senderUser?.userId,
            "receiverUserId" to receiverUserSubject.userId,
            "senderUserName" to senderUser?.fullName,
            "senderUserProfilePic" to senderUser?.profilePicture,
            "senderUserEmail" to senderUser?.email,
            "selectedSubjectName" to receiverUserSubject.subjectName,
            "requestState" to RequestStateType.PENDING
        )
        userRequestReference.setValue(userRequestData)

        val notificationRequestReference = myRef.child("notificationRequest/${receiverUserSubject
            .userId} - ${receiverUserSubject.subjectName}")
        val notificationRequestData = mapOf(
            "senderUserId" to senderUser?.userId,
            "receiverUserId" to receiverUserSubject.userId,
            "senderUserName" to senderUser?.fullName,
            "selectedSubjectName" to receiverUserSubject.subjectName,
            "notificationType" to NotificationStateType.REQUEST,
        )
        notificationRequestReference.setValue(notificationRequestData)
    }
    suspend fun getCurrentUser(): User? {
        return studyBuddyRepository.getUser().firstOrNull()
    }
}
