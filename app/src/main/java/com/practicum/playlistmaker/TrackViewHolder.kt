package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.activity_track_item, parent, false)
) {

    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: TextView = itemView.findViewById(R.id.track_description)
    private val trackDuration: TextView = itemView.findViewById(R.id.track_duration)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(2))
            .into(trackImage)

        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackDuration.text = SimpleDateFormat(
            itemView.context.getString(R.string.track_duration_mask),
            Locale.getDefault()
        ).format(model.trackTimeMillis)
    }
}