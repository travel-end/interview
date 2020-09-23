package com.journey.interview.customizeview.indicator.drawer

import android.graphics.Canvas

/**
 * @By Journey 2020/9/23
 * @Description
 */
interface IDrawer {
    fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)

    fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int): BaseDrawer.MeasureResult

    fun onDraw(canvas: Canvas)
}