package de.hdmstuttgart.trackmaster.homeScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.newActivityScreen.NewActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val faButton = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        faButton.setOnClickListener (
            fun(view: View) {
                val intent = Intent(activity, NewActivity::class.java)
                startActivity(intent)
            }
        )
    }
}