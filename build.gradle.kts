// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("lifecycle_version", "2.6.1")
        set("room_version", "2.5.2")
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.10"))
    }
}

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
