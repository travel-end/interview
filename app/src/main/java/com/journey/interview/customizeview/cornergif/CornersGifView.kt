package com.journey.interview.customizeview.cornergif

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewParent
import androidx.appcompat.widget.AppCompatImageView
import com.journey.interview.R

/**
 * @By Journey 2020/8/24
 * @Description 自定义圆角gifView
 */
class CornersGifView:AppCompatImageView {
    private lateinit var mPath :Path
    private lateinit var mPaint:Paint
    private var mCornerSize:Int = 0
    private var mLeftBottomCorner:Int = 0
    private var mLeftTopCorner:Int = 0
    private var mRightBottomCorner:Int = 0
    private var mRightTopCorner:Int = 0
    private var mCorners:FloatArray?=null

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
        mPath = Path()
        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD
        mPaint = Paint()
        mPaint.isAntiAlias = true
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CornersGifView)
        mCornerSize = ta.getDimension(R.styleable.CornersGifView_cornerSize, 0f).toInt()
        mLeftBottomCorner = ta.getDimension(R.styleable.CornersGifView_leftBottomCorner, 0f).toInt()
        mLeftTopCorner = ta.getDimension(R.styleable.CornersGifView_leftTopCorner, 0f).toInt()
        mRightBottomCorner = ta.getDimension(R.styleable.CornersGifView_rightBottomCorner, 0f).toInt()
        mRightTopCorner = ta.getDimension(R.styleable.CornersGifView_rightTopCorner, 0f).toInt()
        ta.recycle()
        mCorners = if (mCornerSize == 0) {
            floatArrayOf(
                mLeftTopCorner.toFloat(), mLeftTopCorner.toFloat(),
                mRightTopCorner.toFloat(), mRightTopCorner.toFloat(),
                mRightBottomCorner.toFloat(), mRightBottomCorner.toFloat(),
                mLeftBottomCorner.toFloat(), mLeftBottomCorner.toFloat()
            )
        } else {
            floatArrayOf(
                mCornerSize.toFloat(), mCornerSize.toFloat(),
                mCornerSize.toFloat(), mCornerSize.toFloat(),
                mCornerSize.toFloat(), mCornerSize.toFloat(),
                mCornerSize.toFloat(), mCornerSize.toFloat()
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initPaintColor()
        addRoundRectPath(w,h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {cvs->
            cvs.save()
            cvs.drawPath(mPath,mPaint)
            cvs.restore()
        }
    }

    private fun initPaintColor() {
        var paintColor = getPaintColor(parent)
        if (Color.TRANSPARENT == paintColor) {
            // get theme background color
            val array = context.theme.obtainStyledAttributes(
                intArrayOf(
                    android.R.attr.colorBackground
                )
            )
            paintColor = array.getColor(0, Color.TRANSPARENT)
            array.recycle()
        }
        mPaint.color = paintColor
    }

    private fun addRoundRectPath(width:Int,height:Int) {
        mPath.reset()
        mPath.addRoundRect(RectF(0f,0f,width.toFloat(),height.toFloat()),mCorners!!,Path.Direction.CCW)
    }

    private fun getPaintColor(vp:ViewParent?):Int {
        if (vp == null) {
            return Color.TRANSPARENT
        }
        if (vp is View) {
            val parentView = vp as View
            val color = getViewBackgroundColor(parentView)
            if (Color.TRANSPARENT != color) {
                return color
            } else {
                getPaintColor(parentView.parent)
            }
        }
        return Color.TRANSPARENT
    }

    private fun getViewBackgroundColor(view:View):Int {
        val drawable = view.background
        if (drawable != null) {
            val drawableClass = drawable.javaClass
            try {
                val field = drawableClass.getDeclaredField("mColorState")
                field.isAccessible = true
                val colorState = field.get(drawable)
                val colorStateClass = colorState.javaClass
                val colorStateField = colorStateClass.getDeclaredField("mUserColor")
                colorStateField.isAccessible = true
                val viewColor = colorStateField.get(colorState) as Int
                if (Color.TRANSPARENT != viewColor) {
                    return viewColor
                }
            } catch (e:NoSuchFieldException) {

            } catch (e:IllegalAccessException) {

            }
        }
        return Color.TRANSPARENT
    }

    private fun setCornerSize(
        leftTop: Int,
        leftBottom: Int,
        rightTop: Int,
        rightBottom: Int
    ) {
        mCorners = floatArrayOf(
            leftTop.toFloat(), leftTop.toFloat(),
            rightTop.toFloat(), rightTop.toFloat(),
            rightBottom.toFloat(), rightBottom.toFloat(),
            leftBottom.toFloat(), leftBottom.toFloat()
        )
        addRoundRectPath(width, height)
        invalidate()
    }

    fun getCornerSize(): Int {
        return mCornerSize
    }

    fun setCornerSize(cornerSize: Int) {
        mCornerSize = cornerSize
        setCornerSize(cornerSize, cornerSize, cornerSize, cornerSize)
    }

    fun getLeftBottomCorner(): Int {
        return mLeftBottomCorner
    }

    fun setLeftBottomCorner(leftBottomCorner: Int) {
        mLeftBottomCorner = leftBottomCorner
        setCornerSize(mLeftTopCorner, mLeftBottomCorner, mRightTopCorner, mRightBottomCorner)
    }

    fun getLeftTopCorner(): Int {
        return mLeftTopCorner
    }

    fun setLeftTopCorner(leftTopCorner: Int) {
        mLeftTopCorner = leftTopCorner
        setCornerSize(mLeftTopCorner, mLeftBottomCorner, mRightTopCorner, mRightBottomCorner)
    }

    fun getRightBottomCorner(): Int {
        return mRightBottomCorner
    }

    fun setRightBottomCorner(rightBottomCorner: Int) {
        mRightBottomCorner = rightBottomCorner
        setCornerSize(mLeftTopCorner, mLeftBottomCorner, mRightTopCorner, mRightBottomCorner)
    }

    fun getRightTopCorner(): Int {
        return mRightTopCorner
    }

    fun setRightTopCorner(rightTopCorner: Int) {
        mRightTopCorner = rightTopCorner
        setCornerSize(mLeftTopCorner, mLeftBottomCorner, mRightTopCorner, mRightBottomCorner)
    }
}