package com.journey.interview.imusic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalSong(
    @PrimaryKey
    var id:Int?=null,
    var songId:String?=null,
    var qqId:String?=null,
    var name:String?=null,
    var singer:String?=null,
    var url:String?=null,
    var pic:String?=null,
    var duration:Long?=null
)