package com.example.studybuddy.data

import MyClassDestination
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.studybuddy.ui.navigation.NavigationDestination
import com.example.studybuddy.ui.screen.AboutUsDestination
import com.example.studybuddy.ui.screen.ContactUsDestination
import com.example.studybuddy.ui.screen.HomeDestination
import com.example.studybuddy.ui.screen.RequestDestination

data class NavigationItemContent(
    val navDrawerType: NavDrawerType,
    val icon: ImageVector,
    val text: String,
    val destination : NavigationDestination
)

enum class NavDrawerType {
    Home, Request, MyClass, ContactUs, AboutUs
}

val navigationItemContentList = listOf(
    NavigationItemContent(
        navDrawerType = NavDrawerType.Home,
        icon = Icons.Default.Home,
        text = "Home",
        destination = HomeDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.Request,
        icon = Icons.Default.Person,
        text = "Request",
        destination = RequestDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.MyClass,
        icon = Icons.Filled.DateRange,
        text = "MyClass",
        destination = MyClassDestination
    )
)

val sideNavigationItemContentList = listOf(
    NavigationItemContent(
        navDrawerType = NavDrawerType.Request,
        icon = Icons.Default.Person,
        text = "Request",
        destination = RequestDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.MyClass,
        icon = Icons.Filled.DateRange,
        text = "MyClass",
        destination = MyClassDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.ContactUs,
        icon = Icons.Filled.Call,
        text = "ContactUs",
        destination = ContactUsDestination
    ),
    NavigationItemContent(
        navDrawerType = NavDrawerType.AboutUs,
        icon = Icons.Filled.Star,
        text = "About Us",
        destination = AboutUsDestination
    )
)