package com.example.studybuddy.ui.screen

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studybuddy.ui.navigation.NavigationDestination

object ContactUsDestination : NavigationDestination {
    override val route = "contactUs"
    override val title = "ContactUs"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(

) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {  },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .weight(1f),
        )
        Button(
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Submit")
        }
        Text(
            text = "We will reply you ASAP",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
fun ContactUsScreenPrev() {
    ContactUsScreen()
}