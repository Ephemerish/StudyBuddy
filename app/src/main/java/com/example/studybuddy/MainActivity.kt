package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studybuddy.ui.StudyBuddyUiState
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import com.example.studybuddy.ui.theme.StudyBuddyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            StudyBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudyBuddyApp(

                    )
                }
            }
        }
    }
}

@Composable
fun StudyBuddyApp () {
    StudyBuddyScreen()
}

data class NavigationItemContent(
    val navDrawerType: NavDrawerType,
    val icon: ImageVector,
    val text: String
)

enum class NavDrawerType {
    Home, Message, MyClass, ContactUs
}

val navigationItemContentList = listOf(
    NavigationItemContent(
        navDrawerType = NavDrawerType.Home,
        icon = Icons.Default.Home,
        text = "Home"
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.Message,
        icon = Icons.Default.Email,
        text = "Message"
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.MyClass,
        icon = Icons.Filled.AccountBox,
        text = "MyClass"
    )
)

val sideNavigationItemContentList = listOf(
    NavigationItemContent(
        navDrawerType = NavDrawerType.Message,
        icon = Icons.Default.Email,
        text = "Message"
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.MyClass,
        icon = Icons.Filled.AccountBox,
        text = "MyClass"
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.ContactUs,
        icon = Icons.Filled.Call,
        text = "ContactUs"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyBuddyScreen(
    modifier: Modifier = Modifier
) {
    val viewModel:StudyBuddyViewModel = viewModel()
    val navDrawerUiState = viewModel.uiState.collectAsState().value
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideBar(
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
                    viewModel = viewModel,
                    navDrawerUiState = navDrawerUiState,
                    drawerState = drawerState,
                    scope = scope
                )
            },
            bottomBar = {
                StudyBuddyNavigationBar(
                    viewModel = viewModel,
                    navDrawerUiState =navDrawerUiState
                )
            }
        ) {
            StudyBuddyNavHost(
                viewModel = viewModel,
                navDrawerUiState = navDrawerUiState,
                scope = scope,
                innerPaddingValues = it
            )
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
    scope: CoroutineScope
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "StudyBuddy",
                style = MaterialTheme.typography.titleLarge
            )
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
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Navagation",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = navDrawerUiState.currentNavDrawer.name,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    )
}

@Composable
fun StudyBuddyNavHost(
    viewModel: StudyBuddyViewModel,
    modifier: Modifier = Modifier,
    navDrawerUiState: StudyBuddyUiState,
    scope: CoroutineScope,
    innerPaddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.padding(innerPaddingValues)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${navDrawerUiState.currentNavDrawer}",
                    style = TextStyle(
                        fontSize = 70.sp,
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideBar(
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
        sideNavigationItemContentList.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { Text(item.text) },
                selected = item.navDrawerType == navDrawerUiState.currentNavDrawer,
                onClick = {
                    viewModel.updateCurrentMailbox(item.navDrawerType)
                    viewModel.updateSelectItemIndex(index)
                    scope.launch {
                        drawerState.close()
                    }
                },
                icon = {
                    Icon(
                        imageVector = if(index == navDrawerUiState.selectItemIndex) item.icon else Icons.Default
                            .PlayArrow,
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
    viewModel: StudyBuddyViewModel,
    modifier: Modifier = Modifier,
    navDrawerUiState: StudyBuddyUiState
) {
    Box(modifier = modifier.padding(5.dp)) {
        val bottomNavigationContentDescription = "NAVAGATION"
        NavigationBar(modifier = modifier) {
            for (navItem in navigationItemContentList) {
                NavigationBarItem(
                    selected = navDrawerUiState.currentNavDrawer == navItem.navDrawerType,
                    onClick = {
                        viewModel.updateCurrentMailbox(navItem.navDrawerType)
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudyBuddyTheme {
        Surface {
            StudyBuddyApp()
        }
    }
}