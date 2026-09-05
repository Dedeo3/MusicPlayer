package com.app.musicplayer.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.musicplayer.data.remote.repository.IMusicRepository
import com.app.musicplayer.domain.Song
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

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()


    private var mediaPlayer: MediaPlayer? = null
    private var songList: List<Song> = emptyList()

    init {
        searchSongs("john")
    }

    fun searchSongs(keyword: String) {
        if (keyword.isBlank()) return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getSong(keyword)
                .onSuccess { songs ->
                    songList = songs
                    _uiState.value = UiState.Success(songs)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun playSong(song: Song) {
        mediaPlayer?.release()
        _duration.value = 0
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.previewUrl)
            setOnPreparedListener {
                start()
                _isPlaying.value = true
                _duration.value = this.duration
            }
            setOnCompletionListener { playNext() }
            prepareAsync()
        }
        _currentSong.value = song
    }

    fun playNext() {
        val current = _currentSong.value ?: return
        val index = songList.indexOf(current)
        if (index != -1 && index < songList.lastIndex) {
            playSong(songList[index + 1])
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            } else {
                it.start()
                _isPlaying.value = true
            }
        }
    }

    fun playPrevious() {
        val current = _currentSong.value ?: return
        val index = songList.indexOf(current)
        if (index > 0) {
            playSong(songList[index - 1])
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
    }
}