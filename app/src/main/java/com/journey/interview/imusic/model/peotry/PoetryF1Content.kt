package com.journey.interview.imusic.model.peotry

data class PoetryF1Content(
    val cover:Int?=null,
    val coverDesc:String?=null,
    val contentDesc:String?=null
)
data class PoetryF1ContentList(
    val contentList:MutableList<PoetryF1Content>
)