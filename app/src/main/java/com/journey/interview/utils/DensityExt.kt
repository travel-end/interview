package com.journey.interview.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import com.journey.interview.InterviewApp

/**
 * @By Journey 2020/7/9
 * @Description
 */

private fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

private fun dp2Px(context: Context,dp: Float):Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp
    ,context.resources.displayMetrics)
}

fun Float.toIntPx(context:Context = InterviewApp.instance) = dp2Px(context,this).toInt()

fun Float.toFloatPx(context:Context = InterviewApp.instance) = dp2Px(context,this)

fun String.logV()  {
    Log.v("Kotlin",this)
}

fun String.logE() {
    Log.e("Kotlin",this)
}

fun Int.getString() = InterviewApp.instance.resources.getString(this)

// 将px值转换成dp值
