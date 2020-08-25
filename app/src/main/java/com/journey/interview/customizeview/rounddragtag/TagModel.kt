package com.journey.interview.customizeview.rounddragtag

/**
 * @By Journey 2020/8/25
 * @Description
 */
data class TagModel(
    // 相对于父控件的x坐标百分比
    val x :Float,
    val y:Float,
    // 标签内容
    val text:String,

    // left = true  right=false
    val direction:Boolean
)