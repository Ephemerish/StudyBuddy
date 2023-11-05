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

package com.example.studybuddy.ui


import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.studybuddy.StudyBuddyApplication
import com.example.studybuddy.presentation.sign_in.SignInViewModel
import com.example.studybuddy.ui.screen.HomeViewModel
import com.example.studybuddy.ui.screen.RequestViewModel
import com.example.studybuddy.ui.screen.MyClassViewModel
import com.example.studybuddy.ui.screen.RegistrationViewModel
import com.example.studybuddy.ui.screen.TutorDetailViewModel
import com.example.studybuddy.ui.screen.TutorListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(inventoryApplication().container.studyBuddyRepository)
        }
        // Initializer for RegistrationViewModel
        initializer {
            RegistrationViewModel(inventoryApplication().container.studyBuddyRepository)
        }
        initializer {
            SignInViewModel(inventoryApplication().container.studyBuddyRepository)
        }
        initializer {
            TutorListViewModel(inventoryApplication().container.studyBuddyRepository)
        }
        initializer {
            TutorDetailViewModel(inventoryApplication().container.studyBuddyRepository)
        }
        initializer {
            MyClassViewModel(inventoryApplication().container.studyBuddyRepository)
        }
        initializer {
            RequestViewModel(inventoryApplication().container.studyBuddyRepository)
        }
    }
}

fun CreationExtras.inventoryApplication(): StudyBuddyApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as StudyBuddyApplication)