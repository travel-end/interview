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

    val deletePlaceResult : MutableLiveData<Boolean> = MutableLiveData()
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

    fun deletePlace(name:String) {
        ioRequest {
            RoomHelper.deletePlace(RoomHelper.queryPlaceByName(name))
            queryAllChosenPlace()
        }
    }

    fun deleteChosenPlace(chosenPlace:ChoosePlaceData) {
        ioRequest {
            RoomHelper.deleteChosenPlace(chosenPlace)
            queryAllChosenPlace()
        }
    }

    fun deleteSelectedPlace(name: String,chosenPlace:ChoosePlaceData) {
        viewModelScope.launch {
            val r = withContext(Dispatchers.IO) {
                val deletePlaceDeferred = requestAsync { RoomHelper.deletePlace(RoomHelper.queryPlaceByName(name)) }
                val deleteChosenPlaceDeferred = requestAsync { RoomHelper.deleteChosenPlace(chosenPlace) }
                queryAllChosenPlace()
                deleteChosenPlaceDeferred.await()
                deletePlaceDeferred.await()
            }
            deletePlaceResult.value =  r!= null
        }
//        ioRequest {
//            val deletePlaceDeferred = requestAsync { RoomHelper.deletePlace(RoomHelper.queryPlaceByName(name)) }
//            val deleteChosenPlaceDeferred = requestAsync { RoomHelper.deleteChosenPlace(chosenPlace) }
//            val result1 = deletePlaceDeferred.await()
//            val result2 = deleteChosenPlaceDeferred.await()
//            Log.e("JG","delete result1:$result1, delete result2:$result2")
//            queryAllChosenPlace()
//        }
    }
}