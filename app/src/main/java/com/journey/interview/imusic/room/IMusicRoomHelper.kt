package com.journey.interview.imusic.room

import android.util.Log
import com.journey.interview.InterviewApp
import com.journey.interview.imusic.model.LoveSong
import com.journey.interview.imusic.model.Song

/**
 * @By Journey 2020/9/30
 * @Description
 */
object IMusicRoomHelper {
    private val loveSongDatabase by lazy {
        LoveSongDatabase.getInstance(InterviewApp.instance)
    }

    private val loveSongDao by lazy {
        loveSongDatabase.loveSongDao()
    }

    suspend fun addToMyLoveSong(loveSong: LoveSong):Long? {
        loveSong.songId?.let {
            loveSongDao.queryIsMyLove(it)?.let {data->
                if (data.size>0) {
                    for (i in data) {
                        val result = loveSongDao.deleteMyLove(i)
                        Log.d("JG", "删除我的喜欢：$i")
                    }
                }
            }
        }
        return loveSongDao.insertToMyLove(loveSong)
    }
    suspend fun isMyLoveSong(songId:String?):Boolean {
        songId?.let {
            loveSongDao.queryIsMyLove(it)?.let {data->
                return data.size != 0
            }
        }
        return false
    }

    suspend fun deleteFromMyLoveSong(loveSong: LoveSong):Int? {
        return loveSongDao.deleteMyLove(loveSong)
    }

    suspend fun getAllMyLoveSong():MutableList<LoveSong>? {
        return loveSongDao.queryAllMyLove()
    }

}