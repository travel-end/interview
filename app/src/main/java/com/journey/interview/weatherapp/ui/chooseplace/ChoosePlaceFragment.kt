package com.journey.interview.weatherapp.ui.chooseplace

import android.view.View
import androidx.navigation.Navigation
import com.journey.interview.R
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.wea_frg_choose_place.*
import kotlinx.android.synthetic.main.wea_header_view.view.*

/**
 * @By Journey 2020/9/15
 * @Description 列表显示当前已经添加的城市
 */
class ChoosePlaceFragment:BaseLifeCycleFragment<ChoosePlaceViewModel>() {
    private lateinit var headerView:View
    override fun layoutResId()= R.layout.wea_frg_choose_place
    override fun initView() {
        super.initView()


        initHeaderView()
    }

    override fun initData() {
        super.initData()
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
}