package com.journey.interview.imusic.model.peotry

data class PoetryF5Content(
    val cover:Int?=null,
    val coverDesc:String?=null,
    val contentDesc:String?=null
)
data class PoetryF5ContentList(
    val contentList:MutableList<PoetryF5Content>
)