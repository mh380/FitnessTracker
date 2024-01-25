package de.hdmstuttgart.trackmaster.newActivityScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.newActivityScreen.BackgroundLocationTracking.LocationService

class NewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        val startButton: Button = findViewById(R.id.buttonStart)
        val stopButton: Button = findViewById(R.id.buttonStop)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapsFragment())
                .commit()

            startButton.setOnClickListener {
                // Starten Sie den LocationService mit der Aktion ACTION_START
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                }.also {
                    startService(it)
                }
            }

            stopButton.setOnClickListener {
                // Starten Sie den LocationService mit der Aktion ACTION_STOP
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                }.also {
                    startService(it)
                }
            }
        }
    }
}
