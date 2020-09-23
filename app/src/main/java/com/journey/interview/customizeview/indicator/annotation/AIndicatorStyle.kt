package com.journey.interview.customizeview.indicator.annotation

import androidx.annotation.IntDef
import com.journey.interview.customizeview.indicator.enums.IndicatorStyle

/**
 * @By Journey 2020/9/23
 * @Description
 */
@IntDef(IndicatorStyle.CIRCLE, IndicatorStyle.DASH, IndicatorStyle.ROUND_RECT)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class AIndicatorStyle