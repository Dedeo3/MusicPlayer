package com.app.musicplayer.data.remote.repository

import com.app.musicplayer.data.remote.dto.SearchResponse
import com.app.musicplayer.data.remote.dto.SongResponse
import com.app.musicplayer.data.remote.service.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MusicRepositoryImpTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: MusicRepositoryImp

    @Before
    fun setup() {
        apiService = mockk()
        repository = MusicRepositoryImp(apiService)
    }

    @Test
    fun `getSong success returns mapped list of Song`() = runTest {
        val fakeResponse = SearchResponse(
            resultCount = 1,
            results = listOf(
                SongResponse(
                    trackId = 1L,
                    kind = "song",
                    trackName = "Upside Down",
                    artistName = "Jack Johnson",
                    collectionName = "Curious George",
                    artworkUrl100 = "http://example.com/art.jpg",
                    previewUrl = "http://example.com/preview.m4a",
                    trackTimeMillis = 210000L
                )
            )
        )
        coEvery { apiService.getSongsByKeyword("jack") } returns fakeResponse

        val result = repository.getSong("jack")

        assertTrue(result.isSuccess)
        val songs = result.getOrNull()
        assertEquals(1, songs?.size)
        assertEquals("Upside Down", songs?.first()?.title)
        assertEquals("Jack Johnson", songs?.first()?.artist)
    }

    @Test
    fun `getSong failure returns Result failure on exception`() = runTest {
        coEvery { apiService.getSongsByKeyword(any()) } throws IOException("No internet")

        val result = repository.getSong("jack")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }
}