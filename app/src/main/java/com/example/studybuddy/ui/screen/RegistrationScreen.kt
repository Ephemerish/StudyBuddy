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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.R
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import android.Manifest
import android.content.Context
import android.graphics.Paint.Align
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.studybuddy.data.UriPathFinder
import com.example.studybuddy.presentation.sign_in.GoogleAuthUiClient
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

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
    googleAuthUiClient: GoogleAuthUiClient
) {
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
                viewModel.updateUiState(ItemDetails(
                    img = address
                ))
            }
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        item{
            Surface {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "register top background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp, min = 250.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
//                    Image(
//                        painter = painterResource(R.drawable.ic_launcher_foreground),
//                        contentDescription = "",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(max = 250.dp, min = 250.dp)
//                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    AsyncImage(
                        model = googleAuthUiClient.getSignedUser()?.profilePictureUrl,
                        error = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
                        contentDescription = "Profile Pic",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    if(googleAuthUiClient.getSignedUser()?.userName != null)
                    {
                        Text(
                            text = googleAuthUiClient.getSignedUser()?.userName!!,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
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
                context = context
            )
        }
        item {
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.clearSubjects()
                    navController.navigateUp()
                }
            }) {
                Text(text = "clearSampleSubject")
            }
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.insertSampleSubjects()
                    navController.navigateUp()
                }
            }) {
                Text(text = "insertSampleData")
            }
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.upsertSubject()
                    navController.navigateUp()
                }
            }) {
                Text(text = "upsert name")
            }
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
    context: Context
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = itemDetails.name,
            onValueChange = {
                onValueChange(itemDetails.copy(name = it))
            },
            label = {
                Text("Subject")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = "*required field",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Left
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
            AsyncImage(
                model =  ImageRequest.Builder(context = context)
                    .data(itemDetails.img)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
                    .heightIn(max = 250.dp)
            )
        }
//        OutlinedTextField(
//            value = itemDetails.price,
//            onValueChange = { onValueChange(itemDetails.copy(price = it)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//            label = { Text("Description") },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            ),
//            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = enabled,
//            singleLine = true
//        )
//        OutlinedTextField(
//            value = itemDetails.quantity,
//            onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            label = { Text("Schedule") },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            ),
//            modifier = Modifier.fillMaxWidth(),
//            enabled = enabled,
//            singleLine = true
//        )
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