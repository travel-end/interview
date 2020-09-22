package com.journey.interview.weatherapp.room

import androidx.room.TypeConverter
import com.journey.interview.weatherapp.model.Location
import org.json.JSONObject

class LocationTypeConverter {
    @TypeConverter
    fun fromLocation(location: Location): String {
        return JSONObject().apply {
            put("lng", location.lng)
            put("lat", location.lat)
        }.toString()
    }

    @TypeConverter
    fun toLocation(location: String): Location {
        val json = JSONObject(location)
        return Location(json.getString("lng"), json.getString("lat"))
    }
}