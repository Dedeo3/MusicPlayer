package com.app.musicplayer.ui.view_model

import com.app.musicplayer.data.remote.repository.IMusicRepository
import com.app.musicplayer.domain.Song
import com.app.musicplayer.ui.state.UiState
import com.app.musicplayer.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MusicViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: IMusicRepository
    private lateinit var viewModel: MusicViewModel

    private val dummySongs = listOf(
        Song(1L, "Upside Down", "Jack Johnson", "Curious George", null, "url", 210000L)
    )

    @Before
    fun setup() {
        repository = mockk()
        coEvery { repository.getSong("john") } returns Result.success(dummySongs)
    }

    @Test
    fun `searchSongs success updates uiState to Success`() = runTest {
        coEvery { repository.getSong("jack") } returns Result.success(dummySongs)
        viewModel = MusicViewModel(repository)

        viewModel.searchSongs("jack")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(1, (state as UiState.Success).songs.size)
    }

    @Test
    fun `searchSongs failure updates uiState to Error`() = runTest {
        coEvery { repository.getSong("jack") } returns Result.failure(Exception("Network error"))
        viewModel = MusicViewModel(repository)

        viewModel.searchSongs("jack")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Network error", (state as UiState.Error).message)
    }

    @Test
    fun `searchSongs with blank keyword does nothing`() = runTest {
        viewModel = MusicViewModel(repository)
        advanceUntilIdle()
        val stateBefore = viewModel.uiState.value

        viewModel.searchSongs("")
        advanceUntilIdle()

        assertEquals(stateBefore, viewModel.uiState.value)
    }
}