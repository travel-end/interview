package com.journey.interview.imusic

import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.R
import com.journey.interview.weatherapp.base.BaseActivity

/**
 * @By Journey 2020/9/24
 * @Description
 */
class IWelcomeActivity:BaseActivity<IWelcomeViewModel>() {
    override fun layoutResId()= R.layout.imusic_act_wel
    override fun initView() {
        super.initView()
        ImmersionBar.with(this).transparentBar().init()
    }
}