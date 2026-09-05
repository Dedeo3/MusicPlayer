package com.app.musicplayer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.musicplayer.R
import com.app.musicplayer.databinding.ItemSongBinding
import com.app.musicplayer.domain.Song
import com.bumptech.glide.Glide

class SongAdapter(
    private var songs: List<Song>,
    private var currentPlayingId: Long?,
    private val onItemClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.tvTitle.text = song.title
            binding.tvArtist.text = song.artist
            Glide.with(binding.ivCover.context)
                .load(song.coverUrl)
                .placeholder(R.drawable.bg_item_default)
                .error(R.drawable.bg_item_default)
                .into(binding.ivCover)

            binding.itemRoot.setBackgroundResource(
                if (song.id == currentPlayingId) R.drawable.bg_item_selected
                else R.drawable.bg_item_default
            )

            binding.root.setOnClickListener { onItemClick(song) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlayingId(id: Long?) {
        currentPlayingId = id
        notifyDataSetChanged()
    }
}