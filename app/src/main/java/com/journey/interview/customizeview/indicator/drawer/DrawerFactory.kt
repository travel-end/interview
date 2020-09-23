package com.journey.interview.customizeview.indicator.drawer

import com.journey.interview.customizeview.indicator.IndicatorOptions
import com.journey.interview.customizeview.indicator.enums.IndicatorStyle

/**
 * @By Journey 2020/9/23
 * @Description
 */
internal object DrawerFactory {
    fun createDrawer(indicatorOptions: IndicatorOptions): IDrawer {
        return when (indicatorOptions.indicatorStyle) {
            IndicatorStyle.DASH -> DashDrawer(indicatorOptions)
            IndicatorStyle.ROUND_RECT -> RoundRectDrawer(indicatorOptions)
            else -> CircleDrawer(indicatorOptions)
        }
    }
}