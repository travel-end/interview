package com.journey.interview.utils

import android.os.Build
import android.os.Environment
import com.journey.interview.InterviewApp
import java.io.File

/**
 * @By Journey 2020/9/27
 * @Description
 */
object DownloadUtil {
    fun isExistOfDownloadSong(songId:String?):Boolean {
        val pathname = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            "${InterviewApp.instance.getExternalFilesDirs("music")}/imusic/download/"
        } else {
            "${Environment.getExternalStorageDirectory()}/imusic/download/"
        }
        val file = File(pathname)
        if (!file.exists()) {
            file.mkdirs()
            return false
        }
        val subFile = file.listFiles()
        subFile?.let {
            for (value in subFile) {
                if (value != null) {
                    val songFileName = value.name
                    val songFile = songFileName.substring(0,songFileName.lastIndexOf("."))
                    val songValue = songFile.split("-")
                    //如果文件的大小不等于实际大小，则表示该歌曲还未下载完成，被人为暂停，故跳过该歌曲，不加入到已下载集合
                    if (songValue[3] == songId) {
                        val size = songValue[4].toLong()
                        return size == value.length()
                    }
                }

            }
        }
        return false
    }
}