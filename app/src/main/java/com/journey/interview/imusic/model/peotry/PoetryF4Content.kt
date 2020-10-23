package com.journey.interview.imusic.model.peotry

data class PoetryF4Content(
    val cover:Int?=null,
    val coverDesc:String?=null,
    val contentDesc:String?=null
)
data class PoetryF4ContentList(
    val contentList:MutableList<PoetryF4Content>
)