package de.hdmstuttgart.trackmaster.data

import androidx.annotation.WorkerThread
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year
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
        var targetMonth: String
        if(month.value < 10) {
            targetMonth = "0" + month.value
        } else {
            targetMonth = month.value.toString()
        }
        return trackDao.getTracksFromMonth(targetMonth)
    }

    fun getTracksFromYear(year: Year): List<Track> {
        //targetYear has to be a four-digit-string
        val targetYear = year.toString()
        return trackDao.getTracksFromYear(targetYear)
    }

    @WorkerThread
    suspend fun insert(track: Track) {
        trackDao.insert(track)
    }

    @WorkerThread
    suspend fun delete(track: Track) {
        trackDao.delete(track)
    }
}