package com.example.studybuddy.presentation.sign_in

data class SignInResult(
    val data: com.example.studybuddy.presentation.sign_in.UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val userEmail: String?,
    val userName: String?,
    val profilePictureUrl: String?
)
