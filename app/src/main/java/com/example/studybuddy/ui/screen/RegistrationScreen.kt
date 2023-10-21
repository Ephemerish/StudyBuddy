package com.example.studybuddy.ui.screen

import android.graphics.drawable.VectorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.R
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object RegistrationDestination : NavigationDestination {
    override val route = "registration"
    override val title = "Registration"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavHostController,
    viewModel: RegistrationViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
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
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp, min = 250.dp)
                    )
                }
            }
        }
        repeat(10){
            item{
                OutlinedTextField(
                    value = "",
                    onValueChange = {  },
                    label = {
                        Text(
                            text = "Parameter ${it+1}",
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    singleLine = true
                )
            }
        }
        item {
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.clearSampleSubjects()
                    navController.navigateUp()
                }
            }) {
                Text(text = "clearSampleSubject")
            }
        }
        item {
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.insertSampleSubjects()
                    navController.navigateUp()
                }
            }) {
                Text(text = "insertSampleData")
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPrev() {
    RegistrationScreen(navController = rememberNavController())
}