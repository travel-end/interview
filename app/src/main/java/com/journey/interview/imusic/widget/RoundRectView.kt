package com.journey.interview.imusic.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.journey.interview.R

/**
 * 歌单封面的view,是一个圆角矩形
 */
class RoundRectView(
    context: Context,
    attrs: AttributeSet?
) : AppCompatImageView(context, attrs) {
    private var roundRatio = 16f
    private var path: Path? = null
    override fun onDraw(canvas: Canvas) {
        if (path == null) {
            path = Path()
            path!!.addRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                roundRatio * width,
                roundRatio * height,
                Path.Direction.CW
            )
        }
        canvas.save()
        canvas.clipPath(path!!)
        super.onDraw(canvas)
        canvas.restore()
    }

    init {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.RoundRectView, 0, 0)
        roundRatio = try {
            typedArray.getFloat(R.styleable.RoundRectView_roundRatio, 16f)
        } finally {
            typedArray.recycle()
        }
    }
}