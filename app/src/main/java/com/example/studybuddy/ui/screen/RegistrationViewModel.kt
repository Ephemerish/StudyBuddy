package com.example.studybuddy.ui.screen

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.database.Subject
import com.example.studybuddy.data.database.User
import com.example.studybuddy.data.database.UserSubjects
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale

class RegistrationViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
):ViewModel(){
    val subjectList = mutableStateOf<List<SubjectFirebase>>(emptyList())

    // Add a function to fetch data
    fun fetchDataFromDatabase() {
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

    val database = Firebase.database("https://study-buddy-79089-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val myRef = database.getReference("StudyBuddy")
    val storage = Firebase.storage("gs://study-buddy-79089.appspot.com/")
    val storageRef = storage.getReference("StudyBuddy")

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
//    suspend fun saveItem() {
//        if (validateInput()) {
//            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
//        }
//    }

    private suspend fun getUser(): User? {
        return studyBuddyRepository.getUser().firstOrNull()
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            subjectName.isNotBlank()
        }
    }

    suspend fun insertSampleSubjects(){
        studyBuddyRepository.upsertSampleSubject()
    }

    suspend fun clearSubjects(){
        studyBuddyRepository.clearAllSubjects()
    }

    suspend fun upsertUserSubject( ) {
        val subjectNameNotTitleCase = itemUiState.itemDetails.subjectName
        val subjectName = subjectNameNotTitleCase.split(" ")
            .joinToString(" ") { it.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT) }
        val user = getUser()
        val subjectFlow = studyBuddyRepository.getSubjectWithName(subjectName)

        val insertedSubject = subjectFlow.firstOrNull()

        if (insertedSubject == null) {
            // Subject doesn't exist, so create it
            val newSubject = Subject(
                subjectId = 0,
                name = subjectName,
                img = itemUiState.itemDetails.img
            )
            studyBuddyRepository.upsertSubject(newSubject)

            // Now, retrieve the subject you just inserted to get its subjectId
            val newInsertedSubject = studyBuddyRepository.getSubjectWithName(subjectName).firstOrNull()

            if (newInsertedSubject != null) {
                // Create the UserSubjects record using the subjectId of the newly created subject
                studyBuddyRepository.upsertUserSubject(
                    UserSubjects(
                        userId = user?.userId ?: "null",
                        subjectId = newInsertedSubject.subjectId
                    )
                )
            } else {
                // Handle the case where subject insertion failed
                // You might want to log an error or handle this situation appropriately
            }
        } else {
            // Subject already exists, so you can directly create the UserSubjects record
            studyBuddyRepository.upsertUserSubject(
                UserSubjects(
                    userId = user?.userId ?: "null",
                    subjectId = insertedSubject.subjectId
                )
            )
        }

        val subjectImgReference = storageRef.child("subjects/$subjectName/image.jpg")
        subjectImgReference.putFile(itemUiState.itemDetails.uri ?: Uri.parse("null"))
        val SubjectReference = myRef.child("subjects/$subjectName")
        val UserSubjectReference = myRef.child("userSubjects/${subjectName}/${user?.userId}")
        subjectImgReference.downloadUrl
            .addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                // Now, downloadUrl contains the URL of your file
                val subjectData = mapOf(
                    "subjectName" to subjectName,
                    "imgLink" to downloadUrl
                )
                SubjectReference.setValue(subjectData)
            }
            .addOnFailureListener { exception ->
                // Handle the error
                val subjectData = mapOf(
                    "subjectName" to subjectName,
                    "imgLink" to "null"
                )
                SubjectReference.setValue(subjectData)
            }

        val userSubjectData = mapOf(
            "userId" to user?.userId,
            "userName" to user?.fullName,
            "userProfilePic" to user?.profilePicture,
            "userEmail" to user?.email,
            "subjectName" to subjectName
        )
        UserSubjectReference.setValue(userSubjectData)
    }
}
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val subjectName: String = "",
    val img: String = "",
    val uri: Uri? = null
)