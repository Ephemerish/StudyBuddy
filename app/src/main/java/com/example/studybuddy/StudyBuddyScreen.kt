package com.example.studybuddy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.studybuddy.data.navigationItemContentList
import com.example.studybuddy.data.sideNavigationItemContentList
import com.example.studybuddy.presentation.sign_in.GoogleAuthUiClient
import com.example.studybuddy.presentation.sign_in.ProfileDestination
import com.example.studybuddy.presentation.sign_in.UserData
import com.example.studybuddy.ui.StudyBuddyUiState
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.navigation.StudyBuddyBodyNavHost
import com.example.studybuddy.ui.theme.StudyBuddyViewModel
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object StudyBuddyDestination : NavigationDestination {
    override val route = "StudyBuddy"
    override val title = "StudyBuddy"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyBuddyScreen(
    modifier: Modifier = Modifier,
    loginNavController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    onSignOut: () -> Job,
    firebase: DatabaseReference,
    userData: UserData?
) {
    val viewModel: StudyBuddyViewModel = viewModel()
    val navDrawerUiState = viewModel.uiState.collectAsState().value
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.fetchNotificationRequestDataFromDatabase(userData?.userId ?: "null")
    }

    var notificationRequestRequestList by viewModel.notificationRequestRequestList
    var notificationRequestAcceptList by viewModel.notificationRequestAcceptList

    if(notificationRequestRequestList.isNotEmpty()) {
        notificationRequestRequestList.forEach(){
            makeNotification(
                context = context,
                message = "${it.senderUserName} requested to be tutored in ${it.selectedSubjectName}",
                notificationTitle = "You received a request"
            )
            viewModel.deleteNotificationRequestFromDatabase(
                currentUser = it.receiverUserId ?: "NULL",
                subjectName = it.selectedSubjectName ?: "NULL",
                context = context,
            )
        }
        notificationRequestRequestList = emptyList()
    }
    if(notificationRequestAcceptList.isNotEmpty()) {
        notificationRequestAcceptList.forEach(){
            makeNotification(
                context = context,
                message = "${it.senderUserName} accepted your request to be tutored in ${it
                    .selectedSubjectName}. Let the learning begin!",
                notificationTitle = "Your request has been accepted"
            )
            viewModel.deleteNotificationRequestFromDatabase(
                currentUser = it.receiverUserId ?: "NULL",
                subjectName = it.selectedSubjectName ?: "NULL",
                context = context,
            )
        }
        notificationRequestAcceptList = emptyList()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideBar(
                navController = navController,
                viewModel = viewModel,
                navDrawerUiState = navDrawerUiState,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Scaffold(
            topBar = {
                StudyBuddyTopBar(
                    userData =userData,
                    viewModel = viewModel,
                    navDrawerUiState = navDrawerUiState,
                    loginNavController = loginNavController,
                    drawerState = drawerState,
                    scope = scope
                )
            },
            bottomBar = {
                StudyBuddyNavigationBar(
                    navController = navController,
                    viewModel = viewModel,
                    navDrawerUiState =navDrawerUiState
                )
            }
        ) {
            Column(
                Modifier.padding(it)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Text(
                        text = navDrawerUiState.currentNavDrawer.name,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
                Surface(
                    modifier = Modifier
                        .weight(15f)
                        .fillMaxSize()
                ) {
                    StudyBuddyBodyNavHost(
                        firebase = firebase,
                        navController = navController,
                        viewModel = viewModel,
                        navDrawerUiState = navDrawerUiState,
                        scope = scope,
                        innerPaddingValues = PaddingValues(),
                        googleAuthUiClient =  googleAuthUiClient,
                        userData = userData
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyBuddyTopBar(
    viewModel: StudyBuddyViewModel,
    modifier: Modifier = Modifier,
    navDrawerUiState: StudyBuddyUiState,
    drawerState: DrawerState,
    scope: CoroutineScope,
    loginNavController: NavHostController,
    userData: UserData?
) {
    CenterAlignedTopAppBar(
        title = {
//            Text(
//                text = "StudyBuddy",
//                style = MaterialTheme.typography.titleLarge
//            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch{
                    drawerState.open()
                }
            },
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Navagation",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        },
        actions = {
            IconButton(onClick = {
                loginNavController.navigate(ProfileDestination.route)
            },
            ) {
                if(userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile Img",
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.background),
                                CircleShape
                            )
                            .padding(1.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Profile Img",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideBar(
    navController: NavHostController,
    viewModel: StudyBuddyViewModel,
    modifier: Modifier = Modifier,
    navDrawerUiState: StudyBuddyUiState,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
    ) {
        sideNavigationItemContentList.forEachIndexed { index, navItem ->
            NavigationDrawerItem(
                label = { Text(navItem.text) },
                selected = navItem.navDrawerType == navDrawerUiState.currentNavDrawer,
                onClick = {
                    navController.navigate(navItem.destination.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo (route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                    viewModel.updateSelectItemIndex(index)
                    scope.launch {
                        drawerState.close()
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = ""
                    )
                },
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}

@Composable
fun StudyBuddyNavigationBar(
    navController: NavHostController,
    viewModel: StudyBuddyViewModel,
    modifier: Modifier = Modifier,
    navDrawerUiState: StudyBuddyUiState
) {
    val bottomNavigationContentDescription = "NAVAGATION"
    NavigationBar(
        modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 20),
    ) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = navDrawerUiState.currentNavDrawer == navItem.navDrawerType,
                onClick = {
                    navController.navigate(navItem.destination.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo (route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

//@Preview
//@Composable
//fun StudyBuddyScreenPrev() {
//    StudyBuddyScreen(
//        loginNavController = loginNavController,
//        googleAuthUiClient = googleAuthUiClient
//    ) {
//        coroutineScope.launch {
//            googleAuthUiClient.signout()
//            android.widget.Toast.makeText(
//                applicationContext,
//                "Signed out",
//                android.widget.Toast.LENGTH_LONG
//            ).show()
//            loginNavController.popBackStack()
//        }
//    }
//}