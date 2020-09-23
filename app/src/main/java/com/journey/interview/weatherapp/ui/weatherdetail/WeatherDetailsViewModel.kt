package com.journey.interview.weatherapp.ui.weatherdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.model.Daily
import com.journey.interview.weatherapp.model.HourlyData
import com.journey.interview.weatherapp.model.RealTime
import com.journey.interview.weatherapp.room.RoomHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/23
 * @Description
 */
class WeatherDetailsViewModel:BaseViewModel() {
    val realtimeWeather: MutableLiveData<RealTime> = MutableLiveData()
    val dailyWeather: MutableLiveData<Daily> = MutableLiveData()
    val perHourWeather: MutableLiveData<HourlyData> = MutableLiveData()


    fun loadRealtimeWeatherDetails(lng:String,lat:String) {
        Log.e("JG","--->weatherDetailsFragment loadRealtimeWeatherDetails")
        request {
            val realtimeWeatherDeferred = requestAsync { apiService.loadRealtimeWeather(lng,lat) }
            realtimeWeather.value = realtimeWeatherDeferred.await()

            val dailyWeatherDeferred = requestAsync { apiService.loadDailyWeather(lng,lat) }
            dailyWeather.value = dailyWeatherDeferred.await()

            val perHourWeatherDeferred = requestAsync { apiService.loadHourlyWeather(lng,lat) }
            perHourWeather.value = perHourWeatherDeferred.await()
        }
    }

    fun loadRealtimeWeather(lng:String,lat:String) {
        request {
            realtimeWeather.value = apiService.loadRealtimeWeather(lng,lat)
        }
    }

    fun loadDailyWeather(lng: String,lat: String) {
        request {
            dailyWeather.value = apiService.loadDailyWeather(lng,lat)
        }
    }

    fun loadPerHourWeather(lng: String,lat: String) {
        request {
            perHourWeather.value = apiService.loadHourlyWeather(lng,lat)
        }
    }

    fun updateChoosePlace(temperature: Int, skycon: String, name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                RoomHelper.updateChoosePlace(temperature, skycon, name)
            }
        }
    }

}