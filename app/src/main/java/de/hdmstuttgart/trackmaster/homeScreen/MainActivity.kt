package de.hdmstuttgart.trackmaster.homeScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import de.hdmstuttgart.settings.SettingsActivity
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.newActivityScreen.NewActivity
import de.hdmstuttgart.trackmaster.trackScreen.TrackActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onFloatingActionButtonClick(view: View) {
        val intent = Intent(this, NewActivity::class.java)
        startActivity(intent)
    }

    fun onSettingsButtonClick (view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun onTrackButtonClick (view: View) {
        val intent = Intent(this, TrackActivity::class.java)
        startActivity(intent)
    }
}