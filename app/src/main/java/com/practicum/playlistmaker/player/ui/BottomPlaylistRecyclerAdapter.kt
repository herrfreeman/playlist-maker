package com.practicum.playlistmaker.player.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.PlayListApplication
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import org.koin.java.KoinJavaComponent.getKoin

class BottomPlaylistRecyclerAdapter(
    private val clickListener: ItemClickListener,
) : RecyclerView.Adapter<BottomPlaylistViewHolder>() {

    val playlists: MutableList<Playlist> = emptyList<Playlist>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomPlaylistViewHolder =
        BottomPlaylistViewHolder(parent, clickListener)

    override fun getItemCount(): Int = playlists.count()

    override fun onBindViewHolder(holder: BottomPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun interface ItemClickListener {
        fun onClick(playlist: Playlist)
    }
}