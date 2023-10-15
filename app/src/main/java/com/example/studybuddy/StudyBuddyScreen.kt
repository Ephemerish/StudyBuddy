package com.example.studybuddy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.data.navigationItemContentList
import com.example.studybuddy.data.sideNavigationItemContentList
import com.example.studybuddy.ui.StudyBuddyUiState
import com.example.studybuddy.ui.navigation.StudyBuddyBodyNavHost
import com.example.studybuddy.ui.theme.StudyBuddyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyBuddyScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: StudyBuddyViewModel = viewModel()
    val navDrawerUiState = viewModel.uiState.collectAsState().value
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()

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
                    viewModel = viewModel,
                    navDrawerUiState = navDrawerUiState,
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
                    modifier = Modifier.weight(1f)
                        .fillMaxSize()
                ) {
                    Text(
                        text = navDrawerUiState.currentNavDrawer.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                Surface(
                    modifier = Modifier.weight(15f)
                        .fillMaxSize()
                ) {
                    StudyBuddyBodyNavHost(
                        navController = navController,
                        viewModel = viewModel,
                        navDrawerUiState = navDrawerUiState,
                        scope = scope,
                        innerPaddingValues = PaddingValues()
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
    scope: CoroutineScope
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
                    modifier = Modifier.size(30.dp)
                )
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
                        // Restore state when reselecting a previously selected item
                        restoreState = true
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