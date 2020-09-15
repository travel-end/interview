package com.journey.interview.weatherapp.net

import com.journey.interview.weatherapp.Constant
import com.journey.interview.weatherapp.model.SearchPlace
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @By Journey 2020/9/15
 * @Description
 */
interface ApiService {

    @GET("v2/place?token=${Constant.CAI_YUN_TOKEN}&lang=zh_CN")
    suspend fun searchPlaces(@Query("query") query:String):SearchPlace
}