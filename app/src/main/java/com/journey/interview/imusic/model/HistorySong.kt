package com.journey.interview.imusic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistorySong(
    @PrimaryKey
    var id:Int?=null,
    var songId:String?=null,
    var mediaId:String?=null,// 下载标识符
    var qqId:String?=null,
    var name:String?=null,
    var singer:String?=null,
    var url:String?=null,
    var pic:String?=null,
    var duration:Int?=null,
    var isOnline:Boolean=false,
    var isDownload:Boolean=false
)