package com.example.studybuddy.ui.screen

import android.service.autofill.OnClickAction
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.R
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.theme.Shapes

object TutorListDestination : NavigationDestination {
    override val route = "tutorList"
    override val title = "TutorList"
}
@Composable
fun TutorListScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    )
    {
        repeat(6){
            item {
                TutorCard(
                    onClickAction = {
                        navController.navigate(TutorDetailDestination.route)
                    }
                )
                Divider(thickness = 1.dp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun TutorCard(
    onClickAction: ()->Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 250.dp, min = 250.dp),
        shape = Shapes.small
    ) {
        Row {
            Surface(
                modifier = Modifier
                    .weight(4f)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
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
            }
            Surface(
                modifier = Modifier
                    .weight(6f)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Name"
                    )
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Description",
                            modifier = Modifier.weight(6f)
                        )
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(5f)
                        ) {
                            Text(
                                text = "Time",
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = onClickAction,
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = "See more",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TutorCardPrev() {
    TutorCard({})
}

@Preview
@Composable
fun TutorListScreenPrev() {
    TutorListScreen(navController = rememberNavController())
}