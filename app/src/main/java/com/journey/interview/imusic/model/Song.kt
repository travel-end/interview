package com.journey.interview.imusic.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by 残渊 on 2018/10/19.
 */
//@Parcelize
@Entity
data class Song(
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
    var duration //总时长
    : Int = 0,
    var songName //歌曲名字
    : String? = null,
    var url //歌曲url
    : String? = null,
    var currentTime //歌曲播放时长位置
    : Long = 0,
    var position //在音乐列表的位置
    : Int = 0,
    var imgUrl //歌曲照片
    : String? = null,
    var isOnline //是否为网络歌曲
    : Boolean = false,
    var listType //歌曲列表类别,0表示当前没有列表，即可能在播放网络歌曲
    : Int = 0,
    var isDownload //是否为下载的歌曲
    : Boolean = false

) : Serializable