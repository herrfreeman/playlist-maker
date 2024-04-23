package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackSearchViewHolder(
    parent: ViewGroup,
    private val clickListener: TrackSearchAdapter.TrackClickListener
) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.track_item, parent, false)
) {

    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: TextView = itemView.findViewById(R.id.track_description)
    private val trackDuration: TextView = itemView.findViewById(R.id.track_duration)

    fun bind(track: Track) {

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.track_placeholder_45)
            .transform(RoundedCorners(2))
            .into(trackImage)

        trackName.text = track.trackName
        trackArtist.text = track.artistName
        trackArtist.requestLayout()
        trackDuration.text = SimpleDateFormat(
            itemView.context.getString(R.string.track_duration_mask),
            Locale.getDefault()
        ).format(track.trackTimeMillis)
        itemView.setOnClickListener{clickListener.onTrackClick(track)}
    }
}

