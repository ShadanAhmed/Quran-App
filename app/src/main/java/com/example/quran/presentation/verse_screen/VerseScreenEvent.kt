package com.example.quran.presentation.verse_screen

import android.net.Uri

sealed class VerseScreenEvent {
    class PlayButtonClick(val index: Int, val playAll: Boolean = false): VerseScreenEvent()
    class CurrentPlayingItemChanged(val index: Int): VerseScreenEvent()
    object PauseButtonClick: VerseScreenEvent()
    object LoadNextPage: VerseScreenEvent()
}