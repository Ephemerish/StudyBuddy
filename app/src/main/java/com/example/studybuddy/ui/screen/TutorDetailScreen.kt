package com.example.studybuddy.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.theme.StudyBuddyViewModel
import kotlinx.coroutines.launch

object TutorDetailDestination : NavigationDestination {
    override val route = "tutorDetail"
    override val title = "TutorDetail"
}

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun TutorDetailScreen(
    navController: NavHostController,
    viewModel: TutorDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    appViewModel: StudyBuddyViewModel,
    selectedSubject: String,
) {
    val profileList by viewModel.userProfileList

    val coroutineScope = rememberCoroutineScope()
    val userSubject by appViewModel.currentUserSubject
    var UploadConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    var currentUser by viewModel.currentUser
    val context = LocalContext.current
    coroutineScope.launch {
        currentUser = viewModel.getCurrentUser()?.userId ?: "NULL"
    }
    LaunchedEffect(key1 = Unit){
        viewModel.fetchProfileDataFromDatabase(userSubject.userId ?: "NULL")
    }
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
        Row(
            modifier = Modifier.weight(1f)
        ) {
            val context = LocalContext.current
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model =  ImageRequest.Builder(context = context)
                        .data(userSubject.userProfilePic)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.defaultprofile),
                    contentDescription = "Tutor Pic",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
//                Spacer(modifier = Modifier.padding(10.dp))
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .absolutePadding(left = 0.dp, right = 20.dp)
            ) {
                Text(
                    text = userSubject.userName ?: "NULL",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { UploadConfirmationRequired = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Apply Now",
                        fontSize = 20.sp
                    )
                }
            }
        }
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                if(profileList.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(3.5f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profileList[0].userBio ?: "Null",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                    Text(
                        text = "${profileList[0].userCourse ?: "Null"} " +
                                "Year ${profileList[0].userYearLevel ?: "Null"}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                } else {
                    Text(text = "BIO")
                    Text(text = "  -")
                    Text(text = "  -")
                    Text(text = "  -")
                    Text(text = "YEAR")
                    Text(text = "  -")
                    Text(text = "  -")
                }
            }
        }
        Surface(
            modifier = Modifier
                .heightIn(max = 150.dp, min = 250.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
            ) {
                Divider(thickness = 1.dp, color = Color.Gray)
                LazyRow(){
                    repeat(3){
                        item {
                            Image(
                                painter = painterResource(R.drawable.sb_logo_with_title),
                                contentDescription = "Tutors Course Pic",
                                Modifier.fillMaxHeight()
                            )
                        }
                    }
                }
                Divider(thickness = 1.dp, color = Color.Gray)
            }
        }
    }
    if (UploadConfirmationRequired) {
        if(currentUser != (userSubject.userId ?: "null")) {
//        if(true){
            EnrollConfirmationDialog(
                onConfirm = {
                    UploadConfirmationRequired = false
                    coroutineScope.launch {
                        viewModel.upsertUserRequest(
                            receiverUserSubject = userSubject
                        )
                    }
                    navController.navigate(
                        route = HomeDestination.route,
                        builder = {
                            popUpTo(HomeDestination.route) {
                                // This will pop all destinations up to the specified route (including itself).
                                inclusive = true
                            }
                        }
                    )
                },
                onCancel = {
                    UploadConfirmationRequired = false
                },
                modifier = Modifier.padding(10.dp),
                tutorName = userSubject?.userName ?: "NULL",
                subject = userSubject.subjectName
            )
        } else {
            AlertDialog(onDismissRequest = { /* Do nothing */ },
                title = { Text("Hey...") },
                text = { Text("You cannot request to your self, silly!") },
                modifier = Modifier.padding(10.dp),
                confirmButton = {
                    TextButton(onClick = {
                            UploadConfirmationRequired = false
                    }) {
                        Text(text = "Back")
                    }
                }
            )
        }
    }
}

@Composable
fun EnrollConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    tutorName: String,
    subject: String?,
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Attention") },
        text = { Text("Are you sure you want to send a request to ${tutorName}'s ${subject} " +
                "class?")},
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = "no")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "yes")
            }
        }
    )
}

//@Preview
//@Composable
//fun TutorDetailScreenPrev() {
//    TutorDetailScreen(
//        navController = rememberNavController(),
//        currentUserSubject = UserSubjectFirebase(),
//        appViewModel = viewModel
//    )
//}