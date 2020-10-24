package com.journey.interview.imusic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.journey.interview.imusic.model.recommend.RecomSong

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Database(entities = [RecomSong::class], version = 1, exportSchema = false)
abstract class RecommendSongDatabase : RoomDatabase() {
    abstract fun recommendSongDao(): RecommendSongDao
    companion object {
        private var instance: RecommendSongDatabase? = null
        fun getInstance(context: Context): RecommendSongDatabase {
            if (instance == null) {
                synchronized(RecommendSongDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            RecommendSongDatabase::class.java,
                            "database_recom_song"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}