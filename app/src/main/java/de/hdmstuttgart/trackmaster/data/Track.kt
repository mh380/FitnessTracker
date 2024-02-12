package de.hdmstuttgart.trackmaster.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import kotlin.math.round

@Entity(tableName = "running_table")
data class Track(
    val date: LocalDate = LocalDate.now(),
    val distanceInMeters: Float,
    val timeInMillis: Long,
    val avgSpeedInKMH: Float = round((distanceInMeters / 1000f) / (timeInMillis / 1000f / 60 / 60) * 10) / 10f
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}