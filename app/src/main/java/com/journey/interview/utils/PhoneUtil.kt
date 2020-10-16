package com.journey.interview.utils

import android.provider.MediaStore
import com.journey.interview.InterviewApp
import com.journey.interview.imusic.model.LocalSong

object PhoneUtil {
    fun getLocalSong() :MutableList<LocalSong>?{
        val context = InterviewApp.instance.applicationContext
        if (context != null) {
            try {
                val songList = mutableListOf<LocalSong>()
                val cursor = context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                    null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER
                )
                if (cursor != null) {
                    for (i in 0 until cursor.count) {
                        cursor.moveToNext()
                        val map3Info = LocalSong()
                        // 标题
                        var title =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        // 歌手
                        var artist =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        // 时长
                        val duration =
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                        // 文件大小
                        val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                        // 文件路径
                        val url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                        // 是否为音乐
                        val isMusic =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC))
                        // 只把音乐添加到集合当中
                        if (isMusic != 0) {
                            if (size > 1000 * 800) {
                                // 分离出歌曲名和歌手
                                if (title.contains("-")) {
                                    val array = title.split("-")
                                    artist = array[0]
                                    title = array[1]
                                }
                                map3Info.apply {
                                    name = title.trim()
                                    singer = artist
                                    this.duration = duration
                                    this.url = url
                                    songId = "$i"
                                }
                                songList.add(map3Info)
                            }
                        }

                    }
                }
                cursor?.close()
                return songList
            } catch (e:Exception) {
                return null
            }
        }
        return null
    }
}