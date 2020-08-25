package com.journey.interview.customizeview.rounddragtag.core

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.journey.interview.R
import com.journey.interview.utils.toIntPx
import kotlinx.android.synthetic.main.layout_random_tag.view.*

/**
 * @By Journey 2020/8/25
 * @Description 仿小红书拖拽标签视图
 */
class RandomDragTagView:LinearLayout {
    // 呼吸灯动画
    private  var mBreathingAnimator:ValueAnimator?=null
    // 回弹动画
    private var mReboundAnimator: ValueAnimator?=null

    // 是否显示左侧视图  默认显示左侧视图
    private var mIsShowLeftView:Boolean = true
    // 是否可以拖拽
    private var mCanDrag:Boolean = true
    // 是否可以拖拽出父控件区域
    private var mDragOutParent:Boolean = true

    // 文本圆角矩形的最大宽度
    private var mMaxTextLayoutWidth:Int = 0
    // 删除标签区域的高度
    private var mDeleteRegionHeight:Int = 0
    // 最大挤压宽度
    private var mMaxExtrusionWidth:Int = 400
    // 父控件最大的高度
    private var mMaxParentHeight:Int = 0

    // 是否多根手指按下
    private var mPointerDown:Boolean  = false
    private var mTouchSlop:Int = -1

    // 暴露接口
    private var mStartDrag:Boolean = false

    private var mStartReboundX:Float = 0f
    private var mStartReboundY:Float = 0f
    private var mLastMotionRawY:Float = 0f
    private var mLastMotionRawX:Float = 0f

