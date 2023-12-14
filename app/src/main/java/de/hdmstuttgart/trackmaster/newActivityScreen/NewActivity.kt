package de.hdmstuttgart.trackmaster.newActivityScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.hdmstuttgart.trackmaster.R

class NewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {

        googleMap = p0

        //Coordinates of a specific location you can change if you want another location
        val location = LatLng(48.7823200, 9.1770200);
        googleMap.addMarker(
            MarkerOptions().position(location).title("Marker in Stuttgart")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }
}