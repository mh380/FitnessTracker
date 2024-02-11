package de.hdmstuttgart.trackmaster.homeScreen

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.newActivityScreen.NewActivity
import de.hdmstuttgart.trackmaster.utils.Constants
import de.hdmstuttgart.trackmaster.utils.TrackingUtility

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()


        val faButton = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        faButton.setOnClickListener (
            fun(view: View) {
                val intent = Intent(activity, NewActivity::class.java)
                startActivity(intent)
            }
        )
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }

        val permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            Constants.REQUEST_CODE_LOCATION_PERMISSION
        )
    }
}