package com.journey.interview.weatherapp.room

import android.util.Log
import com.journey.interview.InterviewApp
import com.journey.interview.weatherapp.model.ChoosePlaceData
import com.journey.interview.weatherapp.model.Place

/**
 * @By Journey 2020/9/22
 * @Description
 */
object RoomHelper {

    private val placeDatabase by lazy {
        PlaceDatabase.getInstance(InterviewApp.instance)
    }

    private val choosePlaceDataBase by lazy {
        ChoosePlaceDataBase.getInstance(InterviewApp.instance)
    }

    private val placeDao by lazy {
        placeDatabase?.placeDao()
    }

    private val choosePlaceDao by lazy {
        choosePlaceDataBase?.choosePlaceDao()
    }

    // 储存选择的城市
    suspend fun insertPlace(place: Place):Long?=
        placeDao?.let {
            it.queryPlaceByName(place.name)?.let {pl->
                val i = it.deletePlace(pl)
                Log.d("JG",i.toString())
            }
            it.insertPlace(place)
        }

    // 储存选择的城市的天气信息
    suspend fun insertChoosePlace(choosePlaceData:ChoosePlaceData) :Long?=
        choosePlaceDao?.let {
            it.queryChoosePlaceByName(choosePlaceData.name)?.let {data->
               val i = choosePlaceDao!!.deleteChoosePlace(data)
                Log.d("JG",i.toString())
            }
            it.insertPlace(choosePlaceData)
        }

}