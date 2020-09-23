package com.journey.interview.customizeview.indicator.drawer

import android.graphics.Canvas
import android.graphics.RectF
import com.journey.interview.customizeview.indicator.IndicatorOptions
import com.journey.interview.customizeview.indicator.IndicatorUtils
import com.journey.interview.customizeview.indicator.enums.IndicatorSlideMode

/**
 * @By Journey 2020/9/23
 * @Description
 */
class CircleDrawer internal constructor(indicatorOptions: IndicatorOptions) : BaseDrawer(indicatorOptions) {

    private val rectF = RectF()

    override fun measureHeight(): Int {
        return maxWidth.toInt()
    }

    override fun onDraw(canvas: Canvas) {
        if (mIndicatorOptions.pageSize > 1) {
            drawNormal(canvas)
            drawSlider(canvas)
        }
    }

    private fun drawNormal(canvas: Canvas) {
        val normalIndicatorWidth = mIndicatorOptions.normalSliderWidth
        mPaint.color = mIndicatorOptions.normalSliderColor
        for (i in 0 until mIndicatorOptions.pageSize) {
            val coordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, i)
            val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
            drawCircle(canvas, coordinateX, coordinateY, normalIndicatorWidth / 2)
        }
    }

    private fun drawSlider(canvas: Canvas) {
        mPaint.color = mIndicatorOptions.checkedSliderColor
        when (mIndicatorOptions.slideMode) {
            IndicatorSlideMode.NORMAL, IndicatorSlideMode.SMOOTH -> drawCircleSlider(canvas)
            IndicatorSlideMode.WORM -> drawWormSlider(canvas)
            IndicatorSlideMode.SCALE -> drawScaleSlider(canvas)
            IndicatorSlideMode.COLOR -> drawColor(canvas)
        }
    }

    private fun drawColor(canvas: Canvas) {
        val currentPosition = mIndicatorOptions.currentPosition
        val slideProgress = mIndicatorOptions.slideProgress
        val coordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
        var evaluate = argbEvaluator?.evaluate(slideProgress, mIndicatorOptions.checkedSliderColor, mIndicatorOptions.normalSliderColor)
        mPaint.color = (evaluate as Int)
        drawCircle(canvas, coordinateX, coordinateY, mIndicatorOptions.normalSliderWidth / 2)

        // 绘制可循环的ViewPager指示器渐变
        evaluate = argbEvaluator?.evaluate(1 - slideProgress, mIndicatorOptions.checkedSliderColor, mIndicatorOptions.normalSliderColor)
        mPaint.color = evaluate as Int
        val nextCoordinateX = if (currentPosition == mIndicatorOptions.pageSize - 1) {
            IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, 0)
        } else {
            coordinateX + mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
        }
        drawCircle(canvas, nextCoordinateX, coordinateY, mIndicatorOptions.checkedSliderWidth / 2)

    }

    private fun drawScaleSlider(canvas: Canvas) {
        val currentPosition = mIndicatorOptions.currentPosition
        val slideProgress = mIndicatorOptions.slideProgress
        val coordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
        if (slideProgress < 1) {
            val evaluate = argbEvaluator?.evaluate(slideProgress, mIndicatorOptions.checkedSliderColor, mIndicatorOptions.normalSliderColor)
            mPaint.color = (evaluate as Int)
            val radius = mIndicatorOptions.checkedSliderWidth / 2 - (mIndicatorOptions.checkedSliderWidth / 2 - mIndicatorOptions.normalSliderWidth / 2) * slideProgress
            drawCircle(canvas, coordinateX, coordinateY, radius)
        }

        if (currentPosition == mIndicatorOptions.pageSize - 1) {
            val evaluate = argbEvaluator?.evaluate(slideProgress, mIndicatorOptions.normalSliderColor, mIndicatorOptions.checkedSliderColor)
            mPaint.color = evaluate as Int
            val nextCoordinateX = maxWidth / 2
            val nextRadius = minWidth / 2 + (maxWidth / 2 - minWidth / 2) * (slideProgress)
            drawCircle(canvas, nextCoordinateX, coordinateY, nextRadius)
        } else {
            if (slideProgress > 0) {
                val evaluate = argbEvaluator?.evaluate(slideProgress, mIndicatorOptions.normalSliderColor, mIndicatorOptions.checkedSliderColor)
                mPaint.color = evaluate as Int
                val nextCoordinateX = coordinateX + mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
                val nextRadius = mIndicatorOptions.normalSliderWidth / 2 + (mIndicatorOptions.checkedSliderWidth / 2 - mIndicatorOptions.normalSliderWidth / 2) * slideProgress
                drawCircle(canvas, nextCoordinateX, coordinateY, nextRadius)
            }
        }
    }

    private fun drawCircleSlider(canvas: Canvas) {
        val currentPosition = mIndicatorOptions.currentPosition
        val startCoordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val endCoordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, (currentPosition + 1) % mIndicatorOptions.pageSize)
        val coordinateX = startCoordinateX + (endCoordinateX - startCoordinateX) * mIndicatorOptions.slideProgress
        val coordinateY = IndicatorUtils.getCoordinateY(maxWidth)
        val radius = mIndicatorOptions.checkedSliderWidth / 2
        drawCircle(canvas, coordinateX, coordinateY, radius)
    }

    private fun drawWormSlider(canvas: Canvas) {
        val sliderHeight = mIndicatorOptions.normalSliderWidth
        val slideProgress = mIndicatorOptions.slideProgress
        val currentPosition = mIndicatorOptions.currentPosition
        val distance = mIndicatorOptions.sliderGap + mIndicatorOptions.normalSliderWidth
        val startCoordinateX = IndicatorUtils.getCoordinateX(mIndicatorOptions, maxWidth, currentPosition)
        val left = startCoordinateX + Math.max(distance * (slideProgress - 0.5f) * 2.0f, 0f) - mIndicatorOptions.normalSliderWidth / 2
        val right = startCoordinateX + Math.min(distance * slideProgress * 2f, distance) + mIndicatorOptions.normalSliderWidth / 2
        rectF.set(left, 0f, right, sliderHeight)
        canvas.drawRoundRect(rectF, sliderHeight, sliderHeight, mPaint)
    }

    private fun drawCircle(canvas: Canvas, coordinateX: Float, coordinateY: Float, radius: Float) {
        canvas.drawCircle(coordinateX, coordinateY, radius, mPaint)
    }
}
