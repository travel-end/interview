package com.journey.interview.weatherapp.ui.searchplace

import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.journey.interview.InterviewApp
import com.journey.interview.R
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.hideKeyboards
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import com.journey.interview.weatherapp.global.AppEventViewModel
import com.journey.interview.weatherapp.global.EventSender
import com.journey.interview.weatherapp.model.ChoosePlaceData
import com.journey.interview.weatherapp.model.Place
import kotlinx.android.synthetic.main.wea_frg_search_place.*
import kotlinx.android.synthetic.main.wea_header_view.view.*

/**
 * @By Journey 2020/9/15
 * @Description
 */
class SearchPlaceFragment:BaseLifeCycleFragment<SearchPlaceViewModel>() {
    private var place:Place?=null
    override fun layoutResId()=R.layout.wea_frg_search_place
    override fun initView() {
        super.initView()
        initHeaderView()
        place_recycle.setup<Place> {
            adapter {
                addItem(R.layout.wea_item_search_result) {
                    bindViewHolder {data, position, holder ->
                        data?.let {pl->
                            setText(R.id.placeName,pl.name)
                            setText(R.id.placeAddress,pl.address)
                            itemClicked(View.OnClickListener {
                                place = pl
                                mViewModel.loadRealtimeWeather(pl.location.lng,pl.location.lat)
                                mViewModel.insertPlace(pl)
                            })
                        }

                    }
                }
            }
        }

    }

    private fun initHeaderView() {
        search_bar.detail_title.text = "搜索城市"
        search_bar.detail_end.visibility = View.GONE
        search_bar.detail_start.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
    }

    override fun initData() {
        super.initData()
        search_places_edit.addTextChangedListener {edit->
            val content = edit.toString()
            if (content.isNotEmpty()) {
                mViewModel.searchPlaces(content)
                place_recycle.visibility = View.VISIBLE
            } else {
                place_recycle.visibility = View.GONE
                mViewModel.searchPlaceData.value = null
                place_recycle.clearData()
            }
        }
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.searchPlaceData.observe(this,Observer{
            it?.let {
                place_recycle.submitList(it.places)
            }
        })
        mViewModel.realtimeWeather.observe(this,Observer{
            it?.let {
                Log.e("JG","--->储存天气信息")
                // 将天气信息储存数据库
                mViewModel.insertChoosePlace(
                    ChoosePlaceData(
                        0,false,place?.name?:"",
                        it.result.realtime.temperature.toInt(),it.result.realtime.skycon
                    )
                )
            }
        })

        // 储存已选城市
        mViewModel.insertPlaceResult.observe(this,Observer{
            it?.let {
                EventSender.sendPlaceAddEvent()
                requireActivity().hideKeyboards()
            }
        })

        // 储存已选城市天气信息
        mViewModel.insertChoosePlaceResult.observe(this,Observer{
            it?.let {
                EventSender.sendPlaceChosenEvent()
                Navigation.findNavController(search_place).navigateUp()
            }
        })

    }
}