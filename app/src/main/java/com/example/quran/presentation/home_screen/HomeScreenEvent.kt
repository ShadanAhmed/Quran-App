package com.example.quran.presentation.home_screen

import com.example.quran.presentation.util.SearchWidgetState

sealed class HomeScreenEvent {
    data class OnSearchTextChanged(val text: String): HomeScreenEvent()
    data class OnSearchStateChanged(val state: SearchWidgetState): HomeScreenEvent()
}
