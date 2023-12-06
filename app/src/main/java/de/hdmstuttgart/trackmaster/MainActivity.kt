package de.hdmstuttgart.trackmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

// test karin

class MainActivity : AppCompatActivity() {

    private var counter = 0     //var -> mehrfache Wertzuweisung möglich; val -> nur eine Wertzuweisung/ Konstante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun onClick(view: View) {
        counter++

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "Clicked $counter times"

        if (counter >= 5) {
            //start new activity
            startActivity(Intent(this, NewActivity::class.java))
        }
    }
}