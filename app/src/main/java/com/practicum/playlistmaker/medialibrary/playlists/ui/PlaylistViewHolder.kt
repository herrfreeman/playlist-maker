package com.practicum.playlistmaker.medialibrary.playlists.ui

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

class PlaylistViewHolder(
    parent: ViewGroup,
    private val clickListener: PlaylistRecyclerAdapter.ItemClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.playlist_item, parent, false)
) {

    private val appSettings: AppSettings = getKoin().get<SettingsInteractor>().getSettings()
    private val trackCounter: TrackCountString = getKoin().get()
    private val playlistImage: ImageView = itemView.findViewById(R.id.playlistImage)
    private val trackCount: TextView = itemView.findViewById(R.id.playlistTrackCount)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)


    fun bind(playlist: Playlist) {

        playlistName.text = playlist.name
        val countString = trackCounter.convertCount(playlist.trackCount)
        trackCount.text = "${playlist.trackCount} $countString"

        val imageDirectory = appSettings.imageDirectory
        if (playlist.coverFileName.isNotEmpty() and (imageDirectory != null)) {
            val file = File(imageDirectory, playlist.coverFileName)
            if (file.exists()) {
                //playlistImage.setImageURI(file.toUri()) //Works too slow with full image files
                Glide.with(itemView)
                    .load(file.toUri())
                    .centerCrop()
                    .placeholder(R.drawable.track_placeholder_45)
                    //.transform(RoundedCorners(2))
                    .into(playlistImage)
            }
        } else {
            playlistImage.setImageResource(R.drawable.track_placeholder_45)
        }

        itemView.setOnClickListener {
            clickListener.onClick(playlist)
        }
    }
}

