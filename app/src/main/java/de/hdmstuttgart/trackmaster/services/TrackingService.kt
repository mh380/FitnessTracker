package de.hdmstuttgart.trackmaster.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.utils.Constants
import de.hdmstuttgart.trackmaster.utils.Constants.ACTION_PAUSE_SERVICE
import de.hdmstuttgart.trackmaster.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import de.hdmstuttgart.trackmaster.utils.Constants.ACTION_STOP_SERVICE
import de.hdmstuttgart.trackmaster.utils.Constants.FASTEST_LOCATION_INTERVAL
import de.hdmstuttgart.trackmaster.utils.Constants.LOCATION_UPDATE_INTERVAL
import de.hdmstuttgart.trackmaster.utils.Constants.TIMER_UPDATE_INTERVAL
import de.hdmstuttgart.trackmaster.utils.TrackingUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
//import javax.inject.Inject
//import dagger.hilt.android.AndroidEntryPoint

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

//@AndroidEntryPoint
class TrackingService : LifecycleService() {

    var isFirstRun = true

    //@Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //Implementing the Stop Watch
    private val timeRunInSeconds = MutableLiveData<Long>()

    //@Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var curNotificationBuilder: NotificationCompat.Builder

    //Whenever the tracking state changes post the new value inside isTracking
    companion object {
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        curNotificationBuilder = baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Log.d("TrackingService", "Resumed service")
                        startTimer()
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Log.d("TrackingService", "Paused service")
                    pauseService()
                }

                ACTION_STOP_SERVICE -> {
                    Log.d("TrackingService", "Stopped service")
                }

                else -> {
                    Log.d("TrackingService", "Unknown action")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //Implementing the Stop Watch

    var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch{
            while(isTracking.value!!) {
                // time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted
                // post the new lapTime
                timeRunInMillis.postValue(timeRun + lapTime)
                if(timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    // Implementing the notification tracking state (damit updaten wir die Notification)
    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        curNotificationBuilder = baseNotificationBuilder
            .addAction(R.drawable.ic_launcher_foreground, notificationActionText, pendingIntent)
        notificationManager.notify(Constants.NOTIFICATION_ID, curNotificationBuilder.build())
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if (true) { //TrackingUtility.hasLocationPermissions(this)) { //todo: fix this to check the permissions
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            if(isTracking.value!!) {
                p0.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Log.d("TrackingService", "New Location: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location) {
        location.let {
            val position = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        // nicht mehr benötigt da wir jetzt Hilt verwenden!!!
        /*val notificationBuilder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("TrackMaster")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())*/



        startForeground(Constants.NOTIFICATION_ID, baseNotificationBuilder.build())
        timeRunInSeconds.observe(this, Observer {
            val notification = curNotificationBuilder
                .setContentText(TrackingUtility.getFormattedStopWatchTime(it * 1000L))
            notificationManager.notify(Constants.NOTIFICATION_ID, notification.build())
        })
    }

    // nicht mehr benötigt da wir jetzt Hilt verwenden!!!
    /*private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )*/

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}

