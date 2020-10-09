package com.journey.interview.imusic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DownloadSong(
    @PrimaryKey
    var id:Long?=null,
    var songId:String?=null,
    var songName:String?=null,
    var singer:String?=null,
    var progress:Int?=null,
    var url:String?=null,
    var pic:String?=null,
    var duration:Int?=null,
    var currentSize:Long?=null,
    var totalSize:Long?=null,
    var position:Int?=null,// 正在下載歌曲在列表中的位置
    var status:Int?=null,
    var albumName:String?=null
)