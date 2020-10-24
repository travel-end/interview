package com.journey.interview.imusic.util

import android.util.Log
import com.google.gson.Gson
import com.journey.interview.Constant
import com.journey.interview.InterviewApp
import com.journey.interview.imusic.download.IMusicDownloadUtil
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.model.recommend.RecomSong
import com.journey.interview.imusic.model.recommend.Recommend
import com.journey.interview.utils.StringUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * @By Journey 2020/10/16
 * @Description
 */
object AssetsUtil {
    private val gson by lazy {
        Gson()
    }

    fun readAssetsFile(fileName:String):String? {
        val content:String?
        return try {
            val input = InterviewApp.instance.applicationContext.assets.open(fileName)
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()
            content = String(buffer)
            Log.e("JG","读取asset文件内容：$content")
            content
        } catch (e:Exception) {
            e.printStackTrace()
            Log.e("JG","读取assets文件出错!!!--> error:${e.message}")
            null
        }
    }

    fun readAssetsJson(jsonName:String):String? {
        val builder = StringBuilder()
        return try {
            val inputReader =
                InputStreamReader(InterviewApp.instance.applicationContext.resources.assets.open(jsonName))
            val bufReader = BufferedReader(inputReader)
            // 只读一行
            var line: String?
            while (bufReader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            builder.toString()
        } catch (e:Exception) {
            e.printStackTrace()
            null
        }
    }

    fun loadRecommendSongs():MutableList<RecomSong>? {
        val builder=StringBuilder()
        val list = mutableListOf<RecomSong>()
        try {
            val inputReader =
                InputStreamReader(InterviewApp.instance.applicationContext.resources.assets.open("recommend_song.json"))
            val bufReader = BufferedReader(inputReader)
            // 只读一行
            var line: String?
            while (bufReader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            val result = gson.fromJson(builder.toString(), Recommend::class.java)
            Log.e("JG","查询json文件的推荐歌曲数目：${result.recommendList.size}")
            result?.let {
                val recommendList = it.recommendList
                for (r in recommendList) {
                    val song = RecomSong().apply {
                        songId = r.songmid
                        singer = StringUtils.getSinger(r)
                        songName = r.songname
                        imgUrl = "${Constant.ALBUM_PIC}${r.albummid}${Constant.JPG}"
                        duration = r.interval
                        isOnline = true
                        mediaId = r.strMediaMid
                        songmid = r.songmid
                        albumName = r.albumname
                        isDownload = IMusicDownloadUtil.isExistOfDownloadSong(r.songmid?:"")//003IHI2x3RbXLS
                    }
                    list.add(song)
                }
                return list
            }
            return null
        } catch (e:IOException) {
            e.printStackTrace()
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return null
    }
}