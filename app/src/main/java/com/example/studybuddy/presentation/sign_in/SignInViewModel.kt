package com.example.studybuddy.presentation.sign_in

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.database.User
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val studyBuddyRepository: StudyBuddyRepository): ViewModel() {

    val user = studyBuddyRepository.getUser()

    var isEditing = mutableStateOf(false)
    var newBio = mutableStateOf("")

    val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val myRef = database.getReference("StudyBuddy")

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

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

    fun updateProfileBioFirebase(userData: UserProfileFirebase) {
        val newBio by newBio
        val userRequestReference = myRef.child(
            "profile" +
                    "/${userData.userName}"
        )
        val userProfileData = mapOf(
            "userId" to userData.userId,
            "userEmail" to userData.userEmail,
            "userName" to userData.userName,
            "userProfilePic" to userData.userProfilePic,
            "userVerificationState" to userData.userVerificationState,
            "userBio" to newBio,
            "userMunicipality" to userData.userMunicipality,
            "userYearLevel" to userData.userYearLevel,
            "userCourse" to userData.userCourse,
            "userDateOfBirth" to userData.userDateOfBirth,
            "userSchoolId" to userData.userSchoolId
        )
        userRequestReference.setValue(userProfileData)
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {it.copy(
            isSignInSuccesful = result.data != null,
            signInError = result.errorMessage
        ) }
    }
    fun resetState(){
        _state.update { SignInState() }
    }

    fun saveUserToLocalDatabase(user: UserData?) {
        fetchProfileDataFromDatabase(user?.userId ?: "null")
        viewModelScope.launch {
            val userProfile by userProfileList

            // Introduce a delay before processing
            delay(5000) // Adjust the delay time as needed

            val loggedUser = User(
                user?.userId ?: "null",
                user?.userEmail ?: "null",
                user?.userName ?: "null",
                "Bio",
                user?.profilePictureUrl,
                "Location",
                0L,
                0L
            )
            studyBuddyRepository.upsertUser(loggedUser)

            // Check if userProfile is empty after the delay
            if (userProfile.isEmpty()) {
                val userRequestReference = myRef.child(
                    "profile" +
                            "/${user?.userName}"
                )
                val userProfileData = mapOf(
                    "userId" to user?.userId,
                    "userEmail" to user?.userEmail,
                    "userName" to user?.userName,
                    "userProfilePic" to user?.profilePictureUrl,
                    "userVerificationState" to "NOT APPLIED",
                    "userBio" to "",
                    "userMunicipality" to "NULL",
                    "userYearLevel" to "NULL",
                    "userCourse" to "Computer Engineering",
                    "userDateOfBirth" to "NULL",
                    "userSchoolId" to "NULL"
                )
                userRequestReference.setValue(userProfileData)
            }
        }
    }

    fun logoutUser(){
        viewModelScope.launch {
            studyBuddyRepository.deleteUser()
        }
    }
}

data class UserProfileFirebase(
    val userId: String? = null,
    val userSchoolId: String? = null,
    val userEmail: String? = null,
    val userName: String? = null,
    val userProfilePic: String? = null,
    val userDateOfBirth: String? = null,
    val userVerificationState: String? = null,
    val userMunicipality: String? = null,
    val userBio: String? = null,
    val userYearLevel: String? = null,
    val userCourse: String? = null,
)

enum class VerificationStateType {
    APPLIED,
    VERIFIED,
    FAILED
}
