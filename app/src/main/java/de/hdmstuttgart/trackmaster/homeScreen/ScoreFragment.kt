package de.hdmstuttgart.trackmaster.homeScreen

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.TrackMasterApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScoreFragment : Fragment (R.layout.fragment_score) {

    private var maxDistance = 0f
    private var maxTime = 0L
    private var maxPace = 0f


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        update(view)
    }

    private fun update(view: View) {
        activity?.let {
            val fragmentActivity = it
            val trackMasterApplication = fragmentActivity.application as TrackMasterApplication

            lifecycleScope.launch(Dispatchers.IO) {

                maxDistance = trackMasterApplication.repository.getMaxDistance() / 1000
                maxTime = trackMasterApplication.repository.getMaxTime() / 60000
                maxPace = trackMasterApplication.repository.getMaxPace()

                val maxDistanceView = view.findViewById<TextView>(R.id.maxDistance)
                val maxTimeView= view.findViewById<TextView>(R.id.maxTime)
                val maxPaceView = view.findViewById<TextView>(R.id.maxPace)

                withContext(Dispatchers.Main) {
                    maxDistanceView.append("\n" + maxDistance + " km")
                    maxTimeView.append("\n" + maxTime + " minutes")
                    maxPaceView.append("\n" + maxPace + " km/h")
                }
            }
        }
    }
}