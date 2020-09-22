package com.journey.interview.weatherapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.journey.interview.weatherapp.model.Place

/**
 * @By Journey 2020/9/22
 * @Description
 */
@Database(entities = [Place::class], version = 1, exportSchema = false)

@TypeConverters(LocationTypeConverter::class)
abstract class PlaceDatabase:RoomDatabase() {
    abstract fun placeDao():PlaceDao
    companion object{
        private var instance:PlaceDatabase?=null
        fun getInstance(context: Context):PlaceDatabase? {
            synchronized(PlaceDatabase::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        PlaceDatabase::class.java,
                        "database_weather"
                    ).build()
                }
            }
            return instance
        }
    }

}