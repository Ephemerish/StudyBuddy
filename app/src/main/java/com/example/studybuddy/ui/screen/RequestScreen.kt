package com.example.studybuddy.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.studybuddy.R
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.theme.Shapes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object RequestDestination : NavigationDestination {
    override val route = "request"
    override val title = "Request"
}

@Composable
fun RequestScreen(
    viewModel: RequestViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.fetchDataFromDatabase(context = context)
    }
    val coroutineScope = rememberCoroutineScope()
    val userSubject by viewModel.userRequestList
    // Fetch data when HomeScreen is created
    if(userSubject.isNotEmpty()){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(userSubject){
                RequestCard(
                    userRequest= it,
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                    context = context
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
                contentDescription = ""
            )
            Text(
                text = "No Request",
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
fun RequestCard(
    userRequest: UserRequestData,
    viewModel: RequestViewModel,
    coroutineScope: CoroutineScope,
    context: Context
) {
    Card(
//        onClick = {
//        coroutineScope.launch(){
//            viewModel.updateRequest(
//                updatedData = userRequest,
//                newRequestState = RequestStateType.ACCEPTED,
//                context = context
//            )
//        } },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp, min = 200.dp),
        shape = Shapes.small
    ) {
//        Text(text = "${userRequest.senderUserName ?: "NULL"} - ${userRequest.selectedSubjectName} - ${userRequest.requestState}")
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = userRequest.senderUserName ?: "NULL",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
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
                    model = ImageRequest.Builder(context = context)
                        .data(userRequest.senderUserProfilePic)
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
                        text = "Requested Subject:\n" +
                                "     ${userRequest.selectedSubjectName}",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Status:\n" +
                                "     ${userRequest.requestState}",
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (userRequest.requestState == RequestStateType.PENDING) {
                            Button(onClick = {
                                coroutineScope.launch() {
                                    viewModel.updateRequest(
                                        updatedData = userRequest,
                                        newRequestState = RequestStateType.ACCEPTED,
                                        context = context
                                    )
                                }
                            }) {
                                Text(text = "ACCEPT")
                            }
                            Button(onClick = {
                                coroutineScope.launch() {
                                    viewModel.updateRequest(
                                        updatedData = userRequest,
                                        newRequestState = RequestStateType.REJECTED,
                                        context = context
                                    )
                                }
                            }) {
                                Text(text = "REJECT")
                            }
                        } else if(userRequest.requestState == RequestStateType.ACCEPTED){
                            Column(
                                verticalArrangement = Arrangement.Top,
                                modifier = Modifier
                            ) {
                            Text(
                                text = "Contact Requester at:",
                                modifier = Modifier
                            )
                            Text(
                                text = "Due to privacy reason we will not share the email yet",
                                modifier = Modifier,
                                color = Color.Red
                            )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MessageScreenPrev() {
    RequestScreen()
}