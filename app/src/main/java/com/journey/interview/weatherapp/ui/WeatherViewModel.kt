package com.journey.interview.weatherapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.model.SearchPlace

/**
 * @By Journey 2020/9/15
 * @Description
 */
class WeatherViewModel:BaseViewModel() {
    val mSearchPlacesData: MutableLiveData<SearchPlace> = MutableLiveData()
    fun searchPlaces(query:String) {
        Log.v("JG","--->searchPlaces")
        request{
            mSearchPlacesData.value=  apiService.searchPlaces(query)
        }
    }

}