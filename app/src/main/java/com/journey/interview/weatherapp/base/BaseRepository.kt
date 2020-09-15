package com.journey.interview.weatherapp.base

import com.journey.interview.weatherapp.net.ApiService
import com.journey.interview.weatherapp.net.RetrofitClient

/**
 * @By Journey 2020/9/15
 * @Description
 */
open class BaseRepository {
    protected val apiService :ApiService by lazy {
        RetrofitClient.instance.createApiService(ApiService::class.java)
    }
}