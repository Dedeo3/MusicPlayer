package com.app.musicplayer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: List<SongResponse>
)

data class SongResponse(
    @SerializedName("trackId")
    val trackId: Long?,
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("trackName")
    val trackName: String?,
    @SerializedName("artistName")
    val artistName: String?,
    @SerializedName("collectionName")
    val collectionName: String?,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String?,
    @SerializedName("previewUrl")
    val previewUrl: String?,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long?
)
