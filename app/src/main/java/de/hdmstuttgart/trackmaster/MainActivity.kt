package de.hdmstuttgart.trackmaster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //todo: remove test tracks //todo: inserting a track into the database has to happen whenever tracking is stopped
        val trackMasterApplication = this.application as TrackMasterApplication
        lifecycleScope.launch(Dispatchers.IO) {
            trackMasterApplication.repository.deleteAll()
            trackMasterApplication.repository.insert(Track(distance = 2, time = 20))
            trackMasterApplication.repository.insert(Track(distance = 5, time = 45))
            trackMasterApplication.repository.insert(Track(date = LocalDate.now().minusDays(1), distance = 12, time = 25))
            trackMasterApplication.repository.insert(Track(date = LocalDate.now().minusDays(10), distance = 12, time = 25))
            trackMasterApplication.repository.insert(Track(date = LocalDate.parse("2024-02-15"), distance = 15, time = 30))
        }


        val homeFragment = HomeFragment()
        val tracksFragment = TrackFragment()
        val settingsFragment = SettingsFragment()

        makeCurrentFragment(homeFragment)

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