package de.hdmstuttgart.trackmaster.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate
import java.time.Month

@Dao
interface TrackDao {
    //dao = database access object
    @Query("SELECT * FROM track")
    fun getAllTracks(): List<Track>

    @Query("SELECT * FROM track WHERE toLocalDate(date) BETWEEN :startDate AND :endDate")
    fun getTracksFromWeek(startDate: LocalDate, endDate: LocalDate): List<Track>

    @Query("SELECT * FROM track WHERE strftime('%m', toLocalDate(date))=:month")
    fun getTracksFromMonth(month: String): List<Track>

    @Query("SELECT * FROM track WHERE strftime('%Y', toLocalDate(date))=:year")
    fun getTracksFromYear(year: String): List<Track>

    @Insert
    suspend fun insert(track: Track)

    @Delete
    suspend fun delete(track: Track)
}
