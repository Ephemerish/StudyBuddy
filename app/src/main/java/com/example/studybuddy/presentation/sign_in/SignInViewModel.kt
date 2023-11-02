package com.example.studybuddy.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.database.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val studyBuddyRepository: StudyBuddyRepository): ViewModel() {

    val user = studyBuddyRepository.getUser()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update {it.copy(
            isSignInSuccesful = result.data != null,
            signInError = result.errorMessage
        ) }
    }
    fun resetState(){
        _state.update { SignInState() }
    }

    fun saveUser(user: UserData?) {
        viewModelScope.launch {
            val loggedUser = User(
                user?.userId ?: "null",
                user?.userEmail ?:"null",
                user?.userName ?:"null",
                "Bio",
                user?.profilePictureUrl,
                "Location",
                0L,
                0L
            )
            studyBuddyRepository.upsertUser(loggedUser)
        }
    }

    fun logoutUser(){
        viewModelScope.launch {
            studyBuddyRepository.deleteUser()
        }
    }
}