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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteTrack(favoriteTrack: TrackInFavorites)

    @Delete
    fun deleteFavoriteTrack(favoriteTrack: TrackInFavorites)

    @Query("SELECT * FROM tracks JOIN trackinfavorites ON tracks.id = trackinfavorites.trackid ORDER BY trackinfavorites.timestamp DESC")
    fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackid FROM trackinfavorites")
    fun getFavoriteTracksId(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Query("""SELECT id, name, description, coverfilename AS coverFileName, COUNT(trackinlist.trackid) AS trackCount  
            FROM playlist 
            LEFT JOIN trackinlist ON trackinlist.playlistid = playlist.id 
            GROUP BY id, name, description, coverfilename""")
    fun getPlaylists(): List<Playlist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrackInPlaylist(trackInPlaylist: TrackInPlaylistEntity)

    @Query("SELECT count(trackid) FROM trackinlist WHERE trackid = :trackid AND playlistid = :playlistid")
    fun checkTrackInPlaylist(trackid: String, playlistid: Int): Int

    @Query("SELECT * FROM tracks JOIN trackinlist ON tracks.id = trackinlist.trackid WHERE trackinlist.playlistid = :playlistid")
    fun getTracksInPlaylist(playlistid: Int): List<TrackEntity>
}