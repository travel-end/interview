package com.journey.interview.customizeview.indicator

import androidx.viewpager.widget.ViewPager

/**
 * @By Journey 2020/9/23
 * @Description
 */
interface IIndicator: ViewPager.OnPageChangeListener  {
    fun notifyDataChanged()

    fun setIndicatorOptions(options: IndicatorOptions)
}