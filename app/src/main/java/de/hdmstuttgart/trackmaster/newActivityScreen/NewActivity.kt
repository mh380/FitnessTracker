package de.hdmstuttgart.trackmaster.newActivityScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.hdmstuttgart.trackmaster.R

class NewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapsFragment())
                .commit()
        }
    }
}
