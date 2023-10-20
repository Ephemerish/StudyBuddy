package com.example.studybuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.R
import com.example.studybuddy.ui.navigation.NavigationDestination

object TutorDetailDestination : NavigationDestination {
    override val route = "tutorDetail"
    override val title = "TutorDetail"
}
@Composable
fun TutorDetailScreen(
    navController: NavHostController
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
                    contentDescription = "Tutor Pic",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "Peer-to-peer")
                Text(text = "Expert Tutor")
                Spacer(modifier = Modifier.padding(10.dp))
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "NAME",
                )
                Button(
                    onClick = { deleteConfirmationRequired = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Apply Now",
                        fontSize = 20.sp
                    )
                }
            }
        }
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
            Text(text = "Resume")
            Text(text = "the resume")
            Text(text = "Curriculum")
            Text(text = "the Curriculum")
            }
        }
        Surface(
            modifier = Modifier
                .heightIn(max = 200.dp, min = 200.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
            ) {
                Divider(thickness = 1.dp, color = Color.Gray)
                LazyRow(){
                    repeat(3){
                        item {
                            Image(
                                painter = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
                                contentDescription = "Tutors Cource Pic",
                                Modifier.fillMaxHeight()
                            )
                        }
                    }
                }
                Divider(thickness = 1.dp, color = Color.Gray)
            }
        }
    }
    if (deleteConfirmationRequired) {
        EnrollConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
            },
            onDeleteCancel = {
                deleteConfirmationRequired = false
            },
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun EnrollConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Attention") },
        text = { Text("Are You sure You want to Enroll to (NAME OF PERSON)") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "no")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "yes")
            }
        }
    )}

@Preview
@Composable
fun TutorDetailScreenPrev() {
    TutorDetailScreen(navController = rememberNavController())
}