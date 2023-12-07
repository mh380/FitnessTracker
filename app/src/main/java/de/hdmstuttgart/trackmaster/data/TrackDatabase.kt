package de.hdmstuttgart.trackmaster.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Track::class], version = 1, exportSchema = false) //bei Änderungen immer Versionsnummer erhöhen!
abstract class TrackDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: TrackDatabase? = null

        fun getDatabase(context: Context): TrackDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackDatabase::class.java,
                    "movie-database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}