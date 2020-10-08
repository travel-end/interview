package com.journey.interview.imusic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.journey.interview.imusic.model.LocalSong

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Database(entities = [LocalSong::class], version = 1, exportSchema = false)
abstract class LocalSongDatabase : RoomDatabase() {
    abstract fun localSongDao(): LocalSongDao
    companion object {
        private var instance: LocalSongDatabase? = null
        fun getInstance(context: Context): LocalSongDatabase {
            if (instance == null) {
                synchronized(LocalSongDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            LocalSongDatabase::class.java,
                            "database_local_song"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}