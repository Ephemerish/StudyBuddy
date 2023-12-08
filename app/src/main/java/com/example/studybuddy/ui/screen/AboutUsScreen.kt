package com.example.studybuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.R
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.theme.Shapes

object AboutUsDestination : NavigationDestination {
    override val route = "aboutUs"
    override val title = "AboutUs"
}
@Composable
fun AboutUsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavHostController,
    selectedSubject: String
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    )
    {
        item {
            AboutUsCard(
                onClickAction = {
                    navController.navigate(TutorDetailDestination.route)
                },
                tutorName = "Kenneth  Harold Panis $selectedSubject",
                tutorDescription = "Description:\n" +
                        "Embark on a coding journey with our programming guru. Learn Python, Java, C++, and more. Perfect for beginners or those looking to enhance their skills.",
                tutorTime = "Schedule:\n" +
                        "\n" +
                        "Mondays: 6:00 PM - 7:30 PM\n" +
                        "Wednesdays: 6:30 PM - 8:00 PM\n" +
                        "Saturdays: 1:00 PM - 2:30 PM",
                tutorImage = R.drawable.harold,
            )
            Divider(thickness = 1.dp, color = Color.Gray)

        }
        item {
            AboutUsCard(
                onClickAction = {
                    navController.navigate(TutorDetailDestination.route)
                },
                tutorName = "Virgilyn Tamayo",
                tutorDescription = "Description:\n" +
                        "Master the art of calculus with personalized, one-on-one tutoring. Whether you're tackling derivatives, integrals, or limits, our sessions will demystify complex math concepts.",
                tutorTime = "Schedule:\n" +
                        "\n" +
                        "Mondays: 4:00 PM - 5:30 PM\n" +
                        "Wednesdays: 3:30 PM - 5:00 PM\n" +
                        "Saturdays: 10:00 AM - 11:30 AM",
                tutorImage = R.drawable.ynah,

                )
            Divider(thickness = 1.dp, color = Color.Gray)
        }
        item {
            AboutUsCard(
                onClickAction = {
                    navController.navigate(TutorDetailDestination.route)
                },
                tutorName = "Crisha Mae Acasio",
                tutorDescription = "Description:\n" +
                        "Explore the world of robotics! Our tutor will guide you through building and programming robots, combining hardware and software for exciting projects.",
                tutorTime = "Schedule:\n" +
                        "\n" +
                        "Tuesdays: 6:00 PM - 7:30 PM\n" +
                        "Thursdays: 6:30 PM - 8:00 PM\n" +
                        "Sundays: 3:00 PM - 4:30 PM",
                tutorImage = R.drawable.crisha

            )
            Divider(thickness = 1.dp, color = Color.Gray)
        }
        item {
            AboutUsCard(
                onClickAction = {
                    navController.navigate(TutorDetailDestination.route)
                },
                tutorName = "Maria Jeziel Quimpan",
                tutorDescription = "Description:\n" +
                        "Level up your software development skills. Learn about web development, databases, and application design. Ideal for aspiring software engineers.",
                tutorTime = "Schedule:\n" +
                        "\n" +
                        "Mondays: 7:00 PM - 8:30 PM\n" +
                        "Wednesdays: 7:30 PM - 9:00 PM\n" +
                        "Saturdays: 2:00 PM - 3:30 PM",
                tutorImage = R.drawable.mj,

                )
            Divider(thickness = 1.dp, color = Color.Gray)
        }
        item {
            AboutUsCard(
                onClickAction = {
                    navController.navigate(TutorDetailDestination.route)
                },
                tutorName = "Jhon Reyl Arcayena",
                tutorDescription = "Description:\n" +
                        "Build a strong foundation in algebra to excel in math. Our expert tutor will help you understand equations, functions, and problem-solving techniques.",
                tutorTime = "Schedule:\n" +
                        "\n" +
                        "Tuesdays: 5:00 PM - 6:30 PM\n" +
                        "Thursdays: 4:30 PM - 6:00 PM\n" +
                        "Sundays: 2:00 PM - 3:30 PM",
                tutorImage = R.drawable.jhon

            )
            Divider(thickness = 1.dp, color = Color.Gray)
        }

    }
}

@Composable
fun AboutUsCard(
    onClickAction: () -> Unit,
    tutorName: String,
    tutorDescription: String,
    tutorTime: String,
    tutorImage: Int
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
                        painter = painterResource(tutorImage),
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
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = tutorName,
                        style = TextStyle(
                            fontWeight= FontWeight.Bold
                        )

                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = tutorDescription,
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
                                text = tutorTime,
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

//@Preview
//@Composable
//fun TutorCardPrev() {
//    TutorCard({}, "",)
//}

@Preview
@Composable
fun AboutUsScreenPrev() {
    AboutUsScreen(navController = rememberNavController(), selectedSubject = "test")
}