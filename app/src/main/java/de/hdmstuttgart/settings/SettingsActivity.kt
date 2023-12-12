package de.hdmstuttgart.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import de.hdmstuttgart.trackmaster.R


class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val darkLightSwitch = this.findViewById<SwitchMaterial>(R.id.darkLightSwitch)

        sharedPreferences = getPreferences(MODE_PRIVATE)

        // Status des SwitchMaterial aus den SharedPreferences wiederherstellen
        darkLightSwitch.isChecked = sharedPreferences.getBoolean("darkTheme", false)

        darkLightSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Theme basierend auf dem Switch-Status ändern
            setTheme(if (isChecked) {
                R.style.Base_Theme_TrackMaster
            } else {
                R.style.Theme_TrackMaster
            })

            // Aktivität aktualisieren, um die Änderungen anzuwenden
            recreate()

            // Status des SwitchMaterial in den SharedPreferences speichern
            sharedPreferences.edit().putBoolean("darkTheme", isChecked).apply()
        }
    }
}