package com.example.studybuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.studybuddy.R
import com.example.studybuddy.ui.navigation.NavigationDestination

object MyClassDestination : NavigationDestination {
    override val route = "myClass"
    override val title = "My Class"
}
@Composable
fun MyClassScreen() {
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
            text = "No upcoming\n" +
                    "Class",
            style = TextStyle(
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview
@Composable
fun MyClassScreenPrev() {
    MyClassScreen()
}