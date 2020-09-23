package com.journey.interview.weatherapp.ui.chooseplace

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.journey.interview.R
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.getSky
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import com.journey.interview.weatherapp.global.EventSender
import com.journey.interview.weatherapp.model.ChoosePlaceData
import kotlinx.android.synthetic.main.wea_frg_choose_place.*
import kotlinx.android.synthetic.main.wea_header_view.view.*

/**
 * @By Journey 2020/9/15
 * @Description 列表显示当前已经添加的城市
 */
class ChosenPlaceFragment:BaseLifeCycleFragment<ChosenPlaceViewModel>() {
    override fun layoutResId()= R.layout.wea_frg_choose_place
    override fun initView() {
        super.initView()
        initHeaderView()
        rvChosenPlace.setup<ChoosePlaceData> {
            withLayoutManager {
                return@withLayoutManager StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
            adapter {
                addItem(R.layout.wea_item_place) {
                    bindViewHolder { data, position, holder ->
                        data?.let {item->
                            val bgColor = if (item.skycon.contains("night",ignoreCase = true)) {
                                ContextCompat.getColor(requireContext(),R.color.colorPrimaryDarkNight)
                            } else {
                                ContextCompat.getColor(requireContext(),R.color.bluebackground)
                            }
                            setBackgroundColor(R.id.location_card,bgColor)
                            setVisible(R.id.location_tag,item.isLocation)
                            setText(R.id.location_name,item.name)
                            setText(R.id.location_temperature,"${item.temperature} ℃")
                            setImageResource(R.id.location_img, getSky(item.skycon).icon)
                            itemClicked(View.OnClickListener {
                                EventSender.changeCurrentPlace(position)
                                Navigation.findNavController(it).navigateUp()
                            })
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.queryAllChosenPlace()
    }

    private fun initHeaderView() {
        header_view.detail_title.text = "添加的城市"
        header_view.detail_end.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_choosePlaceFragment_to_searchPlaceFragment)
        }
        header_view.detail_start.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.allChosenPlace.observe(this,Observer{
            it?.let {places->
                if (places.size == 0) {
                    Toast.makeText(requireContext(),"请添加城市",Toast.LENGTH_SHORT).show()
                }
                rvChosenPlace.submitList(it)
            }
        })
        EventSender.observePlaceChosen(this,{
//            Log.e("JG","--->observePlaceChosen")
            mViewModel.queryAllChosenPlace()
        })
//        EventSender.observePlaceChange(this,{
//
//        })
    }
}