package com.example.studybuddy.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.database.User
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.firstOrNull

enum class RequestStateType {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELED
}


class TutorDetailViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
): ViewModel() {
    var currentUser = mutableStateOf("id not queried")
    val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val myRef = database.getReference("StudyBuddy")
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
    }
    suspend fun getCurrentUser(): User? {
        return studyBuddyRepository.getUser().firstOrNull()
    }
}