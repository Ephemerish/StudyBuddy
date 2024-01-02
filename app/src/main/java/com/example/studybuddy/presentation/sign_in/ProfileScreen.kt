package com.example.studybuddy.presentation.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val title = "Profile"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory),
    loginNavController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.fetchProfileDataFromDatabase(userData?.userId ?: "null")
    }

    val userProfile by viewModel.userProfileList
    val coroutineScope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, bottom = 20.dp)
    ) {
        Surface(
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                if(userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile Pic",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(5.dp)
                ){
                    if(userProfile.isNotEmpty())
                    {
                        Text(
                            text = userProfile[0].userName ?: "null",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    } else {
                        if(userData?.userEmail != null)
                        {
                            Text(
                                text = userData.userName ?: "null",
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                    if(userData?.userEmail != null)
                    {
                        Text(
                            text = userData.userEmail,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    if(userProfile.isNotEmpty()) {
                        Text(
                            text = userProfile[0].userVerificationState ?: "NOT APPLIED",
                            textAlign = TextAlign.End,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    } else {
                        Text(
                            text = "NOT APPLIED",
                            textAlign = TextAlign.End,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
        if(userProfile.isNotEmpty()) {
            var isEditing by viewModel.isEditing
            var newBio by viewModel.newBio
            Surface(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            ) {
                if (isEditing) {
                    // Display input widget when editing
                    EditBio(
                        viewModel = viewModel,
                        userProfile = userProfile
                    )
                } else if (userProfile[0].userBio != "") {
                    // Display bio text when not editing
                    Card(onClick = {
                        isEditing = true
                    },
                        colors = CardDefaults.cardColors(Color.Transparent)
                    ) {
                        Text(
                            text = userProfile[0].userBio ?: "null",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                } else {
                    // Display "Add bio" button when not editing and no bio is present
                    Button(
                        onClick = {
                            // Enter editing mode when the button is pressed
                            isEditing = true
                        },
                        modifier = Modifier.width(300.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Add bio"
                        )
                    }
                }
            }
        }
        Button(
            onClick = {
                loginNavController.navigate(VerificationCenterDestination.route)
            },
            modifier = Modifier.width(300.dp)
        ) {
            Text(
                text = "Verification Center"
            )
        }
        Button(
            onClick = {
                loginNavController.navigate(AccountInfoDestination.route)
            },
            modifier = Modifier.width(300.dp)

        ) {
            Text(text = "Account Info.")
        }
        Button(
            onClick = onSignOut,
            modifier = Modifier.width(300.dp)
        ) {
            Text(text = "Sign out")
        }
    }
}

@Composable
fun EditBio(viewModel: SignInViewModel, userProfile: List<UserProfileFirebase>) {
    var isEditing by viewModel.isEditing
    var newBio by viewModel.newBio
    var bioCharacterCount by remember { mutableStateOf(newBio.length) }
    LaunchedEffect(key1 = Unit){
        newBio = userProfile[0].userBio ?: ""
        bioCharacterCount = newBio.length
    }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.35f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = newBio,
            onValueChange = {
                if (it.length <= 256) {
                    newBio = it
                    bioCharacterCount = it.length
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            label = { Text("Bio ($bioCharacterCount/256)") },
            maxLines = 4,
            minLines = 4
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (bioCharacterCount <= 256) {
                        // Save button action
                        // Update the user's bio with the new value (newBio)
                        // You may want to save it to your data source or do other operations
                        viewModel.updateProfileBioFirebase(userProfile[0])
                        isEditing = false
                    }
                },
                modifier = Modifier
                    .padding(5.dp)
                    .heightIn(min = 30.dp),
                enabled = bioCharacterCount <= 256
            ) {
                Text(text = "Save")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // Cancel button action
                    // You may want to handle the cancellation logic here
                    isEditing = false
                },
                modifier = Modifier
                    .padding(5.dp)
                    .heightIn(min = 30.dp)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}