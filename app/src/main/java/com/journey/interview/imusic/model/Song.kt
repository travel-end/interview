package com.journey.interview.imusic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
 class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var songId //歌曲id
    : String? = null,
    var qqId //专属本地音乐，本地音乐在qq音乐中的songId
    : String? = null,
    var mediaId //播放id,下载需要用到
    : String? = null,
    var singer //歌手
    : String? = null,
    var duration //总时长（秒）
    : Int = 0,
    var songName //歌曲名字
    : String? = null,
    var url //歌曲播放地址url
    : String? = null,
    var currentTime //歌曲播放时长位置
    : Long = 0,
    var position //在音乐列表的位置
    : Int = 0,
    var imgUrl //封面专辑照片
    : String? = null,
    var isOnline //是否为网络歌曲
    : Boolean = false,
    var listType //歌曲列表类别,0表示当前没有列表，即可能在播放网络歌曲
    : Int = 0,
    var isDownload //是否为下载的歌曲
    : Boolean = false,
    var albumName:String?=null,
    var playStatus:Int?=null

) :Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}