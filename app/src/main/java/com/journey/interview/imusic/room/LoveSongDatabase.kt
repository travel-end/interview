package com.journey.interview.imusic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.journey.interview.imusic.model.LoveSong

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Database(entities = [LoveSong::class], version = 1, exportSchema = false)
abstract class LoveSongDatabase : RoomDatabase() {
    abstract fun loveSongDao(): LoveSongDao
    companion object {
        private var instance: LoveSongDatabase? = null
        fun getInstance(context: Context): LoveSongDatabase {
            if (instance == null) {
                synchronized(LoveSongDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            LoveSongDatabase::class.java,
                            "database_love_song"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}