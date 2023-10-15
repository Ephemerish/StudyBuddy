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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studybuddy.ui.StudyBuddyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CourseItemUiState(
    val currentEnable: String = ""
)

data class HomeUiState(
    val searchCourseText: String = "",
    val isEntryValid: Boolean = false
)

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(): ViewModel() {
    var _itemUiState by mutableStateOf(CourseItemUiState())
        private set
    var _homeUiState by mutableStateOf(HomeUiState())
        private set

    fun updateUiState(homeUiState: HomeUiState) {
        _homeUiState =
            HomeUiState(searchCourseText = homeUiState.searchCourseText, isEntryValid = validateInput(homeUiState))
    }
    private fun validateInput(uiState: HomeUiState): Boolean {
        return with(uiState) {
            searchCourseText.isNotBlank()
        }
    }
}

/**
 * Ui State for HomeScreen
 */
// data class HomeUiState(val itemList: List<Item> = listOf())
