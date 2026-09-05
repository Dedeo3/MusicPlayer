package com.app.musicplayer.data.remote.mapper

import com.app.musicplayer.data.remote.dto.SongResponse
import com.app.musicplayer.domain.Song

fun SongResponse.toSong(): Song = Song(
    id = trackId ?: 0L,
    title = trackName ?: "Unknown",
    artist = artistName ?: "Unknown",
    album = collectionName ?: "Unknown",
    coverUrl = artworkUrl100,
    previewUrl = previewUrl ?: "",
    durationMs = trackTimeMillis ?: 0L
)