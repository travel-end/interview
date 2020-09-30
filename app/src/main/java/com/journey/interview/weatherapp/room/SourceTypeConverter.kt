package com.journey.interview.weatherapp.room

import androidx.room.TypeConverter
import com.journey.interview.weatherapp.model.Location
import org.json.JSONObject

/**
 * TypeConverter:room用于处理不能识别的类型（即不在基础类型之内的）
 * 此处的Location类型显然不是基本类型，使用TypeConverter转换为String类型
 */
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