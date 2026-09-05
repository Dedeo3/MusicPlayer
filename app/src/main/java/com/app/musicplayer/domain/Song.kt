package com.app.musicplayer.domain

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val coverUrl: String?,
    val previewUrl: String,
    val durationMs: Long
)
