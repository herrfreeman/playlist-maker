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

    @Query("SELECT * FROM tracks WHERE isFavorite = TRUE ORDER BY favoriteTimestamp DESC")
    fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT id FROM tracks WHERE isFavorite = TRUE")
    fun getFavoriteTracksId(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Query(
        """SELECT playlist.id AS id, playlist.name AS name, playlist.description AS description, playlist.coverfilename AS coverFileName, 
            COUNT(trackinlist.trackid) AS trackCount, SUM(tracks.trackTimeMillis) AS totalDuration
            FROM playlist 
            LEFT JOIN trackinlist ON trackinlist.playlistid = playlist.id
            LEFT JOIN tracks ON trackinlist.trackid = tracks.id
            WHERE NOT :selectbyid OR playlist.id = :playlistid 
            GROUP BY playlist.id, playlist.name, playlist.description, playlist.coverfilename"""
    )
    fun getPlaylists(playlistid: Int = -1, selectbyid: Boolean = false): List<Playlist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrackInPlaylist(trackInPlaylist: TrackInPlaylistEntity)

    @Delete(entity = TrackInPlaylistEntity::class)
    fun deleteTrackFromPlaylist(trackInPlaylist: TrackInPlaylistEntity)

    @Query("SELECT count(trackid) FROM trackinlist WHERE trackid = :trackid AND playlistid = :playlistid")
    fun checkTrackInPlaylist(trackid: String, playlistid: Int): Int

    @Query("SELECT * FROM tracks JOIN trackinlist ON tracks.id = trackinlist.trackid WHERE trackinlist.playlistid = :playlistid")
    fun getTracksInPlaylist(playlistid: Int): List<TrackEntity>

    @Query("DELETE FROM trackinlist WHERE trackinlist.playlistid = :playlistid")
    fun clearPlaylist(playlistid: Int)

    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM tracks LEFT JOIN trackinlist ON tracks.id = trackinlist.trackid WHERE trackinlist.trackid IS NULL AND NOT tracks.isFavorite")
    fun getAloneTracks(): List<TrackEntity>

    @Delete(entity = TrackEntity::class)
    fun deleteTrack(trackEntity: TrackEntity)

}