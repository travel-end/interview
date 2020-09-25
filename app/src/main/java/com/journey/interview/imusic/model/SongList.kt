package com.journey.interview.imusic.model

/**
 * @By Journey 2020/9/25
 * @Description
 */

data class SongListAll(
    val allList:MutableList<SongList>
)

data class SongList(
    val songCover:Int,
    val songDesc:String,
    val volume:String
)