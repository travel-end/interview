package com.journey.interview.customizeview.popupwindow

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

/**
 * @By Journey 2020/9/22
 * @Description
 */
class CommonPopupWindow(context: Context) : PopupWindow() {
    private var controller: PopupController = PopupController(context, this)
    override fun getWidth(): Int = controller.mPopupView?.measuredWidth ?: 0
    override fun getHeight(): Int = controller.mPopupView?.measuredHeight ?: 0
    override fun dismiss() {
        super.dismiss()
        setBackgroundAlpha(controller.mContext as Activity, 1f)
    }

    // 显示在控件上方
    fun showUpView(view: View, bgAlpha: Float = -1f) {
        this.showAsDropDown(
            view,
            -(this.width - view.measuredWidth) / 2,
            -(this.height + view.measuredHeight)
        )
        if (bgAlpha != -1f) {
            setBackgroundAlpha(controller.mContext as Activity, bgAlpha)
        }
    }

    // 显示在控件下方
    fun showDownView(view: View, bgAlpha: Float = -1f) {
        this.showAsDropDown(view, -(this.width - view.measuredWidth) / 2, 0)
        if (bgAlpha != -1f) {
            setBackgroundAlpha(controller.mContext as Activity, bgAlpha)
        }
    }

    fun showBottom(view: View,bgAlpha:Float) {
        this.showAtLocation(view,Gravity.BOTTOM,0,0)
        setBackgroundAlpha(controller.mContext as Activity,bgAlpha)
    }

    // 显示在控件左方
    fun showLeftView(view: View, bgAlpha: Float = -1f) {
        this.showAsDropDown(view, -view.measuredWidth, -(this.height + view.measuredHeight) / 2)
        if (bgAlpha != -1f) {
            setBackgroundAlpha(controller.mContext as Activity, bgAlpha)
        }
    }

    // 显示在控件右方
    fun showRightView(view: View, bgAlpha: Float = -1f) {
        this.showAsDropDown(view, view.measuredWidth, -(this.height + view.measuredHeight) / 2)
        if (bgAlpha != -1f) {
            setBackgroundAlpha(controller.mContext as Activity, bgAlpha)
        }
    }


    class Builder(context: Context) {
        private var params: PopupController.PopupParams = PopupController.PopupParams(context)

        private var listener: PopItemListener? = null

        fun setView(layoutResId: Int): Builder {
            params.mPopView = null
            params.mLayoutResId = layoutResId
            return this
        }

        fun setView(view: View): Builder {
            params.mPopView = view
            params.mLayoutResId = 0
            return this
        }

        fun setViewOnclickListener(listener: PopItemListener?): Builder {
            this.listener = listener
            return this
        }

        fun setWidthAndHeight(
            width: Int,
            height: Int
        ): Builder {
            params.mWidth = width
            params.mHeight = height
            return this
        }

        fun setOutsideTouchable(touchable: Boolean): Builder {
            params.isTouchable = touchable
            return this
        }

        fun setAnimationStyle(animationStyle: Int): Builder {
            params.isShowAnimation = true
            params.animationStyle = animationStyle
            return this
        }

        fun create(): CommonPopupWindow {
            val popupWindow = CommonPopupWindow(params.ctx)
            params.apply(popupWindow.controller)
            if (listener != null) {
                listener?.getChildView(popupWindow.controller.mPopupView!!)
            }
//            measureWidthAndHeight(popupWindow.controller.mPopupView!!)

            val view = popupWindow.controller.mPopupView!!
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            view.measure(widthMeasureSpec, heightMeasureSpec)

            return popupWindow
        }
    }

//    private fun measureWidthAndHeight(view: View) {
//        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
//            0,
//            View.MeasureSpec.UNSPECIFIED)
//        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0,
//            View.MeasureSpec.UNSPECIFIED)
//        view.measure(widthMeasureSpec,heightMeasureSpec)
//    }

    private fun setBackgroundAlpha(activity: Activity, bgAlpha: Float) {
        val window = activity.window
        val lp = window?.attributes
        lp?.alpha = bgAlpha
        if (bgAlpha == 1f) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        window?.attributes = lp
    }


    interface PopItemListener {
        fun getChildView(view: View)
    }
}



