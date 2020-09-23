package com.journey.interview.weatherapp.ui.chooseplace

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.model.ChoosePlaceData
import com.journey.interview.weatherapp.room.RoomHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/22
 * @Description
 */
class ChosenPlaceViewModel :BaseViewModel() {
    val allChosenPlace : MutableLiveData<MutableList<ChoosePlaceData>> = MutableLiveData()
//    fun queryAllChosenPlace() {
//        var result:MutableList<ChoosePlaceData>?=null
//        ioRequest {
//            result = RoomHelper.queryAllChosenPlace()
//        }
//        Log.e("JG","allChosenPlace: $result")
//        allChosenPlace.value = result
//    }
    fun queryAllChosenPlace() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                RoomHelper.queryAllChosenPlace()
            }
            Log.e("JG","allChosenPlace: $result")
            allChosenPlace.value = result
        }
    }
}