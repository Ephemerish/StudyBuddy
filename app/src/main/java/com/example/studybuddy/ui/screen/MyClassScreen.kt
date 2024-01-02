import com.example.studybuddy.ui.screen.MyClassViewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.studybuddy.R
import com.example.studybuddy.ui.AppViewModelProvider
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.screen.SubjectFirebase
import com.example.studybuddy.ui.screen.UserSubjectFirebase

object MyClassDestination : NavigationDestination {
    override val route = "myClass"
    override val title = "My Class"
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MyClassScreen(
    viewModel: MyClassViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    val userSubject by viewModel.userSubjectList
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val subjectList by viewModel.subjectList
    val userSubjectList by viewModel.userSubjectList
    var subjectNameToDelete by viewModel.subjectNameToDelete
    var currentUser by remember {
        mutableStateOf("")
    }
    viewModel.fetchSubjectsFromDatabase()
    // Fetch data when HomeScreen is created
    LaunchedEffect(Unit) {
        currentUser = viewModel.getUser()?.userId.toString()
        viewModel.fetchUserSubjectFromDatabase(
            currentUser = currentUser,
            subjectList = subjectList
        )
    }
    
    if(userSubjectList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(userSubjectList) {
//                Text(text = it.subjectName ?: "NULL")
                MyClassCard(
                    onClick = {
                        deleteConfirmationRequired = true
                        subjectNameToDelete = it.subjectName.toString()
//                        viewModel.deleteUserSubjectFromDatabase(
//                            subjectName = it.subjectName,
//                            currentUser = currentUser,
//                            context = context
//                        )
                    },
                    subjectList = subjectList,
                    userSubject = it,
                    context = context
                )
            }
        }
        if (deleteConfirmationRequired) {
            AlertDialog(onDismissRequest = { /* Do nothing */ },
                title = { Text("Attention") },
                text = { Text("Are you sure you want to delete your posting in " +
                        "in ${subjectNameToDelete}?")},
                modifier =  Modifier.padding(10.dp),
                dismissButton = {
                    TextButton(onClick = {
                        deleteConfirmationRequired = false
                    }) {
                        Text(text = "no")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteUserSubjectFromDatabase(
                            subjectName = subjectNameToDelete,
                            currentUser = currentUser,
                            context = context
                        )
                        deleteConfirmationRequired = false
                    }) {
                        Text(text = "yes")
                    }
                }
            )
        }
        } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.sb_logo_with_title),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyClassCard(
    userSubject: UserSubjectFirebase,
    context: Context,
    subjectList: List<SubjectFirebase>,
    onClick: () -> Unit
) {
    var subjectImgUrl: String = "NULL"
    subjectList.forEach(){
        if(it.subjectName == userSubject.subjectName){
            subjectImgUrl = it.imgLink ?: "NULL"
        }
    }
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        enabled = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
//                Image(
//                    painter = painterResource(R.drawable._83945387_1317182618979724_2368759731661496754_n_removebg_preview),
//                    contentDescription = "Cource Photo",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(7f)
//                )
            AsyncImage(
                model = ImageRequest.Builder(context = context)
                    .data(subjectImgUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.sb_logo_with_title),
                contentDescription = "Course Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7f)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = userSubject.subjectName ?: "Null",
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .weight(2f),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    overflow = TextOverflow.Ellipsis, // Use an ellipsis for overflowing text
                    maxLines = 1 // Restrict the text to a single line
                )
            }
            Divider(thickness = 1.dp, color = Color.Gray)
        }
    }
}


//@Preview
//@Composable
//fun MyClassScreenPrev() {
//    MyClassScreen()
//}

//@Preview
//@Composable
//fun MyClassCardPrev() {
//    MyClassCard(it, context)
//}