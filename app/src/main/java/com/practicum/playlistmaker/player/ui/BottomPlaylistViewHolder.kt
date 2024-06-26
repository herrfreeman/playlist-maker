package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist
import com.practicum.playlistmaker.medialibrary.playlists.domain.TrackCountString
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppSettings
import org.koin.java.KoinJavaComponent.getKoin
import java.io.File

class BottomPlaylistViewHolder(
    parent: ViewGroup,
    private val clickListener: BottomPlaylistRecyclerAdapter.ItemClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.playlist_bottom_item, parent, false)
) {

    private val appSettings: AppSettings = getKoin().get<SettingsInteractor>().getSettings()
    private val trackCounter: TrackCountString = getKoin().get()
    private val playlistImage: ImageView = itemView.findViewById(R.id.playlistImage)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val trackCount: TextView = itemView.findViewById(R.id.playlistTrackCount)

    fun bind(playlist: Playlist) {

        playlistName.text = playlist.name
        val countString = trackCounter.convertCount(playlist.trackCount)
        trackCount.text = "${playlist.trackCount} $countString"

        val imageDirectory = appSettings.imageDirectory
        if (playlist.coverFileName.isNotEmpty() and (imageDirectory != null)) {
            val file = File(imageDirectory, playlist.coverFileName)
            if (file.exists()) {
                //playlistImage.setImageURI(file.toUri()) Works too slow with full image files
                Glide.with(itemView)
                    .load(file.toUri())
                    .centerCrop()
                    .placeholder(R.drawable.track_placeholder_45)
                    //.transform(RoundedCorners(2))
                    .into(playlistImage)
            }
        }

        itemView.setOnClickListener {
            clickListener.onClick(playlist)
        }
    }
}

