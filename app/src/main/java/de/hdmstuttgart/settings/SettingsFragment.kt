package de.hdmstuttgart.settings

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import de.hdmstuttgart.trackmaster.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val darkLightSwitch = view.findViewById<SwitchMaterial>(R.id.darkLightSwitch)

        sharedPreferences = this.requireActivity().getPreferences(MODE_PRIVATE)

        // Status des SwitchMaterial aus den SharedPreferences wiederherstellen
        darkLightSwitch.isChecked = sharedPreferences.getBoolean("darkTheme", false)

        darkLightSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Theme basierend auf dem Switch-Status ändern
            this.requireActivity().setTheme(
                if (isChecked) {
                    R.style.Base_Theme_TrackMaster
                } else {
                    R.style.Theme_TrackMaster
                }
            )

            // Aktivität aktualisieren, um die Änderungen anzuwenden
            refreshFragment()

            // Status des SwitchMaterial in den SharedPreferences speichern
            sharedPreferences.edit().putBoolean("darkTheme", isChecked).apply()
        }
        }

    private fun refreshFragment() {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper, SettingsFragment()).commit()
    }
}