package de.hdmstuttgart.trackmaster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdmstuttgart.trackmaster.data.Track
import de.hdmstuttgart.trackmaster.settings.SettingsFragment
import de.hdmstuttgart.trackmaster.homeScreen.HomeFragment
import de.hdmstuttgart.trackmaster.trackScreen.TrackFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //using correct mode when app is started
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        when (sharedPreferences.getBoolean("darkTheme", false)) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //example data for demonstration purpose
        val trackMasterApplication = this.application as TrackMasterApplication
        lifecycleScope.launch(Dispatchers.IO) {
            if (trackMasterApplication.repository.getAllTracksOrderedByDate().isEmpty()) { //only inserted if repository is empty
                trackMasterApplication.repository.insert(
                    Track(
                        distanceInMeters = 2000.7f,
                        timeInMillis = 1200000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        distanceInMeters = 5000f,
                        timeInMillis = 2700000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        date = LocalDate.now().minusDays(1),
                        distanceInMeters = 12000f,
                        timeInMillis = 1500000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        date = LocalDate.now().minusDays(2),
                        distanceInMeters = 4000f,
                        timeInMillis = 900000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        date = LocalDate.now().minusDays(3),
                        distanceInMeters = 5000f,
                        timeInMillis = 1020000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        date = LocalDate.now().minusDays(4),
                        distanceInMeters = 10000f,
                        timeInMillis = 1260000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        date = LocalDate.now().minusDays(5),
                        distanceInMeters = 13000f,
                        timeInMillis = 1680000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        date = LocalDate.now().minusDays(10),
                        distanceInMeters = 8000f,
                        timeInMillis = 1200000L
                    )
                )
                trackMasterApplication.repository.insert(
                    Track(
                        date = LocalDate.parse("2024-01-15"),
                        distanceInMeters = 15000f,
                        timeInMillis = 1800000L
                    )
                )
            }
        }


        val homeFragment = HomeFragment()
        val tracksFragment = TrackFragment()
        val settingsFragment = SettingsFragment()

        if (sharedPreferences.getBoolean("settings", false)) {
            makeCurrentFragment(settingsFragment)
            sharedPreferences.edit().putBoolean("settings", false).apply()
        } else {
            makeCurrentFragment(homeFragment)
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_tracks -> makeCurrentFragment(tracksFragment)
                R.id.ic_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}