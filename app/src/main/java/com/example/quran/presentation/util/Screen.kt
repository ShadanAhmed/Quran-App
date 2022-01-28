package com.example.quran.presentation.util

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object VerseScreen: Screen("verse_screen")
}
