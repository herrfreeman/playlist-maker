package com.practicum.playlistmaker.medialibrary.playlists.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.PlayListApplication
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import org.koin.java.KoinJavaComponent.getKoin

class PlaylistRecyclerAdapter(
    private val clickListener: ItemClickListener,
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    val playlists: MutableList<Playlist> = emptyList<Playlist>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(parent, clickListener)

    override fun getItemCount(): Int = playlists.count()

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun interface ItemClickListener {
        fun onClick(playlist: Playlist)
    }
}