    private var mDragListener:OnRandomDragListener?=null


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
        orientation = HORIZONTAL
        inflate(context,R.layout.layout_random_tag,this)
        initView()
        initListener()
        initData()
        startBreathingAnimator()
    }

    private fun initView() {

    }

    private fun initListener() {
        if (canDragView()) {
           fl_breathing.setOnClickListener {
               switchDirection()
           }
        }
    }

    // 切换方向
    fun switchDirection() {
        mIsShowLeftView = !mIsShowLeftView
        visibilityLeftLayout()
        visibilityRightLayout()

        // 第一步更改 重置 textLayout 的高度
        val preSwitchWidth = width
        val lp =
            (if (isShowLeftView()) left_tag_layout.layoutParams else right_tag_layout.layoutParams) as LayoutParams
        lp.width = LayoutParams.WRAP_CONTENT
        if (mIsShowLeftView) {
            left_tv_tag.text = right_tv_tag.text
            left_tag_layout.layoutParams = lp
        } else {
            right_tv_tag.text = left_tv_tag.text
            right_tag_layout.layoutParams = lp
        }

        // 第二步 重新设置setTranslationX的值
        post {
            var newTranslationX = 0f
            newTranslationX = if (!isShowLeftView()) {
                translationX + preSwitchWidth - white_breathing_view.width
            } else {
                translationX - width + white_breathing_view.width
            }
            // 边界检测
            checkBound(newTranslationX, translationY)
        }
    }
    // 边界检测
    private  fun checkBound(
        newTranslationX: Float,
        newTranslationY: Float
    ) {
        var mTranslationY = newTranslationY
        translationX = newTranslationX
        // 越界的情况下 改变textLayout 的高度
        val parentWidth = (parent as View).width
        val parentHeight = (parent as View).height
        val translationX = translationX
        if (translationX <= 0) {
            extrusionTextRegion(translationX)
        } else if (getTranslationX() >= parentWidth - width) {
            val offsetX = width - (parentWidth - getTranslationX())
            extrusionTextRegion(-offsetX)

            // 越界检测
            post {
                if (getTranslationX() >= parentWidth - width) {
                    setTranslationX(parentWidth - width.toFloat())
                }
            }
        }

        // 越界检测
        if (getTranslationX() <= 0) {
            setTranslationX(0f)
        }
        if (newTranslationY <= 0) {
            mTranslationY = 0f
        } else if (newTranslationY >= parentHeight - height) {
            mTranslationY = parentHeight - height.toFloat()
        }
        translationY = mTranslationY
    }

    /**
     * 挤压拉伸文本区域
     *
     * @param deltaX 偏移量
     */
    private fun extrusionTextRegion(deltaX: Float) {
        val textWidth: Int =
            if (isShowLeftView()) left_tag_layout.width else right_tag_layout.width
        val lp =
            (if (isShowLeftView()) left_tag_layout.layoutParams else right_tag_layout.layoutParams) as LayoutParams
        if (textWidth >= mMaxExtrusionWidth) {
            lp.width = (textWidth + deltaX).toInt()

            // 越界判定
            if (lp.width <= mMaxExtrusionWidth) {
                lp.width = mMaxExtrusionWidth
            } else if (lp.width >= mMaxTextLayoutWidth) {
                lp.width = mMaxTextLayoutWidth
            }
            if (isShowLeftView()) {
                left_tag_layout.layoutParams = lp
            } else {
                right_tag_layout.layoutParams = lp
            }
        }
    }


    private fun initData() {
        // 默认显示左侧视图，隐藏右侧视图
        visibilityLeftLayout()
        getMaxTextLayoutWidth()
        // 删除区域的高度
        mDeleteRegionHeight = 60f.toIntPx()
    }

    private fun visibilityLeftLayout() {
        left_tag_layout?.isVisible = mIsShowLeftView
        left_tv_tag?.isVisible = mIsShowLeftView
        left_line_view?.isVisible = mIsShowLeftView
    }

    private fun visibilityRightLayout() {
        right_tag_layout?.isVisible = !mIsShowLeftView
        right_tv_tag?.isVisible = !mIsShowLeftView
        right_line_view?.isVisible = !mIsShowLeftView
    }

    // 获取文本最大宽度
    private fun getMaxTextLayoutWidth(){
        post {
            mMaxTextLayoutWidth = if (isShowLeftView()) left_tag_layout.width else right_tag_layout.width
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        }
        if (!canDragView()) return super.onTouchEvent(event)
        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val x = event!!.rawX
                val y = event!!.rawY
                parent.requestDisallowInterceptTouchEvent(true)
                mStartDrag = false
                mPointerDown = false
                mLastMotionRawX = x
                mLastMotionRawY = y
                mStartReboundX = translationX
                mStartReboundY = translationY

                // 调整索引 位于其他标签之上
                adjustIndex()
            }
            MotionEvent.ACTION_POINTER_DOWN -> mPointerDown = true
            MotionEvent.ACTION_MOVE -> {
                val rawY = event!!.rawY
                val rawX = event!!.rawX
                if (!mStartDrag) {
                    mStartDrag = true
                    if (mDragListener != null) {
                        mDragListener?.onStartDrag()
                    }
                }
                if (!mPointerDown) {
                    val yDiff: Float = rawY - mLastMotionRawY
                    val xDiff: Float = rawX - mLastMotionRawX
                    // 处理move事件
                    handlerMoveEvent(yDiff, xDiff)
                    mLastMotionRawY = rawY
                    mLastMotionRawX = rawX
                }
            }
            MotionEvent.ACTION_POINTER_UP -> mPointerDown = false
            MotionEvent.ACTION_UP -> {
                mPointerDown = false
                mStartDrag = false
                parent.requestDisallowInterceptTouchEvent(false)
                val translationY = translationY
                val parentHeight = (parent as View).height
                if (mMaxParentHeight - mDeleteRegionHeight < translationY) {
                    removeTagView()
                } else if (parentHeight - height < translationY) {
                    startReBoundAnimator()
                }
                if (mDragListener != null) {
                    mDragListener?.onStopDrag()
                }
            }
        }
        return true
    }

    private fun startReBoundAnimator() {
        mReboundAnimator?.let {
            if (it.isRunning) {
                it.cancel()
            }
        }
        val startTransX = translationX
        val startTransY = translationY
        if (mReboundAnimator == null) {
            mReboundAnimator = ValueAnimator.ofFloat(1F,0F).apply {
                duration = 400
                addUpdateListener {anim->
                    val value = anim.animatedValue as Float
                    translationX = (mStartReboundX + (startTransX - mStartReboundX))
                    translationY = (mStartReboundY + (startTransY - mStartReboundY))
                }
            }
        }
        mReboundAnimator?.start()
    }

    /**
     * 处理手势的move事件
     *
     * @param yDiff y轴方向的偏移量
     * @param xDiff x轴方向的偏移量
     */
    private fun handlerMoveEvent(yDiff: Float, xDiff: Float) {
        var translationX = translationX + xDiff
        var translationY = translationY + yDiff

        // 越界处理 最大最小原则
        val parentWidth = (parent as View).width
        val parentHeight = (parent as View).height
        if (mMaxParentHeight == 0) {
            val parentParentHeight = (parent.parent as View).height
            mMaxParentHeight =
                (if (mDragOutParent) parentParentHeight else parentHeight) - height
        }
        val maxWidth = parentWidth - width

        // 分情况处理越界 宽度
        if (translationX <= 0) {
            translationX = 0f
            // 标签文本出现挤压效果
            if (isShowLeftView()) {
                extrusionTextRegion(xDiff)
            }
        } else if (translationX >= maxWidth) {
            translationX = maxWidth.toFloat()
            // 右侧挤压
            if (!isShowLeftView()) {
                extrusionTextRegion(-xDiff)
                handleWidthError()
            }
        } else {
            val textWidth: Int =
                if (isShowLeftView()) left_tag_layout.width else right_tag_layout.width
            // 左侧视图
            if (isShowLeftView()) {
                if (getTranslationX() == 0f && textWidth < mMaxTextLayoutWidth) {
                    translationX = 0f
                    extrusionTextRegion(xDiff)
                }
            } else {
                if (textWidth < mMaxTextLayoutWidth) {
                    extrusionTextRegion(-xDiff)
                    handleWidthError()
                }
            }
        }

        // 高度越界处理
        if (translationY <= 0) {
            translationY = 0f
        } else if (translationY >= mMaxParentHeight) {
            translationY = mMaxParentHeight.toFloat()
        }
        setTranslationX(translationX)
        setTranslationY(translationY)
    }

    // 处理宽度误差
    private fun handleWidthError() {
        post {
            if (parent != null) {
                val parentWidth = (parent as View).width
                val maxWidth = parentWidth - width
                translationX = maxWidth.toFloat()
            }
        }
    }

    private fun removeTagView() {
        if (parent != null) {
            if (parent is ViewGroup) {
                (parent as ViewGroup).removeView(this)
            }
        }
    }

    /**
     * 调整索引 位于其他标签之上
     * moveToTop(View target) 方法的性能更好
     */
    @Deprecated("")
    private fun adjustIndex() {
        val parent = parent
        if (parent != null) {
            if (parent is ViewGroup) {
                val childCount = parent.childCount
                if (childCount > 1 && indexOfChild(this) != childCount - 1) {
                    parent.removeView(this)
                    parent.addView(this)
                    // 重新开启呼吸灯动画
                    startBreathingAnimator()
                }
            }
        }
    }

    /**
     * 添加标签
     *
     * @param text           标签文本
     * @param translationX   相对于父控件的x坐标
     * @param translationY   相对于父控件的y坐标
     * @param isShowLeftView 是否显示左侧标签
     */
    fun initTagView(
        text: String?,
        translationX: Float,
        translationY: Float,
        isShowLeftView: Boolean
    ) {
        mIsShowLeftView = isShowLeftView
        visibilityLeftLayout()
        visibilityRightLayout()
        // 不可见
        visibility = View.INVISIBLE
        // 设置文本控件
        if (mIsShowLeftView) {
            left_tv_tag.text = text
        } else {
            right_tv_tag.text = text
        }
        // 获取文本最大宽度
        getMaxTextLayoutWidth()
        post { // 边界检测
            checkBound(translationX, translationY)
            // 设置可见
            visibility = View.VISIBLE
        }
    }


    private fun startBreathingAnimator() {
        clearBreathingAnimator()
        if (mBreathingAnimator == null) {
            mBreathingAnimator = ValueAnimator.ofFloat(0.8F, 1.0F).apply {
                repeatMode = ValueAnimator.REVERSE
                duration = 800
                startDelay = 200
                repeatCount = -1
                addUpdateListener {anim->
                    val value = anim.animatedValue as Float
                    white_breathing_view.scaleX = value
                    white_breathing_view.scaleY = value
                }
            }
        }
        mBreathingAnimator?.start()
    }

    // 注意清理  防止内存泄露
    private fun clearBreathingAnimator() {
        mBreathingAnimator?.let {
            if (it.isRunning) {
                it.cancel()
                mBreathingAnimator = null
            }
        }
    }

    fun isShowLeftView():Boolean {
        return mIsShowLeftView
    }

    fun canDragView():Boolean {
        return mCanDrag
    }

    fun getTagText(): String? {
        return if (isShowLeftView()) {
            left_tv_tag.text.toString()
        } else right_tv_tag.text.toString()
    }

    fun getPercentTransX(): Float {
        val parentWidth = (parent as View).width
        return translationX / parentWidth
    }

    fun getPercentTransY(): Float {
        val parentHeight = (parent as View).height
        return translationY / parentHeight
    }

    fun setShowLeftView(showLeftView: Boolean) {
        mIsShowLeftView = showLeftView
    }

    fun setMaxExtrusionWidth(maxExtrusionWidth: Int) {
        mMaxExtrusionWidth = maxExtrusionWidth
    }

    fun getMaxExtrusionWidth(): Int {
        return mMaxExtrusionWidth
    }

    override fun onDetachedFromWindow() {
        clearBreathingAnimator()
        super.onDetachedFromWindow()
    }


    interface OnRandomDragListener {
        // 开始拖拽
        fun onStartDrag()
        // 停止拖拽
        fun onStopDrag()
    }
}