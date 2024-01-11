package de.hdmstuttgart.trackmaster.data

import androidx.annotation.WorkerThread
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class TrackRepository(private val trackDao: TrackDao) {

    fun getAllTracks(): List<Track> {
        return trackDao.getAllTracks()
    }

    /*fun getTracksFromWeek(date: LocalDate): List<Track> {
        // Get the start date of the current week (Monday)
        val startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        // Get the end date of the current week (Sunday)
        val endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return trackDao.getTracksFromWeek(startDate, endDate)
    }*/

    fun getTracksFromMonth(month: Month): List<Track> {
        //targetMonth has to be a two-digit-string
        var targetMonth= month.number
        return trackDao.getTracksFromMonth(targetMonth)
    }

    fun getTracksFromYear(year: String): List<Track> {
        //targetYear has to be a four-digit-string
        val targetYear = year
        return trackDao.getTracksFromYear(targetYear)
    }

    fun getMaxDistance(): Int {
        return trackDao.getMaxDistance()
    }

    fun getMaxTime(): Int {
        return trackDao.getMaxTime()
    }

    fun getMaxPace(): Float {
        return trackDao.getMaxPace()
    }

    @WorkerThread
    suspend fun insert(track: Track) {
        trackDao.insert(track)
    }

    @WorkerThread
    suspend fun delete(track: Track) {
        trackDao.delete(track)
    }

    @WorkerThread
    suspend fun deleteAll() {
        trackDao.deleteAll()
    }
}