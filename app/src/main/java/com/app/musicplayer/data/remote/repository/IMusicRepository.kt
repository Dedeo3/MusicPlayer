package com.app.musicplayer.data.remote.repository

import com.app.musicplayer.domain.Song

interface IMusicRepository {
    suspend fun getSong(keyword:String): Result<List<Song>>
}