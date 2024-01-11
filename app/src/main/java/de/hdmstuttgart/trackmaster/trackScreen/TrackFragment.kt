package de.hdmstuttgart.trackmaster.trackScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdmstuttgart.trackmaster.R
import de.hdmstuttgart.trackmaster.TrackMasterApplication
import de.hdmstuttgart.trackmaster.data.Track
import de.hdmstuttgart.trackmaster.data.TrackAdapter
import de.hdmstuttgart.trackmaster.data.TrackClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackFragment : Fragment(R.layout.fragment_tracks), TrackClickListener {

    private val data = ArrayList<Track>()
    private val adapter = TrackAdapter(data, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.trackRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        recyclerView.adapter = adapter

        update()
    }

    private fun update() {
        activity?.let {
            val fragmentActivity = it
            val trackMasterApplication = fragmentActivity.application as TrackMasterApplication

            lifecycleScope.launch(Dispatchers.IO) {

                data.clear()
                data.addAll(trackMasterApplication.repository.getAllTracks())

                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onTrackClickListener(position: Int) {
        //val track = data.get(position)

        //todo: show track details? delete track? if neither is wanted delete onTrackClickListener
        activity?.let {

            lifecycleScope.launch(Dispatchers.IO) {

                withContext(Dispatchers.Main) {

                }
            }
        }
    }
}