package com.journey.interview.imusic.model

/**
 * @By Journey 2020/9/25
 * @Description
 */

data class FindMenuList(
    val list:MutableList<FindMenu>
)

data class FindMenu(
    val iconRes:Int,
    val menuName:String
)