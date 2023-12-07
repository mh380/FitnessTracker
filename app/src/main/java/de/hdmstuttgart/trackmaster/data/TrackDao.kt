package de.hdmstuttgart.trackmaster.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrackDao {
    //dao = database access object
    @Query("SELECT * FROM track")
    fun getAllTracks(): List<Track>

    @Insert
    suspend fun insert(track: Track)

    @Delete
    suspend fun delete(track: Track)
}
