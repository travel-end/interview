package com.journey.interview

import android.Manifest
import java.util.*

/**
 * @By Journey 2020/9/15
 * @Description
 */
object Constant {
    const val BASE_URL = "https://api.caiyunapp.com/"
    const val CAI_YUN_TOKEN = "auv03EiB3Zt9RZcn"
    val mPermission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    const val LOCATION_SUCCESS_CODE = 0

    const val LNG_KEY = "lng"
    const val LAT_KEY = "lat"
    const val PLACE_NAME = "name"
    const val PLACE_PRIMARY_KEY = "key"

    fun getTodayInWeek(data: Date): String {
        val list = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }
}