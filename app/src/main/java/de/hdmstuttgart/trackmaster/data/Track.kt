package de.hdmstuttgart.trackmaster.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Track (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "distance") val distance: Int,
    @ColumnInfo(name = "time") val time: Int,
    @ColumnInfo(name = "pace") val pace: Int,
    //todo: Change variable type according to the type from googlemaps?
)