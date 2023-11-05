package com.example.studybuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.studybuddy.R
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import android.Manifest
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.studybuddy.data.UriPathFinder
import com.example.studybuddy.presentation.sign_in.GoogleAuthUiClient
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.firebase.database.DatabaseReference
import java.util.Locale

object RegistrationDestination : NavigationDestination {
    override val route = "registration"
    override val title = "Registration"
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun RegistrationScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavHostController,
    viewModel: RegistrationViewModel = viewModel(factory = AppViewModelProvider.Factory),
    googleAuthUiClient: GoogleAuthUiClient,
    firebase: DatabaseReference
) {

    // Fetch data when HomeScreen is created
    LaunchedEffect(key1 = Unit){
        viewModel.fetchDataFromDatabase()
    }

    val subjectList by viewModel.subjectList

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        item{
            Surface {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "register top background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 100.dp, min = 100.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 100.dp, min = 100.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Image(
//                        painter = painterResource(R.drawable.ic_launcher_foreground),
//                        contentDescription = "",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(max = 250.dp, min = 250.dp)
//                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AsyncImage(
                        model = googleAuthUiClient.getSignedUser()?.profilePictureUrl,
                        error = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
                        contentDescription = "Profile Pic",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    if(googleAuthUiClient.getSignedUser()?.userName != null)
                    {
                        Text(
                            text = googleAuthUiClient.getSignedUser()?.userName!!,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 35.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }
        item {
            ItemInputForm(
                itemDetails =  viewModel.itemUiState.itemDetails,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth(),
                permissionState = permissionState,
                filePickerLauncher = filePickerLauncher,
                context = context,
                enabled = viewModel.itemUiState.isEntryValid,
                subjectList = subjectList,
                viewModel = viewModel
            )
        }
        item {
//            Button(onClick = {
//                coroutineScope.launch {
//                    viewModel.clearSubjects()
//                    navController.navigateUp()
//                }
//            }) {
//                Text(text = "clearSampleSubject")
//            }
//            Button(onClick = {
//                coroutineScope.launch {
//                    viewModel.insertSampleSubjects()
//                    navController.navigateUp()
//                }
//            }) {
//                Text(text = "insertSampleData")
//            }
            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.upsertUserSubject()
                    navController.navigateUp()
                }
            },
                enabled = viewModel.itemUiState.isEntryValid
            ) {
                Text(
                    text = "Upsert Subject",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {},
    enabled: Boolean = true,
    permissionState: PermissionState,
    filePickerLauncher: ManagedActivityResultLauncher<String, Uri?>,
    context: Context,
    subjectList: List<SubjectFirebase>,
    viewModel: RegistrationViewModel
) {
    val context = LocalContext.current
    val inputMethodManager = remember { context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    val view = LocalView.current // Assuming you can access the view here.
    val focusRequester = FocusRequester()

    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .focusRequester(focusRequester),
                value = itemDetails.subjectName,
                onValueChange = {
                    onValueChange(itemDetails.copy(subjectName = it))
                    expanded = true
                    focusRequester.requestFocus()
                },
                label = {
                    Text("Subject")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        expanded = !expanded
                        focusRequester.requestFocus()
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "arrow",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(15.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 150.dp)
                ) {
                    val filterText = itemDetails.subjectName?.lowercase(Locale.ROOT) ?: "NULL"
                    val filteredSubjects = subjectList.filter {
                        it.subjectName?.lowercase(Locale.ROOT)?.contains(filterText) == true
                    }
                    if (filteredSubjects.isNotEmpty()) {
                        items(filteredSubjects) {
                            CategoryItems(
                                title = it.subjectName ?: "null",
                                view = view,
                                inputMethodManager = inputMethodManager
                            ) { title ->
                                viewModel.updateUiState(
                                    viewModel.itemUiState.itemDetails.copy(
                                        subjectName = title,
                                        img = it.imgLink ?: "NULL"
                                    )
                                )
                                expanded = false
                            }
                        }
                    } else {
                        expanded = false
                    }
                }
            }
        }
        if (!enabled) {
            Text(
                text = "*required field",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Left,
                color = Color.Red
            )
        }
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
    }
}


@Composable
fun CategoryItems(
    title: String,
    view:   View,
    inputMethodManager: InputMethodManager,
    onSelect: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 18.sp)
    }
}

//@Preview
//@Composable
//fun RegistrationScreenPrev() {
//    RegistrationScreen(
//        navController = rememberNavController(),
//        googleAuthUiClient = googleAuthUiClient
//    )
//}