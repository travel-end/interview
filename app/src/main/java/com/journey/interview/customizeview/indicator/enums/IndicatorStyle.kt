package com.journey.interview.customizeview.indicator.enums

/**
 * @By Journey 2020/9/23
 * @Description
 */
interface IndicatorStyle {
    companion object {
        const val CIRCLE = 0
        const val DASH = 1 shl 1
        const val ROUND_RECT = 1 shl 2
    }
}