package com.app.musicplayer.ui.screen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.SeekBar
import com.app.musicplayer.databinding.ActivityMainBinding
import com.app.musicplayer.ui.adapter.SongAdapter
import com.app.musicplayer.ui.state.UiState
import com.app.musicplayer.ui.view_model.MusicViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MusicViewModel by viewModel()
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeUiState()
        observePlaybackState()
        setupSearch()
        setupPlayerControls()
    }

    private fun setupRecyclerView() {
        songAdapter = SongAdapter(
            songs = emptyList(),
            currentPlayingId = null,
            onItemClick = { song -> viewModel.playSong(song) }
        )
        binding.rvSongs.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = songAdapter
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressLoading.visibility = View.GONE
                    binding.rvSongs.visibility = View.GONE

                    when (state) {
                        is UiState.Loading -> {
                            binding.progressLoading.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.rvSongs.visibility = View.VISIBLE
                            songAdapter.updateSongs(state.songs)
                        }

                        is UiState.Error -> {
                            binding.tvEmptyState.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun observePlaybackState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentSong.collect { song ->
                        binding.playerBar.visibility = if (song != null) View.VISIBLE else View.GONE
                        songAdapter.updatePlayingId(song?.id)
                        song?.let {
                            binding.tvMiniTitle.text = it.title
                            binding.tvMiniArtist.text = it.artist
                            Glide.with(binding.ivMiniCover.context).load(it.coverUrl).into(binding.ivMiniCover)
                        }
                    }
                }
                launch {
                    viewModel.isPlaying.collect { isPlaying ->
                        binding.btnPlayPause.setImageResource(
                            if (isPlaying) android.R.drawable.ic_media_pause
                            else android.R.drawable.ic_media_play
                        )
                    }
                }
                launch {
                    viewModel.duration.collect { duration ->
                        binding.seekBarProgress.max = duration
                    }
                }
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.searchSongs(text.toString())
        }
    }

    private fun setupPlayerControls() {
        binding.btnPlayPause.setOnClickListener { viewModel.togglePlayPause() }
        binding.btnNext.setOnClickListener { viewModel.playNext() }
        binding.btnPrev.setOnClickListener { viewModel.playPrevious() }

        binding.seekBarProgress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}