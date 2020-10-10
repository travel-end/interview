package com.journey.interview.imusic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Entity
data class LoveSong(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val songId:String?=null,
    val mediaId:String?=null,// 下载标识符
    val qqId:String?=null,
    var name:String?=null,
    val singer:String?=null,
    val url:String?=null,
    val pic:String?=null,
    val duration:Int?=null,
    val isOnline:Boolean?=null,
    val isDownload:Boolean?=null
)