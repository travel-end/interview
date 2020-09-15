package com.journey.interview.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.model.SearchPlace

/**
 * @By Journey 2020/9/15
 * @Description
 */
class WeatherViewModel:BaseViewModel<WeatherRepository>() {
    val mSearchPlacesData: MutableLiveData<SearchPlace> = MutableLiveData()
    fun searchPlaces(query:String) {
        launch{
            mSearchPlacesData.value=  mRepository.searchPlaces(query)
        }
    }

}