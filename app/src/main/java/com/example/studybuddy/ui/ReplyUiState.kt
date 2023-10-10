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

import com.example.studybuddy.NavDrawerType

data class StudyBuddyUiState(
   // val mailboxes: Map<NavDrawerType, List<Email>> = emptyMap(),
    val currentNavDrawer: NavDrawerType = NavDrawerType.Home,
   // val currentSelectedEmail: Email = LocalEmailsDataProvider.defaultEmail,
    val isShowingHomepage: Boolean = true
) {
    // val currentMailboxEmails: List<Email> by lazy { mailboxes[currentMailbox]!! }
}