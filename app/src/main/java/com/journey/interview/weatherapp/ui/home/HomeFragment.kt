package com.journey.interview.weatherapp.ui.home

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.journey.interview.R
import com.journey.interview.customizeview.indicator.IndicatorView
import com.journey.interview.customizeview.indicator.enums.IndicatorSlideMode
import com.journey.interview.customizeview.indicator.enums.IndicatorStyle
import com.journey.interview.utils.f_dp
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import com.journey.interview.weatherapp.global.EventSender
import com.journey.interview.weatherapp.model.Place
import com.journey.interview.weatherapp.ui.weatherdetail.WeatherDetailsFragment
import kotlinx.android.synthetic.main.wea_frg_home.*
import kotlinx.android.synthetic.main.wea_frg_home.view.*

/**
 * @By Journey 2020/9/15
 * @Description  显示具体城市的具体天气情况
 */
class HomeFragment:BaseLifeCycleFragment<HomeViewModel>() {
    private lateinit var indicatorView:IndicatorView
    private lateinit var weatherTitle:TextView
    private val placeNames = arrayListOf<String>()
    override fun layoutResId()= R.layout.wea_frg_home
    override fun initView() {
        super.initView()
        indicatorView = indicator_view
        weatherTitle =  home_title
        weatherTitle.text = resources.getString(R.string.weather)
    }

    override fun initData() {
        super.initData()
        mViewModel.queryAllPlace()
        mRootView.findViewById<ImageView>(R.id.home_to_city).setOnClickListener {
            Navigation.findNavController(weatherTitle).navigate(R.id.action_homeFragment_to_choosePlaceFragment)
        }
        mRootView.findViewById<ImageView>(R.id.home_iv_more).setOnClickListener {

        }
    }

    override fun dataObserve() {
        super.dataObserve()
        EventSender.observePlaceAdd(this,{
            Log.e("JG","--->observePlaceAdd")
            mViewModel.queryAllPlace()
        })
        EventSender.observePlaceChange(this,{
            Log.e("JG","--->observePlaceChange $it")
            it?.let {
                home_viewpager.setCurrentItem(it,true)
            }
        })
        mViewModel.allPlaceData.observe(this,Observer{
            it?.let {
                if (it.size == 0) {
                    Navigation.findNavController(home_root_view).navigate(R.id.choosePlaceFragment)
                    weatherTitle.text = ""
                }
                showChosenPlaceWeather(it)
            }
        })
    }

    private fun showChosenPlaceWeather(places:MutableList<Place>) {
        val tabs = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        for (place in places) {
                tabs.add(place.name)
                fragments.add(
                    WeatherDetailsFragment.newInstance(
                        place.name,
                        place.location.lng,
                        place.location.lat,
                        place.primaryKey
                    )
                )
        }
        placeNames.clear()
        placeNames.addAll(tabs)
        if (placeNames.isNotEmpty()) {
            weatherTitle.text = placeNames[0]
        }
        home_viewpager.offscreenPageLimit = placeNames.size
        val pageAdapter = object :FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int)=fragments[position]
            override fun getCount()=fragments.size
        }
        home_viewpager.adapter = pageAdapter
        home_viewpager.addOnPageChangeListener(WeatherTitlePageChangeListener())
        indicatorView
            .setSliderColor(ContextCompat.getColor(requireContext(),R.color.grey_10),ContextCompat.getColor(requireContext(),R.color.material_blue))
            .setSliderWidth(8.f_dp)
            .setSliderHeight(8.f_dp)
            .setSlideMode(IndicatorSlideMode.WORM)
            .setIndicatorStyle(IndicatorStyle.CIRCLE)
            .setupWithViewPager(home_viewpager)
//        indicatorView.visibility = View.INVISIBLE
    }

    private inner class WeatherTitlePageChangeListener:ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {
            Log.e("JG","--->onPageScrollStateChanged: $state")
//            indicatorView.visibility = View.INVISIBLE
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {}
        override fun onPageSelected(position: Int) {
            weatherTitle.text = placeNames[position]
        }
    }
}