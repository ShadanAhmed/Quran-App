package com.example.quran.presentation.home_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quran.domain.model.Chapter
import com.example.quran.presentation.home_screen.components.ChapterItem
import com.example.quran.presentation.home_screen.components.DefaultAppBar
import com.example.quran.presentation.home_screen.components.SearchAppBar
import com.example.quran.presentation.util.Response
import com.example.quran.presentation.util.Screen
import com.example.quran.presentation.util.SearchWidgetState
import com.example.quran.ui.theme.BlueLight
import com.example.quran.ui.theme.BlueDark
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state = viewModel.getChapterResponse.value
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var chapters: List<Chapter> by remember {
        mutableStateOf(emptyList())
    }

    chapters = if(viewModel.searchText.value.isEmpty()) {
        viewModel.getChapterResponse.value.data?.chapters ?: emptyList()
    } else {
        viewModel.getChapterResponse.value.data?.chapters?.filter {
            it.name_simple.lowercase().replace(" ", "").replace("-", "").contains(viewModel.searchText.value.lowercase().replace(" ", "").replace("-", ""))
        } ?: emptyList()
    }

    BackHandler(enabled = viewModel.searchWidgetState.value == SearchWidgetState.OPENED) {
        viewModel.onEvent(HomeScreenEvent.OnSearchStateChanged(SearchWidgetState.CLOSED))
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if(viewModel.searchWidgetState.value == SearchWidgetState.CLOSED) {
                DefaultAppBar(
                    onSearchClicked = {
                        viewModel.onEvent(HomeScreenEvent.OnSearchStateChanged(SearchWidgetState.OPENED))
                    }
                )
            } else {
                SearchAppBar(
                    text = viewModel.searchText.value,
                    onTextChange = {
                                   viewModel.onEvent(HomeScreenEvent.OnSearchTextChanged(it))
                    },
                    onCloseClicked = {
                                     viewModel.onEvent(HomeScreenEvent.OnSearchStateChanged(SearchWidgetState.CLOSED))
                    },
                    onSearchClicked = {
                        chapters = if(viewModel.searchText.value.isEmpty()) {
                            viewModel.getChapterResponse.value.data?.chapters ?: emptyList()
                        } else {
                            viewModel.getChapterResponse.value.data?.chapters?.filter {
                                it.name_simple.lowercase().replace(" ", "").replace("-", "").contains(viewModel.searchText.value.lowercase().replace(" ", "").replace("-", ""))
                            } ?: emptyList()
                        }
                    }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            if(state is Response.Loading) {
                CircularProgressIndicator(color = if(isSystemInDarkTheme()) BlueLight else BlueDark)
            }
            if(state is Response.Error) {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(state.message ?: "Some error has occurred, Please check your internet")
                }
            }
            if(state is Response.Success && state.data != null) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(chapters) { chapter ->
                        ChapterItem(
                            modifier = Modifier
                                .clickable {
                                   navController.navigate(
                                        Screen.VerseScreen.route +
                                                "/${chapter.id}/7/${chapter.name_simple}"
                                   )
                                },
                            chapter = chapter
                        )
                    }
                }
            }
        }
    }
}