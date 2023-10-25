package com.example.studybuddy.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.database.StudyBuddyRepository

class RegistrationViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
):ViewModel(){
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

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }

    suspend fun insertSampleSubjects(){
        studyBuddyRepository.upsertSampleSubject()
    }

    suspend fun clearSampleSubjects(){
        studyBuddyRepository.clearAllSubjects()
    }
}
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
)