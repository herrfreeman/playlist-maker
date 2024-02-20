package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track

class TrackAdapter(val trackList: List<Track>, val trackClickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TrackViewHolder(parent, trackClickListener)
    override fun getItemCount(): Int = trackList.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

}