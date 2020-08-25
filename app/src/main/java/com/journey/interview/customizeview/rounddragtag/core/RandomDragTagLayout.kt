package com.journey.interview.customizeview.rounddragtag.core

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * @By Journey 2020/8/25
 * @Description
 */
class RandomDragTagLayout:FrameLayout {
    constructor(context: Context) : super(context,null) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs,0) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    /**
     * 添加标签
     *
     * @param text           标签文本
     * @param x              相对于父控件的x坐标百分比
     * @param y              相对于父控件的y坐标百分比
     * @param isShowLeftView 是否显示左侧标签
     */
    fun addTagView(
        text: String?,
        x: Float,
        y: Float,
        isShowLeftView: Boolean
    ): Boolean {
        if (text == null || text == "") return false
        val tagView = RandomDragTagView(context)
        addView(
            tagView,
            LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        tagView.initTagView(text, x * width, y * height, isShowLeftView)
        return true
    }
}