package com.journey.interview.imusic.model.recommend

import com.journey.interview.imusic.model.ListBean

/**
 * @By Journey 2020/10/24
 * @Description
 */

data class Recommend(

    val id:Int?=null,
    val recommendList:List<ListBean>
)