package com.example.quran.presentation.verse_screen

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quran.domain.model.Verse
import com.example.quran.domain.model.Verses
import com.example.quran.domain.use_case.GetVerses
import com.example.quran.presentation.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class VerseScreenViewModel @Inject constructor(
    val getVersesUseCase: GetVerses,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val TAG = "VerseScreenViewModel"

    private val _getVerseResponse = mutableStateOf<Response<Verses>>(Response.Loading(true))
    val getVerseResponse: State<Response<Verses>> = _getVerseResponse

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _currentItemIndex = mutableStateOf(0)
    val currentItemIndex: State<Int> = _currentItemIndex

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    private var chapterId: Int? = null
    private var reciterId: Int? = null

    init {
        chapterId = savedStateHandle.get<Int>("chapterId")
        reciterId = savedStateHandle.get<Int>("reciterId")
        getVerses(chapterId = chapterId!!, reciterId = reciterId!!)
    }

    fun onEvent(event: VerseScreenEvent) {
        when(event) {
            is VerseScreenEvent.PlayButtonClick -> {
                viewModelScope.launch {
                    _currentItemIndex.value = event.index
                    _isPlaying.value = true

                    _eventFlow.emit(UiEvent.PlayVerse(event.index, event.playAll))
                    _eventFlow.emit(UiEvent.CurrentPlayingItemChanged)
                }
            }
            is VerseScreenEvent.LoadNextPage -> {
                getVerseResponse.value.data?.pagination?.next_page?.let {
                    getVerses(chapterId = chapterId!!, reciterId = reciterId!!, it)
                }
            }

            is VerseScreenEvent.PauseButtonClick -> {
                _isPlaying.value = false
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Pause)
                }
            }

            is VerseScreenEvent.CurrentPlayingItemChanged -> {
                _currentItemIndex.value = event.index
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.CurrentPlayingItemChanged)
                }
            }
        }
    }

    private fun getVerses(chapterId: Int, reciterId: Int, pageNumber: Int = 1) {
        viewModelScope.launch {
            try {
                val response = getVersesUseCase(chapterId = chapterId, reciterId = reciterId, pageNumber = pageNumber)
                val data = response.body()

                data?.let {
                    if(pageNumber > 1 && _getVerseResponse.value is Response.Success) {
                            val verses = ArrayList<Verse>()
                            verses.addAll(getVerseResponse.value.data!!.verses)
                            verses.addAll(data.verses)
                            _getVerseResponse.value = Response.Success(
                                Verses(
                                    verses = verses.toList(),
                                    pagination = data.pagination
                                )
                            )
                    } else {
                        _getVerseResponse.value = Response.Success(data)
                    }
                    _eventFlow.emit(UiEvent.ChangeVersesPlaylist(data.verses))
                }
            } catch (e: HttpException) {
                Log.d(TAG, e.code().toString())
                Log.d(TAG, e.message())

                _getVerseResponse.value = Response.Error(message = e.message())
            } catch (e: IOException) {
                Log.d(TAG, e.toString())
                Log.d(TAG, e.message ?: "")

                _getVerseResponse.value = Response.Error(message = e.message ?: "")
            }
        }
    }

    sealed class UiEvent {
        data class PlayVerse(val index: Int, val playAll: Boolean): UiEvent()
        data class ChangeVersesPlaylist(val newList: List<Verse>): UiEvent()
        object CurrentPlayingItemChanged: UiEvent()
        object Pause: UiEvent()
    }
}