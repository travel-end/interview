package com.journey.interview.customizeview.levelselectview

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt

/**
 * @By Journey 2020/9/9
 * @Description
 */
class LevelSelectView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attributeSet, defStyleAttr) {
    private lateinit var mLinePaint: Paint
    private lateinit var mIndicatorPaint: Paint
    private lateinit var mDotPaint: Paint

    // 控件宽高
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    // 指示器圆心坐标
    private var mIndicatorX: Float = 0f
    private var mIndicatorY: Float = 0f

    // 指示器半径
    private var mIndicatorRadius: Int = 30

    // 初始指示器半径
    private var mIndicatorInitRadius: Int = 30

    // 指示器较上一位置的偏移距离
    private var mIndicatorOffset: Float = 0f

    // 指示器移动动画、缩放动画
    private var mIndicatorTransAnimator: ValueAnimator? = null
    private var mIndicatorScaleAnimator: ValueAnimator? = null

    // 线段段数
    private var mLevelCount = 3

    // 线段线条粗细
    private var mLineStrokeWidth: Float = 4f

    // 线段背景高度
    private var mLineBgHeight: Int = 0

    // 每条线段的长度
    private var mLineSegmentWidth: Int = 0

    // 线段点半径
    private var dotRadius: Int = 10

    //指示器左边的线条渐变色的开始颜色
    private val leftLineStartColor: Long = 0xff7FFFD4

    // 指示器左边的线条渐变色的结束颜色
    private val leftLineEndColor: Long = 0xffFF4500

    companion object {

        // 线段颜色
        @ColorInt
        const val lineSegmentColor = Color.BLACK

        // 指示器颜色
        @ColorInt
        const val indicatorColor = Color.WHITE


        // 阴影颜色
        @ColorInt
        const val shadowColor = 0xffe0e0e0

        // 指示器中心颜色
        @ColorInt
        const val indicatorDotColor = 0x330000FF

        // 线段点颜色
        @ColorInt
        const val dotColor = Color.GRAY


    }

    init {

        /**
         *  Paint.Style.STROKE 只绘制图形轮廓（描边）
        Paint.Style.FILL 只绘制图形内容
        Paint.Style.FILL_AND_STROKE 既绘制轮廓也绘制内容
         */
        mLinePaint = Paint().apply {
            color = lineSegmentColor
            style = Paint.Style.STROKE
            strokeWidth = mLineStrokeWidth
            isAntiAlias = true
        }
        mIndicatorPaint = Paint().apply {
            style = Paint.Style.FILL
            color = indicatorColor
            isAntiAlias = true
        }
        mDotPaint = Paint().apply {
            style = Paint.Style.FILL
            color = dotColor
            isAntiAlias = true
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.e("JG", "onSizeChanged --> w=$w , h=$h")//w=720 , h=176
        this.mWidth = w
        this.mHeight = h
        mIndicatorInitRadius = mHeight / 6 // 28
        mIndicatorRadius = mIndicatorInitRadius
        dotRadius = mIndicatorRadius / 6 // 4
        mIndicatorX = 2 * mIndicatorInitRadius + mIndicatorOffset
        mIndicatorY = mHeight / 2.toFloat()
        mLineSegmentWidth = (mWidth - 4 * mIndicatorInitRadius) / mLevelCount // 202
        mLineBgHeight = mIndicatorInitRadius
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { cvs ->
            setLayerType(LAYER_TYPE_SOFTWARE, null)
            // 画线段点、线段
            for (i in 0..mLevelCount) {
                cvs.drawCircle(
                    (mLineSegmentWidth * i + 2 * mIndicatorInitRadius).toFloat(),
                    (mHeight / 2).toFloat(),
                    dotRadius.toFloat(),
                    mDotPaint
                )
            }

            // 画背景线条
            drawBgLine(cvs)

            // 画指示器
            drawIndicator(cvs)
        }
    }

    private fun drawBgLine(canvas: Canvas) {
        canvas.save()
        val rectF = RectF(
            (2 * mIndicatorInitRadius - mLineBgHeight / 2).toFloat(),
            mIndicatorY - mLineBgHeight / 2,
            (mWidth - 2 * mIndicatorInitRadius + mLineBgHeight / 2).toFloat(),
            mIndicatorY + mLineBgHeight / 2
        )
        //设置内阴影
        val path = Path()
        path.addRoundRect(
            rectF,
            (mLineBgHeight / 2).toFloat(),
            (mLineBgHeight / 2).toFloat(),
            Path.Direction.CW
        )
        mLinePaint.style = Paint.Style.FILL
        mLinePaint.clearShadowLayer()
        mLinePaint.color = -0x771f1f20
        canvas.drawPath(path, mLinePaint)
        //绘制指示器左边的线段背景
        drawIndicatorLeftLineBg(canvas)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLinePaint.setShadowLayer(3f, 0f, 2f, shadowColor)
        }
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = -0x1f1f20
        canvas.clipPath(path)
        canvas.drawPath(path, mLinePaint)
        canvas.restore()
    }

