package com.app.musicplayer.ui.state

import com.app.musicplayer.domain.Song

sealed interface UiState {
    object Loading : UiState
    data class Success(val songs: List<Song>) : UiState
    data class Error(val message: String) : UiState
}