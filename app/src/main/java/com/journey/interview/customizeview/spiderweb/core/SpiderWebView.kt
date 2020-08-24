package com.journey.interview.customizeview.spiderweb.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * @By Journey 2020/8/24
 * @Description
 */
class SpiderWebView : View {
    private lateinit var mSpiderPointList: MutableList<SpiderPoint>
    private lateinit var mConfig: SpiderConfig
    private lateinit var mRandom: Random
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mPointPaint:Paint
    private lateinit var mLinePaint:Paint
    private lateinit var mTouchPaint:Paint

    // 控件宽高
    private var mWidth:Int=0
    private var mHeight:Int = 0

    // 触摸点坐标
    private var mTouchX: Float = -1F
    private var mTouchY: Float = -1F


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mSpiderPointList = mutableListOf()
        mConfig =
            SpiderConfig()
        mRandom = Random()
        mGestureDetector = GestureDetector(context,mSimpleOnGestureListener)
        initPaint()
    }

    private fun initPaint() {
        mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPointPaint.strokeCap = Paint.Cap.ROUND
        mPointPaint.strokeWidth = mConfig.pointRadius.toFloat()
        mPointPaint.color = Color.parseColor("#EBFF4081")

        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint.strokeCap = Paint.Cap.ROUND
        mLinePaint.strokeWidth = mConfig.lineWidth.toFloat()
        mLinePaint.color = Color.parseColor("#EBFF94B9")

        mTouchPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPointPaint.strokeCap = Paint.Cap.ROUND
        mPointPaint.strokeWidth = mConfig.touchPointRadius.toFloat()
        mPointPaint.color = Color.parseColor("#D8FF7875")
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        restart()
    }

    fun restart() {
        resetTouchPoint()
        clearPointList()
        initPoint()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {cvs ->
            cvs.save()
            if (mTouchY != -1F && mTouchX != -1F) {
                cvs.drawPoint(mTouchX,mTouchY,mTouchPaint)
            }
            if (mSpiderPointList.size > 0) {
                var index = 0
                mSpiderPointList.forEach {spiderPoint->
                    spiderPoint.x += spiderPoint.aX
                    spiderPoint.y += spiderPoint.aY

                    // 越界反弹
                    if (spiderPoint.x <= mConfig.pointRadius) {
                        spiderPoint.x = mConfig.pointRadius
                        spiderPoint.aX = -spiderPoint.aX
                    } else if (spiderPoint.x >= mWidth - mConfig.pointRadius) {
                        spiderPoint.x = mWidth - mConfig.pointRadius
                        spiderPoint.aX = -spiderPoint.aX
                    }

                    if (spiderPoint.y <= mConfig.pointRadius) {
                        spiderPoint.y = mConfig.pointRadius
                        spiderPoint.aY = -spiderPoint.aY
                    } else if (spiderPoint.y >= mHeight - mConfig.pointRadius) {
                        spiderPoint.y = mHeight - mConfig.pointRadius
                        spiderPoint.aY = -spiderPoint.aY
                    }
                    // 绘制触摸点与其他点的连线
                    if (mTouchX != -1f && mTouchY != -1f) {
                        val offsetX = (mTouchX - spiderPoint.x).toInt()
                        val offsetY = (mTouchY - spiderPoint.y).toInt()
                        val distance =
                            sqrt(offsetX * offsetX + offsetY * offsetY.toDouble()).toInt()
                        if (distance < mConfig.maxDistance) {
                            if (distance >= mConfig.maxDistance - mConfig.gravitationStrength) {
                                if (spiderPoint.x > mTouchX) {
                                    spiderPoint.x -= (0.03 * -offsetX).toInt()
                                } else {
                                    spiderPoint.x += (0.03f * offsetX).toInt()
                                }
                                if (spiderPoint.y > mTouchY) {
                                    spiderPoint.y -= (0.03f * -offsetY).toInt()
                                } else {
                                    spiderPoint.y += (0.03f * offsetY).toInt()
                                }
                            }
                            val alpha = ((1.0f - distance.toFloat() / mConfig.maxDistance) * mConfig.lineAlpha).toInt()
                            mLinePaint.color = spiderPoint.color
                            mLinePaint.alpha = alpha
                            canvas.drawLine(
                                spiderPoint.x.toFloat(),
                                spiderPoint.y.toFloat(),
                                mTouchX,
                                mTouchY,
                                mLinePaint
                            )
                        }
                    }
                    // 绘制小点
                    Log.d("JG","color=${spiderPoint.color}")
                    mPointPaint.color = spiderPoint.color
                    cvs.drawCircle(spiderPoint.x.toFloat(),spiderPoint.y.toFloat(),mConfig.pointRadius.toFloat(),mPointPaint)

                    // 绘制连线
                    for (i in index until mSpiderPointList.size) {
                        val point = mSpiderPointList[i]
                        // 判定当前点与其他点之间的距离
                        if (spiderPoint != point) {
                            val distance: Int = disPos2d(
                                point.x.toFloat(),
                                point.y.toFloat(),
                                spiderPoint.x.toFloat(),
                                spiderPoint.y.toFloat()
                            )
                            if (distance < mConfig.maxDistance) {
                                // 绘制小点间的连线
                                val alpha =
                                    ((1.0f - distance.toFloat() / mConfig.maxDistance) * mConfig.lineAlpha).toInt()
                                mLinePaint.color = point.color
                                mLinePaint.alpha = alpha
                                canvas.drawLine(
                                    spiderPoint.x.toFloat(),
                                    spiderPoint.y.toFloat(),
                                    point.x.toFloat(),
                                    point.y.toFloat(),
                                    mLinePaint
                                )
                            }
                        }
                    }

                    index++
                }
                cvs.restore()
                invalidate()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {ev->
            if (ev.action == MotionEvent.ACTION_UP
                || ev.action == MotionEvent.ACTION_CANCEL) {
                resetTouchPoint()
                return true
            }
        }

        return mGestureDetector.onTouchEvent(event)
    }

    // 重置触摸点
    fun resetTouchPoint() {
        mTouchX = -1F
        mTouchY = -1F
    }

    private fun clearPointList() {
        mSpiderPointList.clear()
    }

    private fun initPoint() {
        for (i in 0..mConfig.pointNum) {
            val width = (mRandom.nextFloat()*mWidth).toInt()
            val height = (mRandom.nextFloat()*mHeight).toInt()
            val point =
                SpiderPoint(
                    width,
                    height
                )
            var aX = 0
            var aY = 0
            while (aX == 0) {
                aX = ((mRandom.nextFloat() - 0.5F) * mConfig.pointAcceleration).toInt()
            }
            while (aY == 0) {
                aY = ((mRandom.nextFloat() - 0.5F) * mConfig.pointAcceleration).toInt()
            }
            point.aX = aX
            point.aY = aY

            point.color = randomRGB()
            mSpiderPointList.add(point)
        }
    }


    // 手势：用于处理滑动与拖拽
    private val mSimpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            e2?.let {
                mTouchX = it.x
                mTouchY = it.y
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1.pointerCount == e2.pointerCount && e1.pointerCount == 1) {
                mTouchX = e2.x
                mTouchY = e2.y
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDown(e: MotionEvent): Boolean {
            mTouchX = e.x
            mTouchY = e.y
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }
    }

    private fun randomRGB():Int {
        val random = Random()
        return Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255))
    }

    private fun disPos2d(x1:Float,y1:Float,x2:Float,y2:Float) :Int {
        return sqrt((x1 - x2) * (x1- x2) * (y1 - y2) * (y1-y2)).toInt()
    }

    private fun rangeInt(s:Int,e:Int):Int {
        val max = max(s,e)
        val min  = min(s,e) -1
        return (min + ceil(Math.random() * (max -min))).toInt()
    }

    // 设置相关配置参数
    fun getPointRadius(): Int {
        return mConfig.pointRadius
    }

    fun setPointRadius(pointRadius: Int) {
        mConfig.pointRadius = pointRadius
        mPointPaint.strokeWidth = mConfig.pointRadius.toFloat()
    }

    fun getLineWidth(): Int {
        return mConfig.lineWidth
    }

    fun setLineWidth(lineWidth: Int) {
        mConfig.lineWidth = lineWidth
        mLinePaint.strokeWidth = mConfig.lineWidth.toFloat()
    }

    fun getLineAlpha(): Int {
        return mConfig.lineAlpha
    }

    fun setLineAlpha(lineAlpha: Int) {
        mConfig.lineAlpha = lineAlpha
    }

    fun getPointNum(): Int {
        return mConfig.pointNum
    }

    fun setPointNum(pointNum: Int) {
        mConfig.pointNum = pointNum
        restart()
    }

    fun getPointAcceleration(): Int {
        return mConfig.pointAcceleration
    }

    fun setPointAcceleration(pointAcceleration: Int) {
        mConfig.pointAcceleration = pointAcceleration
        restart()
    }

    fun getMaxDistance(): Int {
        return mConfig.maxDistance
    }

    fun setMaxDistance(maxDistance: Int) {
        mConfig.maxDistance = maxDistance
    }

    fun getTouchPointRadius(): Int {
        return mConfig.touchPointRadius
    }

    fun setTouchPointRadius(touchPointRadius: Int) {
        mConfig.touchPointRadius = touchPointRadius
        mTouchPaint.strokeWidth = mConfig.touchPointRadius.toFloat()
    }

    fun getGravitationStrength(): Int {
        return mConfig.gravitationStrength
    }

    fun setGravitationStrength(gravitation_strength: Int) {
        mConfig.gravitationStrength = gravitation_strength
    }
}