package com.journey.interview.weatherapp.ui.searchplace

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.model.ChoosePlaceData
import com.journey.interview.weatherapp.model.Place
import com.journey.interview.weatherapp.model.RealTime
import com.journey.interview.weatherapp.model.SearchPlace
import com.journey.interview.weatherapp.room.RoomHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/22
 * @Description
 */
class SearchPlaceViewModel:BaseViewModel() {
    val searchPlaceData:MutableLiveData<SearchPlace> = MutableLiveData()
    val realtimeWeather:MutableLiveData<RealTime> = MutableLiveData()

    val insertPlaceResult:MutableLiveData<Long?> = MutableLiveData()
    val insertChoosePlaceResult:MutableLiveData<Long?> = MutableLiveData()

    fun searchPlaces(query:String) {
        request {
            val result=apiService.searchPlaces(query)
            searchPlaceData.value = result
            result.ofMap()?.print().let { Log.e("JG","searchPlace：$it") }
        }
    }

    fun loadRealtimeWeather(lng:String,lat:String) {
        request {
            val result = apiService.loadRealtimeWeather(lng,lat)
            realtimeWeather.value = result
            result.ofMap()?.print().let { Log.e("JG","realWeather：$it") }
        }
    }

//    fun insertPlace(place: Place) {
//        // 这种写法是错误
//        var result:Long?=null
//        ioRequest {
//            result = RoomHelper.insertPlace(place)
//        }
//        Log.e("JG","insert place result=$result")
//        insertPlaceResult.value = result
//    }

    fun insertPlace(place: Place) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                RoomHelper.insertPlace(place)
            }
            Log.e("JG","insert place result=$result")
            insertPlaceResult.value = result
        }
    }


//    fun insertChoosePlace(data:ChoosePlaceData) {
//        var result:Long?=null
//        ioRequest {
//            result= RoomHelper.insertChoosePlace(data)
//        }
//        Log.e("JG","insert chosen place result=$result")
//        insertChoosePlaceResult.value = result
//    }

    fun insertChoosePlace(data:ChoosePlaceData) {
        viewModelScope.launch {
            val result= withContext(Dispatchers.IO) {
                RoomHelper.insertChoosePlace(data)
            }
            Log.e("JG","insert chosen place result=$result")
            insertChoosePlaceResult.value = result
        }
    }
}