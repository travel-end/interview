package com.journey.interview.weatherapp.ui.searchplace

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.model.ChoosePlaceData
import com.journey.interview.weatherapp.model.Place
import com.journey.interview.weatherapp.model.RealTime
import com.journey.interview.weatherapp.model.SearchPlace
import com.journey.interview.weatherapp.room.RoomHelper

/**
 * @By Journey 2020/9/22
 * @Description
 */
class SearchPlaceViewModel:BaseViewModel() {
    val searchPlaceData:MutableLiveData<SearchPlace> = MutableLiveData()
    val realtimeWeather:MutableLiveData<RealTime> = MutableLiveData()

    val insertPlaceResult:MutableLiveData<Long> = MutableLiveData()
    val insertChoosePlaceResult:MutableLiveData<Long> = MutableLiveData()

    fun searchPlaces(query:String) {
        request {
            val result=apiService.searchPlaces(query)
            searchPlaceData.value = result
            result.ofMap()?.print().let { Log.v("Kotlin",it?:"") }
        }
    }

    fun loadRealtimeWeather(lng:String,lat:String) {
        request {
            val result = apiService.loadRealtimeWeather(lng,lat)
            realtimeWeather.value = result
            result.ofMap()?.print().let { Log.v("Kotlin",it?:"") }
        }
    }

    fun insertPlace(place: Place) {
        ioRequest {
            insertPlaceResult.value = RoomHelper.insertPlace(place)
        }
    }

    fun insertChoosePlace(data:ChoosePlaceData) {
        ioRequest {
            insertChoosePlaceResult.value = RoomHelper.insertChoosePlace(data)
        }
    }

}