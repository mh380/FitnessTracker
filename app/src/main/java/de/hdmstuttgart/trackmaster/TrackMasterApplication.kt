package de.hdmstuttgart.trackmaster

import android.app.Application
import de.hdmstuttgart.trackmaster.data.TrackDatabase
import de.hdmstuttgart.trackmaster.data.TrackRepository

class TrackMasterApplication : Application() {
    private val database by lazy { TrackDatabase.getDatabase(this) }
    val repository by lazy { TrackRepository(database.trackDao()) }
}