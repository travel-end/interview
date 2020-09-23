package com.journey.interview.weatherapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.model.Place
import com.journey.interview.weatherapp.room.RoomHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/15
 * @Description
 */
class HomeViewModel:BaseViewModel() {
    val allPlaceData: MutableLiveData<MutableList<Place>> = MutableLiveData()

    fun queryAllPlace() {
        Log.e("JG","--->homeFragment queryAllPlace")
        viewModelScope.launch {
            allPlaceData.value = withContext(Dispatchers.IO) {
                RoomHelper.queryAllPlace()
            }
        }
    }
}