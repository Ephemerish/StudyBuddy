package com.example.studybuddy.presentation.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studybuddy.R
import com.example.studybuddy.ui.navigation.NavigationDestination

object SignInDestination : NavigationDestination {
    override val route = "signIn"
    override val title = "SignIn"
}

@Composable
fun SignInScreen(
    state: SignInState,
    onSignClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let {error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                    painter = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
            contentDescription = "LOGO",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onSignClick) {
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = "Google LOGO",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = " Sign in with Google",
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview
@Composable
fun SignInScreenPrev() {
    SignInScreen(state = SignInState(), {})
}