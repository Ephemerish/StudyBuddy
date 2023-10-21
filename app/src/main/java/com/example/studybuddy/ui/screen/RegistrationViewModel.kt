package com.example.studybuddy.ui.screen

import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository

class RegistrationViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
):ViewModel(){
    suspend fun insertSampleSubjects(){
        studyBuddyRepository.insertSampleSubject()
    }

    suspend fun clearSampleSubjects(){
        studyBuddyRepository.clearAllSubjects()
    }
}