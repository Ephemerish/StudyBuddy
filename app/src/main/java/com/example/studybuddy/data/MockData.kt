package com.example.studybuddy.data

import androidx.annotation.DrawableRes
import com.example.studybuddy.R
import com.example.studybuddy.ui.navigation.NavigationDestination


data class FeatureCourseContent(
    val courseName: String,
    @DrawableRes val image: Int = R.drawable.ic_launcher_background,
)

val FeatureCourseContentList = listOf(
    FeatureCourseContent(
        courseName = "mock1",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock2",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock3",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock4",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock5",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock6",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock7",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock8",
        image = R.drawable.ic_launcher_background,
    ),
    FeatureCourseContent(
        courseName = "mock9",
        image = R.drawable.ic_launcher_background,
    ),
)