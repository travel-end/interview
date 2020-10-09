package com.journey.interview.imusic.download

import com.journey.interview.Constant
import com.journey.interview.imusic.model.Downloaded
import java.io.File

object IMusicDownloadUtil {
    fun getSongFromFile(fileName:String):MutableList<Downloaded>? {
        //将.m4a截取掉得到singer-songName-duration-songId-size
        val res = mutableListOf<Downloaded>()
        val file = File(fileName)
        if (!file.exists()) {
            file.mkdirs()
            return res
        }
        val subFiles = file.listFiles()
        if (subFiles != null) {
            for (value in subFiles) {
                if (value != null) {
                    val songFileName = value.name
                    val songFile = songFileName.substring(0,songFileName.lastIndexOf("."))
                    val songValue = songFile.split("-")
                    val size = songValue[4].toLong()
                    //如果文件的大小不等于实际大小，则表示该歌曲还未下载完成，被人为暂停，故跳过该歌曲，不加入到已下载集合
                    if (size != value.length()) continue
                    val downloadSong = Downloaded().apply {
                        singer = songValue[0]
                        name = songValue[1]
                        duration = songValue[2].toLong()
                        songId = songValue[3]
                        url = "$fileName$songFileName"
                    }
                    res.add(downloadSong)
                }
            }
            return res
        }
        return null
    }

    fun isExistOfDownloadSong(songId:String):Boolean {
        val file = File(Constant.STORAGE_SONG_FILE)
        if (!file.exists()) {
            file.mkdirs()
            return false
        }
        val subFile = file.listFiles()
        if (subFile != null) {
            for (value in subFile) {
                if (value != null) {
                    val songFileName = value.name
                    val songFile = songFileName.substring(0,songFileName.lastIndexOf("."))
                    val songValue = songFile.split("-")
                    // 如果文件的大小不等于实际大小，则表示该歌曲还未下载完成，被人为暂停，故跳过该歌曲，不加入到已下载集合
                    if (songValue[3]==songId) {
                        val size = songValue[4].toLong()
                        return size == value.length()
                    }
                }
            }
            return false
        }
        return false
    }

    /**
     * 组装下载歌曲的文件名
     */
    fun getSaveSongFile(singer:String,songName:String,duration:Long,songId:String,size:Long):String {
        return "$singer-$songName-$duration-$songId-$size.m4a"
    }
}