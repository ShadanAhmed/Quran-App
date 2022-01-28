package com.example.quran.presentation.verse_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.presentation.util.Response
import com.example.quran.presentation.verse_screen.components.DefaultAppBar
import com.example.quran.presentation.verse_screen.components.VerseItem
import com.example.quran.ui.theme.BlueDark
import com.example.quran.ui.theme.BlueLight
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.google.android.exoplayer2.Player.STATE_IDLE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun VerseScreen(
    navController: NavController,
    viewModel: VerseScreenViewModel = hiltViewModel(),
    chapterName: String
) {
    val verseState by viewModel.getVerseResponse
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var listener: Player.Listener? = null

    val context = LocalContext.current

    var versePlayer: ExoPlayer? = ExoPlayer.Builder(context).build()

    DisposableEffect(key1 = LocalLifecycleOwner.current.lifecycle) {
        onDispose {
            versePlayer?.stop()
            versePlayer?.release()
            listener?.let {
                versePlayer?.removeListener(it)
            }
            versePlayer = null
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is VerseScreenViewModel.UiEvent.PlayVerse -> {
                    listener?.let {
                        versePlayer?.removeListener(it)
                    }

                    versePlayer?.seekTo(event.index, C.TIME_UNSET)
                    versePlayer?.pauseAtEndOfMediaItems = !event.playAll

                    listener = object: Player.Listener {
                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            super.onMediaItemTransition(mediaItem, reason)

                            viewModel.onEvent(VerseScreenEvent.CurrentPlayingItemChanged(viewModel.currentItemIndex.value + 1))
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)

                            if(playbackState == STATE_ENDED || playbackState == STATE_IDLE) {
                                viewModel.onEvent(VerseScreenEvent.PauseButtonClick)
                            }
                        }

                        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                            super.onPlayWhenReadyChanged(playWhenReady, reason)
                            if(!playWhenReady) {
                                viewModel.onEvent(VerseScreenEvent.PauseButtonClick)
                            }
                        }
                    }

                    versePlayer?.addListener(listener!!)

                    versePlayer?.playWhenReady = true
                }

                is VerseScreenViewModel.UiEvent.ChangeVersesPlaylist -> {
                    for(verse in event.newList) {
                        val mediaItem: MediaItem = MediaItem.fromUri("https://verses.quran.com/${verse.audio.url}".toUri())
                        versePlayer?.addMediaItem(mediaItem)
                    }

                    versePlayer?.prepare()
                }

                is VerseScreenViewModel.UiEvent.CurrentPlayingItemChanged -> {
                    Log.d("VerseScreen", viewModel.currentItemIndex.value.toString())
                    listState.animateScrollToItem(viewModel.currentItemIndex.value)
                }

                is VerseScreenViewModel.UiEvent.Pause -> {
                    versePlayer?.pause()
                    Log.d("VerseScreen", versePlayer?.playWhenReady.toString())
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            DefaultAppBar(
                title = chapterName,
                onBackPressed = {
                    navController.navigateUp()
                },
            )
        },
        bottomBar = {
            BottomAppBar(backgroundColor = MaterialTheme.colors.background) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if(viewModel.currentItemIndex.value > 0) {
                            viewModel.onEvent(event = VerseScreenEvent.PlayButtonClick(viewModel.currentItemIndex.value - 1, true))
                        }
                    }) {
                        Icon(imageVector = Icons.Rounded.SkipPrevious, contentDescription = "Skip to previous")
                    }
                    IconButton(onClick = {
                        if(!viewModel.isPlaying.value)
                            viewModel.onEvent(event = VerseScreenEvent.PlayButtonClick(viewModel.currentItemIndex.value, true))
                        else {
                            versePlayer?.stop()
                            versePlayer?.release()
                            listener?.let {
                                versePlayer?.removeListener(it)
                            }
                            versePlayer?.prepare()
                            viewModel.onEvent(event = VerseScreenEvent.PauseButtonClick)
                        }
                    }) {
                        Icon(imageVector = if(viewModel.isPlaying.value) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = "Play/Pause")
                    }
                    IconButton(onClick = {
                        viewModel.getVerseResponse.value.data?.verses?.let {
                            if (viewModel.currentItemIndex.value < it.size - 1) {
                                viewModel.onEvent(event = VerseScreenEvent.PlayButtonClick(viewModel.currentItemIndex.value + 1, true))
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Rounded.SkipNext, contentDescription = "Skip to next")
                    }
                }
            }
        }
    ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (verseState is Response.Loading) {
                    CircularProgressIndicator(color = if (isSystemInDarkTheme()) BlueLight else BlueDark)
                }
                if (verseState is Response.Error) {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            verseState.message ?: "Some error has occurred, Please check your internet"
                        )
                    }
                }
                if (verseState is Response.Success && verseState.data != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                        itemsIndexed(verseState.data!!.verses) { index, verse ->
                            if(index == verseState.data!!.verses.size - 1 && verseState.data!!.pagination.current_page < verseState.data!!.pagination.total_pages) {
                                Log.d("Verse", verseState.data!!.pagination.toString())
                                viewModel.onEvent(VerseScreenEvent.LoadNextPage)
                            }

                            VerseItem(verse = verse, currentPlayingItemIndex = if(viewModel.isPlaying.value) viewModel.currentItemIndex.value else -1) {
                                viewModel.onEvent(VerseScreenEvent.PlayButtonClick(index))
                            }
                        }
                    }
                }
            }
        }
}