package com.journey.interview.customizeview.cropimageview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.LruCache
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.journey.interview.utils.toFloatPx

/**
 * @By Journey 2020/8/26
 * @Description
 * @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
)
(context, attributeSet, defStyleAttr)
 */
class MCropImageView : AppCompatImageView {
    // 手势帮助类
    private lateinit var mScaleGestureDetector:ScaleGestureDetector
    private lateinit var mGestureDetector:GestureDetector
    private var mMatrix:Matrix?=null
    private lateinit var mLinePaint: Paint
    // 图片缓存
    private lateinit var mLruCache: LruCache<String,Bitmap>

    private var mFirstLayout:Boolean = true
    private var mBaseScale:Float = 1.0f
    private var mMaxScale:Float = 3.0f
    private var mPreScaleFactor:Float = 1.0f
    // 缩放手势（两个手指）中点位置
    private var mLastFocusX:Float = 0f
    private var mLastFocusY:Float = 0f

    private var mTouchSlop:Int = -1

    private var mCurrentScaleAnimCount:Int = 0
    private var mBoundAnimator:ValueAnimator?=null

    // 是否绘制线条
    private var mIsDragging:Boolean = false

    private var mIsWidthLarger :Boolean = false
    private var mIsCrop:Boolean = false
    // 是否留白
    private var mIsLeaveBlank:Boolean = false
    companion object{
        const val MAX_SCROLL_FACTOR = 3
        const val DAMP_FACTOR = 9.0f // 阻尼系数
        const val SCALE_ANIM_COUNT = 10
        const val ZOOM_OUT_ANIM_WHIT = 0
        const val ZOOM_ANIM_WHIT = 1
        const val LINE_ROW_NUMBER = 3
        const val LINE_COLUMN_NUMBER =3
    }
    constructor(context: Context):super(context) {
        init(context)
    }
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet) {
        init(context)
    }
    constructor(context: Context,attributeSet: AttributeSet,defStyleAttr:Int):super(context,attributeSet,defStyleAttr){
        init(context)
    }

    private fun init(context: Context) {
        mScaleGestureDetector = ScaleGestureDetector(context,mOnScaleGestureListener)
        mGestureDetector = GestureDetector(context,mSimpleOnGestureListener)
        mFirstLayout = true
        if (mMatrix == null) {
            mMatrix = Matrix()
        }
        scaleType = ScaleType.MATRIX
        mLinePaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            strokeWidth = 0.5f.toFloatPx()
        }
        // 根据实际情况 设置maxSize大小
        mLruCache = LruCache(Int.MAX_VALUE)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {ev->
            if (mTouchSlop < 0) {
                mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
            }
            when(ev.action) {
                MotionEvent.ACTION_DOWN->{
                    // 防止父类拦截事件
                    parent.requestDisallowInterceptTouchEvent(true)
                    mIsDragging = false
                }
                MotionEvent.ACTION_MOVE->{

                }
                MotionEvent.ACTION_CANCEL->{

                }
                MotionEvent.ACTION_UP->{
                    mPreScaleFactor = 1.0f
                    mCurrentScaleAnimCount = 0
                    mIsDragging = false
                    invalidate()
                    val scale = getScale()
                    if (scale > mMaxScale) {
                        // 缩小 SCALE_ANIM_COUNT = 10

                    }
                }
                else ->{}

            }
        }


        return super.onTouchEvent(event)
    }

    // 获取图片缩放比
    private fun getScale():Float {
        val values = FloatArray(9)
        mMatrix?.getValues(values)
        return values[Matrix.MSCALE_X]
    }

    private fun boundCheck(){
        // 获取矩阵图片
        val rectF = getMatrixRectF()
        rectF.run {
            val rectWidth = right - left
            val rectHeight = bottom - top

            // 获取到左右留白的长度
            var leftLeaveBlankLength = ((width -rectWidth) / 2).toInt()
            leftLeaveBlankLength = if (leftLeaveBlankLength <= 0) 0 else leftLeaveBlankLength
            val leftBound = if (mIsLeaveBlank) leftLeaveBlankLength.toFloat() else 0f
            if (left >= leftBound) {
                // 左边越界
                startBoundAnimator(left,leftBound,true)
            }
            val rightBound = if (mIsLeaveBlank) {
                width - leftLeaveBlankLength
            } else {
                width
            }
            if (right <= rightBound) {
                startBoundAnimator(left,rightBound - rectWidth,true)
            }
            // 同理获取上线留白长度
            var topLeaveBlankLength = ((height -rectHeight) / 2).toInt()
            topLeaveBlankLength = if (topLeaveBlankLength <= 0) 0 else topLeaveBlankLength
            val topBound = if (mIsLeaveBlank) topLeaveBlankLength.toFloat() else 0f
            if (top >= topBound) {
                // 左边越界
                startBoundAnimator(top,topBound,false)
            }
            val bottomBound = if (mIsLeaveBlank) {
                height - topLeaveBlankLength
            } else {
                height
            }
            if (bottom <= bottomBound) {
                startBoundAnimator(top,bottomBound - rectHeight,false)
            }
        }
    }

    // 获取图片矩阵区域
    private fun getMatrixRectF():RectF{
        val rectF = RectF()
        if (drawable != null) {
            rectF.set(0f,0f,drawable.intrinsicWidth.toFloat(),drawable.intrinsicHeight.toFloat())
            mMatrix?.mapRect(rectF)
        }
        return rectF
    }

    // 开始越界动画
    private fun startBoundAnimator(start:Float,end:Float, horizontal:Boolean) {
        if (mBoundAnimator == null) {
            mBoundAnimator = ValueAnimator.ofFloat(start,end).apply {
                duration = 200
                interpolator = LinearInterpolator()
                addUpdateListener {anim->
                    if (anim != null) {
                        val value = anim.animatedValue as Float
                        val vs = FloatArray(9)
                        mMatrix?.getValues(vs)
                        if (horizontal) {
                            vs[Matrix.MSCALE_X] = value
                        } else {
                            vs[Matrix.MSCALE_Y] = value
                        }
                        mMatrix?.setValues(vs)
                        imageMatrix = mMatrix
                    }
                }
            }
        }
        mBoundAnimator?.start()
    }


    // 处理双指的缩放
    private val mOnScaleGestureListener = object :ScaleGestureDetector.OnScaleGestureListener{
        override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(p0: ScaleGestureDetector?) {
        }

        override fun onScale(p0: ScaleGestureDetector?): Boolean {
            if (drawable == null || mMatrix == null) {
                // 如果返回true那么detector就会重置缩放事件
                return true
            }
            mIsDragging = true
            //

            return false
        }

    }
    // 处理手指滑动
    private val mSimpleOnGestureListener = object :GestureDetector.SimpleOnGestureListener(){
        override fun onDown(e: MotionEvent?): Boolean=true
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    private fun sendScaleMessage(relativeScale:Float,what:Int,delayMillis:Long) {
        val msg = Message()
        msg.obj = relativeScale
        msg.what = what
        mHandler.sendMessageDelayed(msg,delayMillis)
    }


    private  val mHandler by lazy {
        object :Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (mCurrentScaleAnimCount < SCALE_ANIM_COUNT) {
                    val obj = msg.obj as Float
                    mMatrix?.postScale(obj,obj,mLastFocusX,mLastFocusY)
                    imageMatrix = mMatrix
                    mCurrentScaleAnimCount++
                    sendScaleMessage(obj,msg.what, SCALE_ANIM_COUNT.toLong())
                } else if (mCurrentScaleAnimCount >= SCALE_ANIM_COUNT) {
                    val values = FloatArray(9)
                    mMatrix?.getValues(values)
                    mCurrentScaleAnimCount = 0
                    if (msg.what == ZOOM_OUT_ANIM_WHIT) {
                        values[Matrix.MSCALE_X] = mMaxScale
                        values[Matrix.MSCALE_Y] = mMaxScale
                    } else if (msg.what == ZOOM_ANIM_WHIT) {
                        values[Matrix.MSCALE_X] = mBaseScale
                        values[Matrix.MSCALE_Y] = mBaseScale
                    }
                    mMatrix?.setValues(values)
                    imageMatrix = mMatrix
                    // 边界检测
                    boundCheck()
                }
            }
        }
    }


}