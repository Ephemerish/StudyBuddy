package com.example.studybuddy.presentation.sign_in

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.ui.screen.ItemDetails
import com.example.studybuddy.ui.screen.ItemUiState
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage

class VerificationCenterViewModel(private val studyBuddyRepository: StudyBuddyRepository): ViewModel()
{
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    val userProfileList = mutableStateOf<List<UserProfileFirebase>>(emptyList())

    val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val myRef = database.getReference("StudyBuddy")
    val storage = Firebase.storage("gs://study-buddy-79089.appspot.com/")
    val storageRef = storage.getReference("StudyBuddy")

    val user = studyBuddyRepository.getUser()
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

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }
    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            img.isNotBlank()
        }
    }

    suspend fun putIDFileFirebase(userProfile: UserProfileFirebase) {
        val subjectImgReference = storageRef.child("applications/${userProfile
            .userId}/${userProfile.userName}.jpg")
        subjectImgReference.putFile(itemUiState.itemDetails.uri ?: Uri.parse("null"))

        val userRequestReference = myRef.child(
            "profile" +
                    "/${userProfile.userName}"
        )
        val userProfileData = mapOf(
            "userId" to userProfile.userId,
            "userEmail" to userProfile.userEmail,
            "userName" to userProfile.userName,
            "userProfilePic" to userProfile.userProfilePic,
            "userVerificationState" to VerificationStateType.APPLIED,
            "userBio" to userProfile.userBio,
            "userMunicipality" to userProfile.userMunicipality,
            "userYearLevel" to userProfile.userYearLevel,
            "userCourse" to userProfile.userCourse,
            "userDateOfBirth" to userProfile.userDateOfBirth,
            "userSchoolId" to userProfile.userSchoolId
        )
        userRequestReference.setValue(userProfileData)
    }

    // Function to generate a Toast
    fun toastMessage(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}