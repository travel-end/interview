package com.journey.interview.customizeview.indicator.drawer

import android.graphics.Canvas
import com.journey.interview.customizeview.indicator.IndicatorOptions

/**
 * @By Journey 2020/9/23
 * @Description
 */
class RoundRectDrawer internal constructor(indicatorOptions: IndicatorOptions) : RectDrawer(indicatorOptions) {

    override fun drawRoundRect(canvas: Canvas, rx: Float, ry: Float) {
        canvas.drawRoundRect(mRectF, rx, ry, mPaint)
    }
}
