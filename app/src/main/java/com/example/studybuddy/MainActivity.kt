package com.example.studybuddy

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.presentation.sign_in.GoogleAuthUiClient
import com.example.studybuddy.presentation.sign_in.ProfileDestination
import com.example.studybuddy.presentation.sign_in.ProfileScreen
import com.example.studybuddy.presentation.sign_in.SignInDestination
import com.example.studybuddy.presentation.sign_in.SignInScreen
import com.example.studybuddy.presentation.sign_in.SignInViewModel
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            onTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
        )
    }
    val firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        setContent {
            StudyBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudyBuddyApp(
                        firebase= firebase,
                        googleAuthUiClient = googleAuthUiClient,
                        applicationContext = applicationContext
                    )
                }
            }
        }
    }
}

@Composable
fun StudyBuddyApp(
    googleAuthUiClient: GoogleAuthUiClient,
    applicationContext: Context,
    viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory),
    firebase: DatabaseReference
) {
    val loggedUser by viewModel.user.collectAsState(null)
    val loginNavController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    NavHost(navController = loginNavController, startDestination = SignInDestination.route) {
        composable(SignInDestination.route){
            val state by viewModel.state.collectAsState()
            LaunchedEffect(key1 = Unit){
               if (loggedUser != null){
                    loginNavController.navigate(StudyBuddyDestination.route)
              }
            }

            val launcher = rememberLauncherForActivityResult(
                contract =  ActivityResultContracts.StartIntentSenderForResult(),
                onResult = {result ->
                    if(result.resultCode == RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )
            LaunchedEffect(key1 = state.isSignInSuccesful){
                if(state.isSignInSuccesful){
                    Toast.makeText( applicationContext,"Sign In Successful", Toast.LENGTH_LONG)
                        .show()
                    viewModel.saveUser(googleAuthUiClient.getSignedUser())
                    loginNavController.navigate(StudyBuddyDestination.route)
                    viewModel.resetState()
                }
            }
            SignInScreen(
                state = state,
                onSignClick = {
                    if (loggedUser != null){
                        loginNavController.navigate(StudyBuddyDestination.route)
                    }else {
                    coroutineScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                }
            )
        }
        composable(StudyBuddyDestination.route){
            StudyBuddyScreen(
                firebase = firebase,
                loginNavController = loginNavController,
                googleAuthUiClient = googleAuthUiClient,
                onSignOut = {
                    coroutineScope.launch {
                        googleAuthUiClient.signout()
                        Toast.makeText(
                            applicationContext,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                        loginNavController.popBackStack()
                        viewModel.logoutUser()
                    }
                }
            )
        }
        composable(ProfileDestination.route) {
            ProfileScreen(
                userData = googleAuthUiClient.getSignedUser(),
                onSignOut = {
                    coroutineScope.launch {
                        googleAuthUiClient.signout()
                        Toast.makeText(
                            applicationContext,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.logoutUser()
                        loginNavController.navigate(SignInDestination.route)
//                        {
//                            loginNavController.graph.startDestinationRoute?.let { route ->
//                                popUpTo(route) {
//                                    saveState = true
//                                }
//                            }
//                            // Avoid multiple copies of the same destination when
//                            // reselecting the same item
//                            launchSingleTop = true
//                            // Restore state when reselecting a previously selected item
//                            restoreState = true
//                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudyBuddyTheme {
        Surface {
           // StudyBuddyApp(googleAuthUiClient, applicationContext)
        }
    }
}