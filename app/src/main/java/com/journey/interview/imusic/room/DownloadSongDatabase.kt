package com.journey.interview.imusic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.journey.interview.imusic.model.DownloadSong

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Database(entities = [DownloadSong::class], version = 1, exportSchema = false)
abstract class DownloadSongDatabase : RoomDatabase() {
    abstract fun downloadSongDao(): DownloadSongDao
    companion object {
        private var instance: DownloadSongDatabase? = null
        fun getInstance(context: Context): DownloadSongDatabase {
            if (instance == null) {
                synchronized(DownloadSongDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            DownloadSongDatabase::class.java,
                            "database_download_song"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}