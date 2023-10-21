/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.studybuddy.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.StudyBuddyRepository
import com.example.studybuddy.data.SubjectDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(private val studyBuddyRepository: StudyBuddyRepository): ViewModel() {

    val homeUiState: StateFlow<SubjectUiState> =
        studyBuddyRepository.getTopRatedSubjects().map {SubjectUiState(it)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SubjectUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

//    var _itemUiState by mutableStateOf(CourseItemUiState())
//        private set
//    var _homeUiState by mutableStateOf(HomeUiState())
//        private set
//
//    fun updateUiState(homeUiState: HomeUiState) {
//        _homeUiState =
//            HomeUiState(searchCourseText = homeUiState.searchCourseText, isEntryValid = validateInput(homeUiState))
//    }
//    private fun validateInput(uiState: HomeUiState): Boolean {
//        return with(uiState) {
//            searchCourseText.isNotBlank()
//        }
//    }
}

/**
 * Ui State for HomeScreen
 */
data class SubjectUiState(val itemList: List<SubjectDao.SubjectWithAverageRating> = listOf())
