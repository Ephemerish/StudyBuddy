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
package com.example.studybuddy.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studybuddy.data.NavDrawerType
import com.example.studybuddy.ui.StudyBuddyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StudyBuddyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StudyBuddyUiState())
    val uiState: StateFlow<StudyBuddyUiState> = _uiState

    init {
        initializeUIState()
    }

    private fun initializeUIState() {

    }

    fun updateCurrentNavDrawer(
        navDrawerType: NavDrawerType,
    ) {
        _uiState.update {
            it.copy(
                currentNavDrawer = navDrawerType
            )
        }
    }

    fun updateSelectItemIndex(index: Int) {
        _uiState.update {
            it.copy(
                selectItemIndex = index
            )
        }
    }
}
