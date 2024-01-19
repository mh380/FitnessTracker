package de.hdmstuttgart.trackmaster.newActivityScreen

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.hdmstuttgart.trackmaster.R

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val FINE_LOCATION_PERMISSION_CODE = 1
    private lateinit var googleMap: GoogleMap
    private var currentLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (checkLocationPermission()) {
            getLastLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            FINE_LOCATION_PERMISSION_CODE
        )
    }

    private fun getLastLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        currentLocation = it
                        initializeMap()
                    } ?: run {
                        showErrorMessage("Unable to get location. Please check your settings.")
                    }
                }
                .addOnFailureListener { e ->
                    showErrorMessage("Error getting location: ${e.localizedMessage}")
                }
        } else {
            requestLocationPermission()
        }
    }

    private fun initializeMap() {
        if (isAdded) {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@MapsFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val location = currentLocation?.let { LatLng(it.latitude, it.longitude) }
        location?.let {
            googleMap.addMarker(MarkerOptions().position(it).title("My Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 5f))

            googleMap.uiSettings.isRotateGesturesEnabled = true;
            googleMap.uiSettings.isRotateGesturesEnabled = true;



        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
