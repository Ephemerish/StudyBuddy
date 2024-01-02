package com.example.studybuddy.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.example.studybuddy.presentation.sign_in.UserProfileFirebase
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.theme.Shapes
import com.example.studybuddy.ui.theme.StudyBuddyViewModel
import kotlinx.coroutines.launch

object TutorListDestination : NavigationDestination {
    override val route = "tutorList"
    override val title = "TutorList"
}
@Composable
fun TutorListScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavHostController,
    selectedSubject: String,
    viewModel: TutorListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    appViewModel: StudyBuddyViewModel,
) {
    val userSubject by appViewModel.currentUserSubject
    val context = LocalContext.current
    // Fetch data when HomeScreen is created
    LaunchedEffect(key1 = Unit){
        viewModel.fetchSubjectDataFromDatabase(selectedSubject)
        viewModel.fetchProfileDataFromDatabase()
    }
    val subjectList by viewModel.UserSubjectList
    val profileList by viewModel.userProfileList

    val coroutineScope = rememberCoroutineScope()

    if(subjectList.isNotEmpty()){
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    )
    {
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = selectedSubject,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(3.dp))
            }
        }
        items(subjectList){
            TutorCard(
                onClickAction = {
                    appViewModel.updateCurrentUserSubject(it)
                    navController.navigate(
                        route = TutorDetailDestination.route + "/${selectedSubject}"
                    )
                },
                tutorID = it.userId ?: "NULL",
                tutorName = it.userName ?: "NULL",
                tutorDescription = "Profile:\n   -\n   -\n   \n",
                tutorTime = "Schedule:\n   -\n   -\n",
                tutorImage = it.userProfilePic,
                context = context,
                profileList = profileList
                )
            Divider(thickness = 1.dp, color = Color.Gray)
        }
    }} else{
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.padding(30.dp))
            Text(
                text = "NO TUTORS\n" +
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

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        ExtendedFloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    viewModel.beTutorToSubject(
                        selectedSubject = selectedSubject,
                        profileList = profileList
                    )
                }
            },
            icon = { Icon(Icons.Filled.AddCircle, "Extended floating action button.") },
            text = { Text(text = "Be a tutor") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorCard(
    onClickAction: () -> Unit,
    tutorName: String,
    tutorDescription: String,
    tutorTime: String,
    tutorImage: String?,
    context: Context,
    profileList: List<UserProfileFirebase>,
    tutorID: String
) {
    var tutorProfile: UserProfileFirebase = UserProfileFirebase()
    profileList.forEach { profile ->
        if(profile.userId == tutorID){
            tutorProfile = profile
        }
    }

    Card(
        onClick = onClickAction,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp, min = 200.dp),
        shape = Shapes.small
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = tutorName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight= FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.padding(3.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model =  ImageRequest.Builder(context = context)
                        .data(tutorImage)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.defaultprofile),
                    contentDescription = "Tutor Pic",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .weight(4f)
                        .padding(4.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .weight(6f)
                        .absolutePadding(left = 5.dp, right = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(3.2f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tutorProfile.userBio ?: "Null",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp), // Adjust padding as needed
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 5,
                        )
                    }
                    Text(
                        text = "${tutorProfile.userCourse ?: "Null"} " +
                                "Year ${tutorProfile.userYearLevel ?: "Null"}",
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun TutorCardPrev() {
//    TutorCard({}, "",)
//}

//@Preview
//@Composable
//fun TutorListScreenPrev() {
//    TutorCard(
//        onClickAction = { /*TODO*/ },
//        tutorName = "test",
//        tutorDescription = "deccription",
//        tutorTime = "time",
//        tutorImage = "null",
//        context = LocalContext.current,
//        profileList = profileList
//    )
//}