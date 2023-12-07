package de.hdmstuttgart.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import de.hdmstuttgart.trackmaster.R


class SettingsActivity : AppCompatActivity() {

    fun onCreate(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // todo: toggle between light and dark mode, not working yet
        val darkLightSwitch = view.findViewById<SwitchMaterial>(R.id.darkLightSwitch)

        darkLightSwitch.isChecked = true


    }
}