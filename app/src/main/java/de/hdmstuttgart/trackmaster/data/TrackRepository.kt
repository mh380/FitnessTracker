package de.hdmstuttgart.trackmaster.data

import androidx.annotation.WorkerThread

class TrackRepository(private val trackDao: TrackDao) {


    fun getAllTracks(): List<Track> {
        return trackDao.getAllTracks()
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