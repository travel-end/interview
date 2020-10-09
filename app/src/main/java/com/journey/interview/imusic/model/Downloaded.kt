package com.journey.interview.imusic.model

data class Downloaded(
    var id:Long?=null,
    var songId:String?=null,
    var mediaId:String?=null,
    var singer:String?=null,
    var url:String?=null,
    var pic:String?=null,
    var duration:Long?=null,
    var name:String?=null,
    var albumName:String?=null
)