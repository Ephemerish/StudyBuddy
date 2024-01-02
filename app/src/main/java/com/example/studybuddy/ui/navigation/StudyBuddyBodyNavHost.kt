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

package com.example.studybuddy.ui.navigation

import MyClassScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studybuddy.data.NavDrawerType
import com.example.studybuddy.presentation.sign_in.GoogleAuthUiClient
import com.example.studybuddy.presentation.sign_in.UserData
import com.example.studybuddy.ui.StudyBuddyUiState
import com.example.studybuddy.ui.screen.AboutUsDestination
import com.example.studybuddy.ui.screen.AboutUsScreen
import com.example.studybuddy.ui.screen.ContactUsDestination
import com.example.studybuddy.ui.screen.ContactUsScreen
import com.example.studybuddy.ui.screen.HomeDestination
import com.example.studybuddy.ui.screen.HomeScreen
import com.example.studybuddy.ui.screen.RequestDestination
import com.example.studybuddy.ui.screen.RequestScreen
import com.example.studybuddy.ui.screen.RegistrationDestination
import com.example.studybuddy.ui.screen.RegistrationScreen
import com.example.studybuddy.ui.screen.TutorDetailDestination
import com.example.studybuddy.ui.screen.TutorDetailScreen
import com.example.studybuddy.ui.screen.TutorListDestination
import com.example.studybuddy.ui.screen.TutorListScreen
import com.example.studybuddy.ui.theme.StudyBuddyViewModel
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope


/**
 * Provides Navigation graph for the application.
 */
@Composable
fun StudyBuddyBodyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,

    viewModel: StudyBuddyViewModel,
    navDrawerUiState: StudyBuddyUiState,
    scope: CoroutineScope,
    innerPaddingValues: PaddingValues,
    googleAuthUiClient: GoogleAuthUiClient,
    firebase: DatabaseReference,
    userData: UserData?
) {

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                innerPaddingValues = innerPaddingValues,
                navController = navController,
                userData = userData
            )
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.Home,
            )
        }
        composable(route = RequestDestination.route) {
            RequestScreen(
                appViewModel = viewModel
            )
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.Request,
            )
        }
        composable(route = MyClassDestination.route) {
            MyClassScreen()
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.MyClass,
            )
        }
        composable(route = ContactUsDestination.route) {
            ContactUsScreen()
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.ContactUs,
            )
        }
        composable(route = AboutUsDestination.route) {
            AboutUsScreen()
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.AboutUs,
            )
        }
        composable(route = RegistrationDestination.route) {
            RegistrationScreen(
                firebase = firebase,
                navController = navController,
                googleAuthUiClient = googleAuthUiClient
            )
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.Home,
            )
        }
        composable(
            route = TutorListDestination.route+"/{myParam}",
            arguments = listOf(
                navArgument("myParam"){
                    type = NavType.StringType
                }
            )
        ) {
            val selectedSubject = it.arguments?.getString("myParam")?: ""
            TutorListScreen(
                navController = navController,
                selectedSubject = selectedSubject,
                appViewModel = viewModel
                )
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.Home,
            )
        }
        composable(
            route = TutorDetailDestination.route+"/{myParam}",
            arguments = listOf(
                navArgument("myParam"){
                    type = NavType.StringType
                }
            )
        ) {
            val selectedSubject = it.arguments?.getString("myParam")?: ""
            TutorDetailScreen(
                navController = navController,
                selectedSubject = selectedSubject,
                appViewModel = viewModel,
            )
            viewModel.updateCurrentNavDrawer(
                navDrawerType = NavDrawerType.Home,
            )
        }
    }
}