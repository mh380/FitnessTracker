package de.hdmstuttgart.trackmaster.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrackDao {

    @Query("SELECT * FROM running_table ORDER BY date DESC")
    fun getAllTracksOrderedByDate(): List<Track>

    @Query("SELECT * FROM running_table WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    fun getTracksFromWeek(startDate: String, endDate: String): List<Track>

    @Query("SELECT * FROM running_table WHERE substr(date, 6, 2) = :month ORDER BY date")
    fun getTracksFromMonth(month: String): List<Track>

    @Query("SELECT * FROM running_table WHERE substr(date, 1, 4) = :year ORDER BY date")
    fun getTracksFromYear(year: String): List<Track>

    @Query("SELECT MAX (distanceInMeters) FROM running_table")
    fun getMaxDistance(): Float

    @Query("SELECT MAX (timeInMillis) FROM running_table")
    fun getMaxTime(): Long

    @Query("SELECT MAX (avgSpeedInKMH) FROM running_table")
    fun getMaxPace(): Float

    @Insert
    suspend fun insert(track: Track)

    @Delete
    suspend fun delete(track: Track)

    @Query("DELETE FROM running_table")
    suspend fun deleteAll()
}
