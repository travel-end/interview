package com.journey.interview.weatherapp.net

import com.journey.interview.Constant
import com.journey.interview.weatherapp.model.Daily
import com.journey.interview.weatherapp.model.HourlyData
import com.journey.interview.weatherapp.model.RealTime
import com.journey.interview.weatherapp.model.SearchPlace
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @By Journey 2020/9/15
 * @Description
 */
interface ApiService {

    @GET("v2/place?token=${Constant.CAI_YUN_TOKEN}&lang=zh_CN")
    suspend fun searchPlaces(@Query("query") query:String):SearchPlace

    @GET("v2.5/${Constant.CAI_YUN_TOKEN}/{lng},{lat}/realtime.json")
    suspend fun loadRealtimeWeather(@Path("lng" ) lng:String,@Path("lat") lat:String):RealTime

    @GET("v2.5/${Constant.CAI_YUN_TOKEN}/{lng},{lat}/daily.json")
    suspend fun loadDailyWeather(@Path("lng") lng: String?, @Path("lat") lat: String?): Daily

    @GET("v2.5/${Constant.CAI_YUN_TOKEN}/{lng},{lat}/hourly.json?hourlysteps=12")
    suspend fun loadHourlyWeather(@Path("lng") lng: String?, @Path("lat") lat: String?): HourlyData

}