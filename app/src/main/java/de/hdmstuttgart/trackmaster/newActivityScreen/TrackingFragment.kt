package de.hdmstuttgart.trackmaster.newActivityScreen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.TrackMasterApplication
import de.hdmstuttgart.trackmaster.data.Track
import de.hdmstuttgart.trackmaster.services.TrackingService
import de.hdmstuttgart.trackmaster.utils.Constants
import de.hdmstuttgart.trackmaster.utils.Constants.ACTION_STOP_SERVICE
import de.hdmstuttgart.trackmaster.utils.TrackingUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TrackingFragment : Fragment() {

    private var isTracking = false
    private var pathPoints = mutableListOf<MutableList<LatLng>>()
    private var map: GoogleMap? = null
    private lateinit var mapView: MapView
    private var curTimeInMillis = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        val btnToggleRun = view.findViewById<View>(R.id.btnToggleRun)
        btnToggleRun.setOnClickListener {
            toggleRun()
        }
        val btnFinishRun = requireView().findViewById<Button>(R.id.btnFinishRun)
        btnFinishRun.setOnClickListener {
            endRunAndSaveToDB()
        }

        mapView.getMapAsync {
            map = it
            addAllPolylines()
        }

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            requireView().findViewById<TextView>(R.id.tvTimer).text = formattedTime
        })
    }

    private fun toggleRun() {
        if(isTracking) {
            sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        activity?.let {
            val fragmentActivity = it
            lifecycleScope.launch(Dispatchers.Main) {
                fragmentActivity.finish()
            }
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        val btnToggleRun = requireView().findViewById<Button>(R.id.btnToggleRun)
        val btnFinishRun = requireView().findViewById<Button>(R.id.btnFinishRun)

        if (!isTracking) {
            btnToggleRun.text = getString(R.string.start)
            btnFinishRun.visibility = View.VISIBLE
        } else {
            btnToggleRun.text = getString(R.string.stop)
            btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constants.MAP_ZOOM
                )
            )
        }
    }



    private fun endRunAndSaveToDB() {
            var distanceInMeters = 0f
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val run = Track(distanceInMeters =  distanceInMeters, timeInMillis =  curTimeInMillis)
            activity?.let {
                val fragmentActivity = it
                val trackMasterApplication = fragmentActivity.application as TrackMasterApplication
                lifecycleScope.launch(Dispatchers.IO) {
                    trackMasterApplication.repository.insert(run)
                }
            }
            stopRun()

    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(Constants.POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(Constants.POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}