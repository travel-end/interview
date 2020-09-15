package com.journey.interview.weatherapp.state

import com.journey.interview.R
import com.kingja.loadsir.callback.Callback

/**
 * @By Journey 2020/9/15
 * @Description
 */
class EmptyCallback:Callback() {
    override fun onCreateView(): Int =R.layout.wea_layout_empty
}