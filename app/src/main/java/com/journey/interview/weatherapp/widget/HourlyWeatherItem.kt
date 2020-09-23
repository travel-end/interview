package com.journey.interview.weatherapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.journey.interview.R

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/6/15 8:31
 */
class HourlyWeatherItem(context: Context) :
    LinearLayout(context) {
    private lateinit var mRootView: View
    private lateinit var mTimeText: TextView
    private lateinit var mWeather: TextView
    private lateinit var mTemperatureView: TemperatureView
    private lateinit var mWindOri: TextView
    private lateinit var mWIndLevel: TextView
    private lateinit var mAirLevel: TextView
    private lateinit var mWeatherImage: ImageView

    init {
        init(context)
    }

    private fun init(context: Context) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.wea_hourly_item, null)
        mTimeText = mRootView.findViewById(R.id.hourly_time)
        mWeather = mRootView.findViewById(R.id.hourly_weather)
        mTemperatureView = mRootView.findViewById(R.id.hourly_temp)
        mWindOri = mRootView.findViewById(R.id.hourly_wind_ori)
        mWIndLevel = mRootView.findViewById(R.id.hourly_wind_level)
        mAirLevel = mRootView.findViewById(R.id.hourly_air_level)
        mWeatherImage = mRootView.findViewById(R.id.hourly_weather_img)
        mRootView.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(mRootView)
    }

    fun setTime(time: String?) {
        mTimeText?.text = time
    }

    fun getTempX(): Float {
        return if (mTemperatureView != null) mTemperatureView.x else 0.0F
    }

    fun getTempY(): Float {
        return if (mTemperatureView != null) mTemperatureView.y else 0.0F
    }

    fun setWeather(weather: String?) {
        mWeather?.text = weather
    }

    fun setWindOri(windOri: String) {
        mWindOri?.text = windOri
    }

    fun setWindLevel(windLevel: String) {
        mWIndLevel?.text = windLevel
    }

    fun setAirLevel(airLevel: String) {
        mAirLevel?.text = airLevel
    }

    fun setTemp(temp: Int) {
        mTemperatureView?.setTemp(temp)
    }

    fun setImg(resId: Int) {
        mWeatherImage?.setImageResource(resId)
    }

    fun setMaxTemp(max: Int) {
        mTemperatureView?.setMaxTemp(max)
    }

    fun setMinTemp(min: Int) {
        mTemperatureView?.setMinTemp(min)
    }
}