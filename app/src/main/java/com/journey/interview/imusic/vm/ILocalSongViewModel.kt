package com.journey.interview.imusic.vm

import android.provider.MediaStore
import android.provider.MediaStore.Audio.*
import com.journey.interview.InterviewApp
import com.journey.interview.imusic.model.LocalSong
import com.journey.interview.weatherapp.base.BaseViewModel

class ILocalSongViewModel:BaseViewModel() {

    fun getLocalMp3Info():MutableList<LocalSong>? {

        val context = InterviewApp.instance.applicationContext
        if (context != null) {
            val songList = mutableListOf<LocalSong>()
            val cursor = context.contentResolver.query(
                Media.EXTERNAL_CONTENT_URI,null,
                null,null,
                Media.DEFAULT_SORT_ORDER
            )
            if (cursor != null) {
                for (i in 0 until cursor.count) {
                    cursor.moveToNext()
                    val map3Info = LocalSong()
                    // 标题
                    val title = cursor.getString(cursor.getColumnIndex(Media.TITLE))
                    // 歌手
                    val artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST))
                    // 时长
                    val duration = cursor.getLong(cursor.getColumnIndex(Media.DURATION))
                    // 文件大小
                    val size = cursor.getLong(cursor.getColumnIndex(Media.SIZE))
                    // 文件路径
                    val url = cursor.getString(cursor.getColumnIndex(Media.DATA))
                    // 是否为音乐
                    val isMusic = cursor.getInt(cursor.getColumnIndex(Media.IS_MUSIC))

                }
            }

            cursor?.close()
            return songList
        }
        return null

    }
}