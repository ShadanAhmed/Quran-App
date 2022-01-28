package com.example.quran.presentation.home_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.domain.model.Chapter
import com.example.quran.domain.model.Chapters
import com.example.quran.domain.use_case.GetChapters
import com.example.quran.presentation.util.Response
import com.example.quran.presentation.util.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val getChaptersUseCase: GetChapters
): ViewModel() {
    private val TAG = "HomeScreenViewModel"

    private val _searchText = mutableStateOf("")
    val searchText: State<String> = _searchText

    private val _searchWidgetState = mutableStateOf(SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _getChapterResponse = mutableStateOf<Response<Chapters>>(Response.Loading(true))
    val getChapterResponse: State<Response<Chapters>> = _getChapterResponse

    init {
        getChapters()
    }

    private fun getChapters() {
        viewModelScope.launch {
            try {
                val response = getChaptersUseCase()
                val data = response.body()

                _getChapterResponse.value = Response.Success(data!!)
            } catch (e: HttpException) {
                Log.d(TAG, e.code().toString())
                Log.d(TAG, e.message())

                _getChapterResponse.value = Response.Error(message = e.message())
            } catch (e: IOException) {
                Log.d(TAG, e.toString())
                Log.d(TAG, e.message ?: "")

                _getChapterResponse.value = Response.Error(message = e.message ?: "")
            }
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when(event) {
            is HomeScreenEvent.OnSearchStateChanged -> {
                _searchWidgetState.value = event.state
            }
            is HomeScreenEvent.OnSearchTextChanged -> {
                _searchText.value = event.text
            }
        }
    }
}