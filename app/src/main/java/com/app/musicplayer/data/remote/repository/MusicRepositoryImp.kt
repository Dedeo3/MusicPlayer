package com.app.musicplayer.data.remote.repository

import com.app.musicplayer.data.remote.mapper.toSong
import com.app.musicplayer.data.remote.service.ApiService
import com.app.musicplayer.domain.Song

class MusicRepositoryImp(val apiService: ApiService): IMusicRepository {
    override suspend fun getSong(keyword: String): Result<List<Song>> {
        return try {
            val response= apiService.getSongsByKeyword(keyword)
            Result.success(response.results.map{
                it.toSong()
            })
        }catch (e: Error){
            Result.failure(e)
        }
    }
}