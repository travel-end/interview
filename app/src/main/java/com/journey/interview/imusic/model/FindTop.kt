package com.journey.interview.imusic.model


/**
 * @By Journey 2020/9/25
 * @Description
 */
data class FindTop(
    val icon:Int=0,
    val note:String="",
    val banners:MutableList<FindTopItem>
)

data class FindTopItem(
    val cover:Int=0,
    val desc:String=""
)