package de.hdmstuttgart.trackmaster.trackScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.hdmstuttgart.trackmaster.R

class TrackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)
    }
}