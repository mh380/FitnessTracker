package de.hdmstuttgart.trackmaster.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface TrackDao {
    //dao = database access object

    @Query("SELECT * FROM track")
    fun getAllTracks(): List<Track>

    @Query("SELECT * FROM track WHERE date BETWEEN :startDate AND :endDate ORDER BY DATE")
    fun getTracksFromWeek(startDate: String, endDate: String): List<Track>

    //@Query("SELECT * FROM track WHERE substr(date, 6, 2)=:month")
    @Query("SELECT * FROM track WHERE substr(date, 6, 2) = :month ORDER BY date")
    fun getTracksFromMonth(month: String): List<Track>

    @Query("SELECT * FROM track WHERE substr(date, 1, 4) = :year ORDER BY date")
    fun getTracksFromYear(year: String): List<Track>

    @Query("SELECT MAX (distance) FROM track")
    fun getMaxDistance(): Int

    @Query("SELECT MAX (time) FROM track")
    fun getMaxTime(): Int

    @Query("SELECT MAX (pace) FROM track")
    fun getMaxPace(): Float

    @Insert
    suspend fun insert(track: Track)

    @Delete
    suspend fun delete(track: Track)

    @Query("DELETE FROM track")
    suspend fun deleteAll()
}
