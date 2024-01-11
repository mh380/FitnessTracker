package de.hdmstuttgart.trackmaster.newActivityScreen

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import java.io.IOException

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val FINE_LOCATION_PERMISSION_CODE = 1
    private lateinit var googleMap: GoogleMap
    private var currentLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapSearchView: SearchView

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
        mapSearchView = view.findViewById(R.id.mapSearch)

        if (checkLocationPermission()) {
            getLastLocation()
        } else {
            requestLocationPermission()
        }


        mapSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location = mapSearchView.query.toString()
                var addressList: List<Address>? = null

                val geocoder = Geocoder(requireContext())

                try {
                    addressList = geocoder.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (!addressList.isNullOrEmpty()) {
                    val address = addressList[0]
                    val latLng = LatLng(address.latitude, address.longitude)

                    // Move the camera smoothly to the searched location
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1f))

                    // Add a marker for the searched location
                    googleMap.addMarker(MarkerOptions().position(latLng).title("Searched Location"))
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle the query text change if needed
                return false
            }
        });

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

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

    private fun initializeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapsFragment)
    }

    private fun getLastLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    currentLocation = it
                    initializeMap()
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Coordinates of the current location, change if needed
        val location = currentLocation?.let { LatLng(it.latitude, it.longitude) }
        location?.let {
            googleMap.addMarker(MarkerOptions().position(it).title("My Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
        }
    }
}
