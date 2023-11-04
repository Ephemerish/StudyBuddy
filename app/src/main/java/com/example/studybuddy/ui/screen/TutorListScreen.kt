package com.example.studybuddy.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.studybuddy.R
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.theme.Shapes
import com.example.studybuddy.ui.theme.StudyBuddyViewModel

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
        viewModel.fetchDataFromDatabase(selectedSubject)
    }
    val subjectList by viewModel.UserSubjectList
    if(subjectList.isNotEmpty()){
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    )
    {
        items(subjectList){
            TutorCard(
                onClickAction = {
                    appViewModel.updateCurrentUserSubject(it)
                    navController.navigate(
                        route = TutorDetailDestination.route
                    )
                },
                tutorName = it.userName ?: "NULL",
                tutorDescription = "Profile:\n   -\n   -\n   \n",
                tutorTime = "Schedule:\n   -\n   -\n",
                tutorImage = it.userProfilePic,
                context = context
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorCard(
    onClickAction: () -> Unit,
    tutorName: String,
    tutorDescription: String,
    tutorTime: String,
    tutorImage: String?,
    context: Context
) {
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
                    modifier = Modifier.weight(6f)
                ) {
                    Text(
                        text = tutorDescription,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = tutorTime,
                        modifier = Modifier.weight(1f)
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

@Preview
@Composable
fun TutorListScreenPrev() {
    TutorCard(
        onClickAction = { /*TODO*/ },
        tutorName = "test",
        tutorDescription = "deccription",
        tutorTime = "time",
        tutorImage = "null",
        context = LocalContext.current
    )
}