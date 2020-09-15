package com.journey.interview.weatherapp

import android.Manifest

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
    const val LOCATION_SUCCESS_CODE=0
}