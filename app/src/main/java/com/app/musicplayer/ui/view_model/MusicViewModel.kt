package com.app.musicplayer.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.musicplayer.data.remote.repository.IMusicRepository
import com.app.musicplayer.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: IMusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun searchSongs(keyword: String) {
        viewModelScope.launch {
            if (keyword.isBlank()){
                _uiState.value= UiState.Idle
            }
            _uiState.value = UiState.Loading
            repository.getSong(keyword)
                .onSuccess { songs ->
                    _uiState.value = UiState.Success(songs)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}