    private fun drawIndicatorLeftLineBg(canvas: Canvas) {
        val linearGradient = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            LinearGradient(
                0f,
                0f,
                mWidth.toFloat(),
                0f,
                leftLineStartColor,
                leftLineEndColor,
                Shader.TileMode.CLAMP
            )
        } else {
            null
        }
        mLinePaint.shader = linearGradient
        mLinePaint.style = Paint.Style.FILL
        mLinePaint.clearShadowLayer()
        val path = Path()
        val rectF = RectF(
            (2 * mIndicatorInitRadius - mLineBgHeight / 2).toFloat(),
            mIndicatorY - mLineBgHeight / 2,
            mIndicatorX + mIndicatorOffset + mLineBgHeight / 2,
            mIndicatorY + mLineBgHeight / 2
        )
        path.addRoundRect(
            rectF,
            mLineBgHeight / 2.toFloat(),
            mLineBgHeight / 2.toFloat(),
            Path.Direction.CW
        )
        canvas.drawPath(path, mLinePaint)
        mLinePaint.clearShadowLayer()
        mLinePaint.shader = null
    }

    private fun drawIndicator(canvas: Canvas) {
        mIndicatorPaint.color = Color.WHITE
        mIndicatorPaint.style = Paint.Style.FILL
        mIndicatorPaint.setShadowLayer(3f, 0f, 2f, Color.GRAY)
        canvas.drawCircle(mIndicatorX + mIndicatorOffset, mIndicatorY, mIndicatorRadius.toFloat(), mIndicatorPaint)
        mIndicatorPaint.color = indicatorDotColor
        mIndicatorPaint.style = Paint.Style.FILL
        mIndicatorPaint.clearShadowLayer()
        canvas.drawCircle(
            mIndicatorX + mIndicatorOffset,
            mIndicatorY,
            (mIndicatorRadius / 2).toFloat(),
            mIndicatorPaint
        )

        //绘制内阴影
        canvas.save()
        val path = Path()
        path.addCircle(
            mIndicatorX + mIndicatorOffset,
            mIndicatorY,
            (mIndicatorRadius / 2).toFloat(),
            Path.Direction.CW
        )
        mIndicatorPaint.color = Color.WHITE
        mIndicatorPaint.setShadowLayer(2f, 0f, 2f, Color.GRAY)
        mIndicatorPaint.style = Paint.Style.STROKE
        canvas.clipPath(path)
        canvas.drawPath(path, mIndicatorPaint)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    /**
     * 缩小指示器动画
     */
    private fun scaleInIndicator() {
        if (mIndicatorScaleAnimator != null) {
            mIndicatorScaleAnimator?.cancel()
        }
        mIndicatorScaleAnimator = ValueAnimator.ofFloat(mIndicatorRadius.toFloat(), mIndicatorInitRadius.toFloat())
        mIndicatorScaleAnimator?.duration = 300L
        mIndicatorScaleAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            mIndicatorRadius = animation.animatedValue as Int
            postInvalidate()
        })
        mIndicatorScaleAnimator?.start()
    }

}