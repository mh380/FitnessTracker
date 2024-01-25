package de.hdmstuttgart.trackmaster

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import de.hdmstuttgart.trackmaster.data.TrackDatabase
import de.hdmstuttgart.trackmaster.data.TrackRepository

class TrackMasterApplication : Application() {
    private val database by lazy { TrackDatabase.getDatabase(this) }
    val repository by lazy { TrackRepository(database.trackDao()) }
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}