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

    import android.annotation.SuppressLint
    import android.content.Context
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.sizeIn
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material3.Card
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TextFieldDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.TextStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.ImeAction
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.text.style.TextOverflow
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavHostController
    import coil.compose.AsyncImage
    import coil.request.ImageRequest
    import com.example.studybuddy.R
    import com.example.studybuddy.makeNotification
    import com.example.studybuddy.presentation.sign_in.UserData
    import com.example.studybuddy.presentation.sign_in.UserProfileFirebase
    import com.example.studybuddy.presentation.sign_in.VerificationStateType
    import com.example.studybuddy.ui.AppViewModelProvider
    import com.example.studybuddy.ui.navigation.NavigationDestination


    object HomeDestination : NavigationDestination {
        override val route = "home"
        override val title = "Home"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun HomeScreen(
        innerPaddingValues: PaddingValues,
        navController: NavHostController,
        viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
        userData: UserData?
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = Unit){
            viewModel.fetchProfileDataFromDatabase(userData?.userId ?: "null")
        }

        val userProfile by viewModel.userProfileList
        var homeSearchBarState by viewModel.homeSearchBarState
        // Fetch data when HomeScreen is created
        val subjectList by viewModel.subjectList
        viewModel.fetchDataFromDatabase()

        val homeUiState by viewModel.homeUiState.collectAsState()
        val coroutineScope = rememberCoroutineScope()
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingValues)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .weight(7f)
                    .fillMaxSize()
                    .padding(top = 5.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(R.drawable.sb_logo_with_title),
                        contentDescription = "LOGO",
                        contentScale = ContentScale.FillWidth,
                        alignment = Alignment.Center,
                        modifier = Modifier.sizeIn(maxWidth = 150.dp)
                    )
                    Text(
                        text = "I need the tutor for",
//                      text = subjectList.getOrNull(0)?.subjectName ?: "No Subject Available",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    TextField(
                        value = homeSearchBarState.subjectFilter,
                        onValueChange = {
                            homeSearchBarState = HomeSearchBarState(it)
                        },
                        label = {
                            Text(
                                text = "Search Course",
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
//                        textStyle = TextStyle(
//                            color = Color.Black,
//                            fontSize = 16.sp
//                        ),
                    )
                    if(subjectList.isNotEmpty()){
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(bottom = 20.dp)
                        ) {
                            val filteredSubjects = subjectList.filter { subject ->
                                subject.subjectName?.contains(homeSearchBarState.subjectFilter,
                                    ignoreCase = true) ?: false
                            }

                            val limitedSubjects = filteredSubjects.take(30)

                            items(limitedSubjects) { subject ->
                                CourseCard(
                                    viewModel = viewModel,
                                    userProfile = userProfile,
                                    context = context,
                                    featureCourse = subject,
                                    onClickAction = {
                                        navController.navigate(
                                            route = TutorListDestination.route + "/${subject.subjectName}",
                                        ) {
                                            navController.graph.startDestinationRoute?.let { route ->
                                                popUpTo(route) {
                                                    saveState = true
                                                }
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Spacer(modifier = Modifier.padding(30.dp))
                            Text(
                                text = "NO SUBJECTS\n" +
                                        "          OR\n" +
                                        "NO INTERNET",
                                modifier = Modifier.weight(1f),
                                style = TextStyle(
                                    fontSize = 50.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CourseCard(
        featureCourse: SubjectFirebase,
        onClickAction: () -> Unit,
        context: Context,
        userProfile: List<UserProfileFirebase>,
        viewModel: HomeViewModel
    ) {
        Card(
            onClick = if(userProfile.isNotEmpty()){
                if(userProfile[0].userVerificationState == VerificationStateType.VERIFIED
                        .toString()){
                    onClickAction} else {
                    {
                        viewModel.toastMessage(context, "You need to be verified first")
                    }
                }
            } else {
                {}
            },
            modifier = Modifier
                .size(width = 105.dp, height = 85.dp),
            enabled = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
//                Image(
//                    painter = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
//                    contentDescription = "Cource Photo",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(7f)
//                )
                AsyncImage(
                    model =  ImageRequest.Builder(context = context)
                        .data(featureCourse.imgLink)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.sb_logo_with_title),
                    contentDescription = "Course Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = featureCourse.subjectName ?: "Null",
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth()
                            .weight(2f),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        ),
                        overflow = TextOverflow.Ellipsis, // Use an ellipsis for overflowing text
                        maxLines = 1 // Restrict the text to a single line
                    )
//                    Text(
//                        text = featureCourse.averageRating.toString(),
//                        modifier = Modifier
//                            .padding(0.dp)
//                            .fillMaxWidth()
//                            .weight(2f),
//                        textAlign = TextAlign.Center,
//                        style = TextStyle(
//                            fontWeight = FontWeight.Bold
//                        ),
//                        overflow = TextOverflow.Ellipsis, // Use an ellipsis for overflowing text
//                        maxLines = 1 // Restrict the text to a single line
//                    )
                }
            }
        }
    }

//    @Preview
//    @Composable
//    fun CourseCardPrev() {
//        CourseCard(FeatureCourseContentList[0], {})
//    }
//
//    @Preview
//    @Composable
//    fun HomeScreenPrev() {
//        HomeScreen(
//            innerPaddingValues = PaddingValues(vertical = 80.dp),
//            rememberNavController(),
//            userData = userData
//        )
//    }