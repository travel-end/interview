package com.journey.interview.weatherapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.journey.interview.InterviewApp
import com.journey.interview.R
import com.journey.interview.Constant
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import com.journey.interview.weatherapp.model.Place
import com.journey.interview.weatherapp.permission.PermissionResult
import com.journey.interview.weatherapp.permission.Permissions

/**
 * @By Journey 2020/9/15
 * @Description
 */
class WeatherActivity : BaseLifeCycleActivity<WeatherViewModel>(),
    GeocodeSearch.OnGeocodeSearchListener {
    private lateinit var mGeoCoderSearch: GeocodeSearch
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private var mPlace:Place?=null
    override fun layoutResId(): Int = R.layout.wea_act_main
    override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
        Log.d("JG","onRegeocodeSearched: ${p0?.regeocodeAddress?.formatAddress} ----  $p1")
        if (p1 == AMapException.CODE_AMAP_SUCCESS) {
            p0?.let {
                if (it.regeocodeAddress != null && it.regeocodeAddress.formatAddress != null) {
                    mViewModel.searchPlaces(it.regeocodeAddress.formatAddress.substring(3, 6))
                }
            }
        }
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
        mGeoCoderSearch = GeocodeSearch(this)
        mGeoCoderSearch.setOnGeocodeSearchListener(this)
    }

    private fun initPermission() {
        Permissions(this).request(*Constant.mPermission).observe(this, Observer {
            it?.let { result ->
                when (result) {
                    is PermissionResult.Grant -> {
                        initLocation()
                    }
                    // 进入设置权限申请页面
                    is PermissionResult.Deny -> {
                        startPermissionsSettings()
                    }
                    is PermissionResult.Rationale -> {
                        startPermissionsSettings()
                    }
                }
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_main).navigateUp()
    }

    private fun initLocation() {
        if (mLocationClient == null) {
            mLocationClient = AMapLocationClient(InterviewApp.instance)
        }
        if (mLocationOption == null) {
            mLocationOption = getDefaultLocationOption()
        }
        // 设置定位参数
        mLocationClient!!.setLocationOption(mLocationOption)
        // 设置定位监听
        mLocationClient!!.setLocationListener(mLocationListener)
        startLocation()
    }

    private val mLocationListener = AMapLocationListener {
        it?.let { location ->
            if (location.errorCode == Constant.LOCATION_SUCCESS_CODE) {
                Log.d("JG","定位成功: ${location.latitude} ----  ${location.longitude}")
                queryLocationAsync(location.latitude, location.longitude)
            } else {
                Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun queryLocationAsync(lat: Double, lng: Double) {
        val query = RegeocodeQuery(LatLonPoint(lat, lng), 200F, GeocodeSearch.AMAP)
        mGeoCoderSearch.getFromLocationAsyn(query)
    }

    // 启动定位
    private fun startLocation() {
        mLocationClient?.setLocationOption(mLocationOption)
        mLocationClient?.startLocation()
        Toast.makeText(this, "正在定位中请稍后...", Toast.LENGTH_SHORT).show()
    }

    // 停止定位
    private fun stopLocation() {
        mLocationClient?.stopLocation()
    }

    private fun getDefaultLocationOption(): AMapLocationClientOption {
        val option =  AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isGpsFirst = true
            httpTimeOut = 30000
            interval = 2000
            isNeedAddress = true
            isOnceLocation = true
            isOnceLocationLatest = false
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS)
            isSensorEnable = false
            isWifiScan = true
            isLocationCacheEnable = true
            geoLanguage = AMapLocationClientOption.GeoLanguage.DEFAULT
        }
        return option
    }

    private fun startPermissionsSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.mSearchPlacesData.observe(this,Observer{
            it?.let {
                mPlace = it.places[0]
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLocationClient != null) {
            mLocationClient?.onDestroy()
            mLocationClient = null
            mLocationOption = null
        }
    }
}