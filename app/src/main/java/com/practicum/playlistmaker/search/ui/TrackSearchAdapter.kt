package com.practicum.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.models.Track

class TrackSearchAdapter(private val trackClickListener: TrackClickListener) : RecyclerView.Adapter<TrackSearchViewHolder>() {

    val trackList: MutableList<Track> = emptyList<Track>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackSearchViewHolder =
        TrackSearchViewHolder(parent, trackClickListener)

    override fun getItemCount(): Int = trackList.count()

    override fun onBindViewHolder(holder: TrackSearchViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    interface TrackClickListener {
        fun onTrackClick(track: Track)
        //fun onFavoriteToggleClick(movie: Movie)
    }
}