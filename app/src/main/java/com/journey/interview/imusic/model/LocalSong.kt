package com.journey.interview.imusic.model

data class LocalSong(
    var id:Int?=null,
    var songId:String?=null,
    var qqId:String?=null,
    var name:String?=null,
    var singer:String?=null,
    var url:String?=null,
    var pic:String?=null,
    var duration:Long?=null
)