package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.medialibrary.playlists.domain.Playlist

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Delete
    fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY timestamp DESC")
    fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM favorite_tracks")
    fun getTracksId(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT id, name, description, coverfilename AS coverFileName, 3 AS trackCount FROM playlist")
    fun getPlaylists(): List<Playlist>

}