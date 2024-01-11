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

    /*@Query("SELECT * FROM track WHERE toLocalDate(date) BETWEEN :startDate AND :endDate")
    fun getTracksFromWeek(startDate: LocalDate, endDate: LocalDate): List<Track> */

    @Query("SELECT * FROM track WHERE SUBSTRING(date, 6, 2)=:month")
    fun getTracksFromMonth(month: String): List<Track>

    @Query("SELECT * FROM track WHERE SUBSTRING(date, 1, 4)=:year")
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
