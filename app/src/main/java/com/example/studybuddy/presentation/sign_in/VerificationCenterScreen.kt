package com.example.studybuddy.presentation.sign_in

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.studybuddy.R
import com.example.studybuddy.data.UriPathFinder
import com.example.studybuddy.data.database.User
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

object VerificationCenterDestination : NavigationDestination {
    override val route = "verificationCenter"
    override val title = "VerificationCenter"
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VerificationCenterScreen(
    viewModel: VerificationCenterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    loggedUser: User?,
    loginNavController: NavHostController
) {
    var userProfile: UserProfileFirebase = UserProfileFirebase()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.fetchProfileDataFromDatabase(loggedUser?.userId ?: "null")
    }

    val userProfileList by viewModel.userProfileList

    val coroutineScope = rememberCoroutineScope()
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )
    SideEffect {
        permissionState.launchPermissionRequest()
    }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri->
            val uriPathFinder = UriPathFinder()
            selectedImageUri = uri
            val uri = Uri.parse(uri.toString())
            val address = uriPathFinder.getPath(context,uri)
            coroutineScope.launch{
                //  viewModel.saveItem(Photo(uri = it))
                viewModel.updateUiState(
                    viewModel.itemUiState.itemDetails.copy(
                        img = address ?: "null",
                        uri = uri
                    )
                )
            }
        }
    )

    val itemDetails =  viewModel.itemUiState.itemDetails
    if(userProfileList.isNotEmpty()) {
        userProfile = userProfileList[0]
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Spacer(modifier =Modifier.padding(vertical = 7.dp))
        // Add instructions for ID photo submission
        Text(
            text = "Get Verified",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.Start)
        )
        if((userProfile.userVerificationState ?: "NULL") ==
            VerificationStateType.FAILED.toString() ||
            (userProfile.userVerificationState ?: "NULL") ==
            "NOT APPLIED"
        ){
            Text(
                text = "You are not a verified user",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )
            Text(
                text = "Please send your valid BISU ID so we can verify your identify for" +
                        " security reasons. Being Verified allows you to access all our services.",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start),
            )

            Text(
                text = "To insure verification process easier please observe the following:",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )

            Text(
                text = "At the moment we will only accept Computer Engineering students.",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Ensure the entire ID is visible and the details are legible.",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Avoid glare and shadows that may obscure information.",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Do not crop the photo.",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier =Modifier.padding(vertical = 5.dp))
            Card(onClick = {
                if (permissionState.status.isGranted) {
                    filePickerLauncher.launch("*/*")
                }
                else{
                    permissionState.launchPermissionRequest()
                }
            }) {
                Spacer(modifier = Modifier.padding(5.dp))
                AsyncImage(
                    model =  ImageRequest.Builder(context = context)
                        .data(itemDetails.img)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.upload_image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                )
            }
            Spacer(modifier =Modifier.padding(vertical = 10.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.putIDFileFirebase(userProfile = userProfile)
                        viewModel.toastMessage(context, "You have successfully applied for " +
                                "registration")
                        loginNavController.navigateUp()
                    }
                },
                enabled = viewModel.itemUiState.isEntryValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .heightIn(min = 50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Apply")
            }
        } else if ((userProfile.userVerificationState ?: "NULL") ==
            VerificationStateType.APPLIED.toString())
        {
            Text(
                text = "You have already applied, your application will be processed soon by our staff.",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "You are already Verified.",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
        }
    }
}