package de.hdmstuttgart.trackmaster.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdmstuttgart.trackmaster.R
import kotlin.math.round

interface TrackClickListener {
    fun onTrackClickListener(position: Int)
}

class TrackAdapter(private val list: List<Track>,
                   private val trackClickListener: TrackClickListener): RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trackModel = list[position]
        val time = round(trackModel.timeInMillis / 60000f)

        holder.dateView.text = ("Date: " + toGermanDate(trackModel.date))
        holder.distanceView.text = ("Distance: " + trackModel.distanceInMeters / 1000 + " km")
        holder.timeView.text = ("Time: " + time + " minutes")
        holder.paceView.text = ("Average Pace: " + trackModel.avgSpeedInKMH + " km/h")

        holder.itemView.setOnClickListener {
            trackClickListener.onTrackClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateView: TextView = itemView.findViewById(R.id.dateView)
        val distanceView: TextView = itemView.findViewById(R.id.distanceView)
        val timeView: TextView = itemView.findViewById(R.id.timeView)
        val paceView: TextView = itemView.findViewById(R.id.paceView)
    }
}