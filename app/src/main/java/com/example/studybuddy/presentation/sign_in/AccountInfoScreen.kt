package com.example.studybuddy.presentation.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studybuddy.data.database.User
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination

object AccountInfoDestination : NavigationDestination {
    override val route = "accountInfo"
    override val title = "AccountInfo"
}

@Composable
fun AccountInfoScreen(
    viewModel: AccountInfoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    loggedUser: User?
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.fetchProfileDataFromDatabase(loggedUser?.userId ?: "null")
    }
    val userProfile by viewModel.userProfileList

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, bottom = 20.dp)
            .padding(horizontal = 20.dp)
    ) {
        if (userProfile.isNotEmpty()) {
            val profile = userProfile[0]
            Spacer(modifier = Modifier.height(5.dp))
            InfoRow("User Name", profile.userName)
            InfoRow("User ID", profile.userId)
            InfoRow("Email", profile.userEmail)
            InfoRow("Profile Pic", profile.userProfilePic)
            InfoRow("Verification State", profile.userVerificationState)
            InfoRow("Municipality", profile.userMunicipality)
            InfoRow("Bio", profile.userBio)
            InfoRow("Year Level", profile.userYearLevel)
            InfoRow("Course", profile.userCourse)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String?) {
    Column {
        Text(
            text = "$label:",
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "\t${value ?: "Unknown"}",
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}