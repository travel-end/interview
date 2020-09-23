package com.journey.interview.customizeview.indicator.drawer

import android.graphics.Canvas
import com.journey.interview.customizeview.indicator.IndicatorOptions

/**
 * @By Journey 2020/9/23
 * @Description
 */
class DashDrawer internal constructor(indicatorOptions: IndicatorOptions) : RectDrawer(indicatorOptions) {

    override fun drawDash(canvas: Canvas) {
        canvas.drawRect(mRectF, mPaint)
    }
}