package com.example.quran.presentation.verse_screen.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.More
import androidx.compose.runtime.Composable

@Composable
fun DefaultAppBar(title: String, onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = title, color = MaterialTheme.colors.secondary)
        },
        navigationIcon = {
           IconButton(onClick = onBackPressed) {
               Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button", tint = MaterialTheme.colors.secondary)
           }
        }
    )
}