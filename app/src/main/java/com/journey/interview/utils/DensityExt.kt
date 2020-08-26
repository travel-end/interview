package com.journey.interview.utils

import android.content.Context
import com.journey.interview.InterviewApp

/**
 * @By Journey 2020/7/9
 * @Description
 */

private fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun Float.toIntPx() = dpToPx(InterviewApp.instance, this).toInt()

fun Float.toFloatPx() = dpToPx(InterviewApp.instance,this)