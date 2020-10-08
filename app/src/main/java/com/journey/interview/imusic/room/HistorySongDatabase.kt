package com.journey.interview.imusic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.journey.interview.imusic.model.HistorySong
import com.journey.interview.imusic.model.LoveSong

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Database(entities = [HistorySong::class], version = 1, exportSchema = false)
abstract class HistorySongDatabase : RoomDatabase() {
    abstract fun historySongDao(): HistorySongDao
    companion object {
        private var instance: HistorySongDatabase? = null
        fun getInstance(context: Context): HistorySongDatabase {
            if (instance == null) {
                synchronized(HistorySongDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            HistorySongDatabase::class.java,
                            "database_history_song"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}