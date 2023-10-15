package com.example.studybuddy.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.screen.ContactUsDestination
import com.example.studybuddy.ui.screen.HomeDestination
import com.example.studybuddy.ui.screen.MessageDestination
import com.example.studybuddy.ui.screen.MyClassDestination

data class NavigationItemContent(
    val navDrawerType: NavDrawerType,
    val icon: ImageVector,
    val text: String,
    val destination : NavigationDestination
)

enum class NavDrawerType {
    Home, Message, MyClass, ContactUs
}

val navigationItemContentList = listOf(
    NavigationItemContent(
        navDrawerType = NavDrawerType.Home,
        icon = Icons.Default.Home,
        text = "Home",
        destination = HomeDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.Message,
        icon = Icons.Default.Email,
        text = "Message",
        destination = MessageDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.MyClass,
        icon = Icons.Filled.AccountBox,
        text = "MyClass",
        destination = MyClassDestination
    )
)

val sideNavigationItemContentList = listOf(
    NavigationItemContent(
        navDrawerType = NavDrawerType.Message,
        icon = Icons.Default.Email,
        text = "Message",
        destination = MessageDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.MyClass,
        icon = Icons.Filled.AccountBox,
        text = "MyClass",
        destination = MyClassDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.ContactUs,
        icon = Icons.Filled.Call,
        text = "ContactUs",
        destination = ContactUsDestination
    )
)