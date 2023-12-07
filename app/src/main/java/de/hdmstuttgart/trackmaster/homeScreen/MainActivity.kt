package de.hdmstuttgart.trackmaster.homeScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import de.hdmstuttgart.trackmaster.newActivityScreen.NewActivity
import de.hdmstuttgart.trackmaster.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onFloatingActionButtonClick(view: View) {
        val intent = Intent(this, NewActivity::class.java)
        startActivity(intent)
    }
}