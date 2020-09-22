package com.journey.interview.customizeview.popupwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

/**
 * @By Journey 2020/9/22
 * @Description
 */
class PopupController(private val context: Context, private val popupWindow: PopupWindow) {

    private var layoutResId: Int? = null //布局id

    private var mView: View? = null

    var mPopupView: View? = null

    var mContext: Context = context


    fun setView(layoutResId: Int) {
        mView = null
        this.layoutResId = layoutResId
        initContentView()
    }

    fun setView(view: View) {
        this.mView = view
        this.layoutResId = 0
        initContentView()
    }

    private fun initContentView() {
        if (layoutResId != 0 && layoutResId != null) {
            mPopupView = LayoutInflater.from(context).inflate(layoutResId!!, null)
        } else if (mView != null) {
            mPopupView = mView
        }
        popupWindow.contentView = mPopupView
    }

    /**
     * 设置宽高
     */
    fun setWidthAndHeight(width: Int, height: Int) {
        if (width == 0 || height == 0) {
            // 如果没设置宽高，默认是WARP_CONTENT
            popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            popupWindow.width = width
            popupWindow.height = height
        }
    }

    /**
     * 设置动画
     */
    fun setAnimationStyle(animationStyle: Int) {
        popupWindow.animationStyle = animationStyle
    }

    /**
     * 设置outSide是否可以点击
     */
    fun setOutsideTouchable(touchable: Boolean) {
        popupWindow.run {
            // 设置背景透明
            setBackgroundDrawable(ColorDrawable(0x00000000))
            isOutsideTouchable = touchable
            isFocusable = touchable
        }
    }

    class PopupParams(context: Context) {
        var mLayoutResId: Int? = null
        var ctx:Context=context
        var mWidth: Int = 0
        var mHeight: Int = 0
        var isShowAnimation: Boolean = false
        var animationStyle: Int? = null
        var mPopView: View? = null
        var isTouchable: Boolean = true

        fun apply(controller: PopupController) {
            controller.run {
                if (mPopView != null) {
                    setView(mPopView!!)
                } else if (mLayoutResId != 0 && mLayoutResId != null) {
                    setView(mLayoutResId!!)
                } else {
                    throw IllegalArgumentException("PopupView's contentView is null")
                }
                setWidthAndHeight(mWidth, mHeight)
                setOutsideTouchable(isTouchable)
                if (isShowAnimation && animationStyle != 0 && animationStyle != null) {
                    setAnimationStyle(animationStyle!!)
                }
            }
        }
    }
}