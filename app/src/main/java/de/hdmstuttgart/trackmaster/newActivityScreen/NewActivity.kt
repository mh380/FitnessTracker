package de.hdmstuttgart.trackmaster.newActivityScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.hdmstuttgart.trackmaster.R

class NewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
    }
}