package com.example.studybuddy.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.StudyBuddyRepository
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.internal.Logger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class TutorDetailViewModel(
    private val studyBuddyRepository: StudyBuddyRepository
): ViewModel() {


}