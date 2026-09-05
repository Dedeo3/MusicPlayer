package com.app.musicplayer.data.remote.service

import com.app.musicplayer.data.remote.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
        @GET("search")
        suspend fun getSongsByKeyword(
            @Query("term") term: String,
            @Query("media") media: String = "music",
            @Query("entity") entity: String = "song"
        ): SearchResponse
}