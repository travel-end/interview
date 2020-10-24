package com.journey.interview.customizeview.indicator.layout

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import com.journey.interview.R
import com.journey.interview.utils.toIntPx

class IndicatorLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private fun init() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }

    fun create(count: Int) {
        for (i in 0 until count) {
            val imageView = ImageView(context)
            imageView.layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            val padding: Int = 3f.toIntPx()
            imageView.setPadding(padding, 0, padding, 0)
            imageView.setImageResource(if (i == 0) R.drawable.ic_play_page_indicator_selected else R.drawable.ic_play_page_indicator_unselected)
            addView(imageView)
        }
    }

    fun setCurrent(position: Int) {
        val count = childCount
        for (i in 0 until count) {
            val imageView = getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageResource(R.drawable.ic_play_page_indicator_selected)
            } else {
                imageView.setImageResource(R.drawable.ic_play_page_indicator_unselected)
            }
        }
    }

    init {
        init()
    }
}