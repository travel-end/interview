package com.journey.interview.imusic.frg

import com.journey.interview.R
import com.journey.interview.imusic.vm.ISearchViewModel
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment

/**
 * @By Journey 2020/9/25
 * @Description
 */
class ISearchFragment:BaseLifeCycleFragment<ISearchViewModel>() {
    override fun layoutResId()= R.layout.imusic_search
    override fun initView() {
        super.initView()
        childFragmentManager.beginTransaction().replace(R.id.search_container,IHotSearchFragment.newInstance()).commitAllowingStateLoss()
    }
}