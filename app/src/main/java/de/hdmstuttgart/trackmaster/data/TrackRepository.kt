package de.hdmstuttgart.trackmaster.data

import androidx.annotation.WorkerThread
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.TemporalAdjusters

class TrackRepository(private val trackDao: TrackDao) {

    fun getAllTracks(): List<Track> {
        return trackDao.getAllTracks()
    }

    fun getTracksFromWeek(startDate: LocalDate, endDate: LocalDate): List<Track> {
        return trackDao.getTracksFromWeek(startDate.toString(), endDate.toString())
    }

    fun getTracksFromMonth(month: Month): List<Track> {
        //targetMonth has to be a two-digit-string
        val targetMonth: String
        if(month.value < 10) {
           targetMonth = "0" + month.value
        } else {
            targetMonth= month.value.toString()
        }
        return trackDao.getTracksFromMonth(targetMonth)
    }

    fun getTracksFromYear(targetYear: String): List<Track> {
        //targetYear has to be a four-digit-string
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