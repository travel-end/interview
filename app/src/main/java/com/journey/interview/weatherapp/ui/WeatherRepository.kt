package com.journey.interview.weatherapp.ui

import com.journey.interview.weatherapp.base.BaseRepository
import com.journey.interview.weatherapp.model.SearchPlace

/**
 * @By Journey 2020/9/15
 * @Description
 */
class WeatherRepository:BaseRepository() {
    suspend fun searchPlaces(query:String):SearchPlace {
        return apiService.searchPlaces(query)
    }

}