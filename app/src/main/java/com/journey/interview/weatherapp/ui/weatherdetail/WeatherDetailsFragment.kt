package com.journey.interview.weatherapp.ui.weatherdetail

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.journey.interview.R
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.getAirLevel
import com.journey.interview.utils.getSky
import com.journey.interview.utils.getWindOri
import com.journey.interview.utils.getWindSpeed
import com.journey.interview.weatherapp.Constant
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import com.journey.interview.weatherapp.model.Daily
import com.journey.interview.weatherapp.model.HourlyWeather
import com.journey.interview.weatherapp.model.RealTime
import com.journey.interview.weatherapp.widget.HourlyWeatherItem
import com.journey.interview.weatherapp.widget.WeatherView
import kotlinx.android.synthetic.main.waa_layout_life_index.*
import kotlinx.android.synthetic.main.wea_frg_weather_details.*
import kotlinx.android.synthetic.main.wea_item_search_result.*
import kotlinx.android.synthetic.main.wea_layout_current_place_details.*
import kotlinx.android.synthetic.main.wea_layout_flipper_detail.*

/**
 * @By Journey 2020/9/23
 * @Description
 */
class WeatherDetailsFragment:BaseLifeCycleFragment<WeatherDetailsViewModel>() {
    private val mLng: String by lazy { arguments?.getString(Constant.LNG_KEY) ?: "" }
    private val mLat: String by lazy { arguments?.getString(Constant.LAT_KEY) ?: "" }
    private val mPlaceName: String by lazy { arguments?.getString(Constant.PLACE_NAME) ?: "" }
    private var list = ArrayList<HourlyWeather>()
    override fun layoutResId()=R.layout.wea_frg_weather_details
    companion object {
        fun newInstance(placeName: String, lng: String, lat: String, placeKey: Int): Fragment {
            val bundle = Bundle()
            bundle.putString(Constant.LNG_KEY, lng)
            bundle.putString(Constant.LAT_KEY, lat)
            bundle.putString(Constant.PLACE_NAME, placeName)
            bundle.putInt(Constant.PLACE_PRIMARY_KEY, placeKey)
            val fragment = WeatherDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        super.initView()
        setHasOptionsMenu(true)
        home_recycler.setup<Daily.DailyData> {
            withLayoutManager {
                return@withLayoutManager LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            }
            adapter {
                addItem(R.layout.wea_layout_daily_item) {
                    bindViewHolder { data, position, holder ->
                        data?.let {item->
                            setText(R.id.date,Constant.getTodayInWeek(item.date))
                            setImageResource(R.id.weather, getSky(item.value).icon)
                            setText(R.id.temperature,"${item.min.toInt()}℃~ ${item.max.toInt()}℃")
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.loadRealtimeWeatherDetails(mLng,mLat)
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.run {
            realtimeWeather.observe(this@WeatherDetailsFragment,Observer{
                it?.let {realtime->
                    initRealtimeWeather(realtime.result.realtime)
                    mViewModel.updateChoosePlace(
                        realtime.result.realtime.temperature.toInt(),
                        realtime.result.realtime.skycon,
                        mPlaceName
                    )
                }
            })
            dailyWeather.observe(this@WeatherDetailsFragment, Observer { response ->
                response?.let {
                    val dailyDataList = ArrayList<Daily.DailyData>()
                    for (i in it.result.daily.skycon.indices) {
                        dailyDataList.add(
                            Daily.DailyData(
                                it.result.daily.skycon[i].date,
                                it.result.daily.skycon[i].value,
                                it.result.daily.temperature[i].max,
                                it.result.daily.temperature[i].min
                            )
                        )
                    }
                    home_recycler.submitList(dailyDataList)
                    val lifeIndex = it.result.daily.life_index
                    coldRiskText.text = lifeIndex.carWashing[0].desc
                    dressingText.text = lifeIndex.dressing[0].desc
                    ultravioletText.text = lifeIndex.ultraviolet[0].desc
                    carWashingText.text = lifeIndex.carWashing[0].desc
                }
            })
            perHourWeather.observe(this@WeatherDetailsFragment, Observer { response ->
                response?.let {
                    for (i in it.result.hourly.temperature.indices) {
                        list.add(
                            HourlyWeather(
                                it.result.hourly.temperature[i].value.toInt(),
                                it.result.hourly.skycon[i],
                                getSky(it.result.hourly.skycon[i].value).info,
                                it.result.hourly.temperature[i].datetime.substring(11, 16),
                                getSky(it.result.hourly.skycon[i].value).icon,
                                getWindOri(it.result.hourly.wind[i].direction).ori,
                                getWindSpeed(it.result.hourly.wind[i].speed).speed,
                                getAirLevel(it.result.hourly.air_quality.aqi[i].value.chn).airLevel
                            )
                        )
                    }
                    initPerHourWeather(list)
                }
            })
        }
    }

    private fun initRealtimeWeather(realtime: RealTime.Realtime) {
        temp_text_view.text = "${realtime.temperature.toInt()} ℃"
        description_text_view.text = getSky(realtime.skycon).info
        animation_view.setImageResource(getSky(realtime.skycon).icon)
        humidity_text_view.text = "湿度: ${(realtime.humidity * 100).toInt()} %"
        wind_text_view.text = "风力: ${getWindSpeed(realtime.wind.speed).speed}"
        visible_text_view.text = "能见度: ${realtime.visibility} m"
        index_text_view.text = "空气质量: ${getAirLevel(realtime.air_quality.aqi.chn).airLevel}"
    }
    private fun initPerHourWeather(list: ArrayList<HourlyWeather>) {
        weather_view.setList(list)
        weather_view.setLineWidth(6f)
        try {
            weather_view.setColumnNumber(5)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        weather_view.setOnWeatherItemClickListener(object : WeatherView.OnWeatherItemClickListener {
            override fun onItemClick(
                itemView: HourlyWeatherItem?,
                position: Int,
                weatherModel: HourlyWeather?
            ) {
                Toast.makeText(requireContext(), position.toString() + "", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        weather_view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                weather_view.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })

    }

}