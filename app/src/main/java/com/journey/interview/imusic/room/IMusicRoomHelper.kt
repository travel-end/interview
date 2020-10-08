package com.journey.interview.imusic.room

import android.util.Log
import com.journey.interview.InterviewApp
import com.journey.interview.imusic.model.*

/**
 * @By Journey 2020/9/30
 * @Description
 */
object IMusicRoomHelper {
    /**
     * 我喜爱的歌曲
     */
    private val loveSongDatabase by lazy {
        LoveSongDatabase.getInstance(InterviewApp.instance)
    }

    /**
     * 本地歌曲
     */
    private val localSongDatabase by lazy {
        LocalSongDatabase.getInstance(InterviewApp.instance)
    }

    /**
     * 历史播放歌曲
     */
    private val historySongDatabase by lazy {
        HistorySongDatabase.getInstance(InterviewApp.instance)
    }

    /**
     * 下載歌曲
     */
    private val downloadSongDatabase by lazy {
        DownloadSongDatabase.getInstance(InterviewApp.instance)
    }

    private val downloadSongDao by lazy {
        downloadSongDatabase.downloadSongDao()
    }

    private val loveSongDao by lazy {
        loveSongDatabase.loveSongDao()
    }

    private val localSongDao by lazy {
        localSongDatabase.localSongDao()
    }

    private val historySongDao by lazy {
        historySongDatabase.historySongDao()
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

    suspend fun saveLocalSong(localSongs:MutableList<LocalSong>) :Long?{
        var result :Long?=null
        for (song in localSongs) {
            localSongDao.deleteLocalSongs(song)
            val localSong = LocalSong().apply {
                name = song.name
                singer = song.singer
                url = song.url
                songId = song.songId
                duration = song.duration
            }
            result = localSongDao.insertLocalSong(localSong)
        }
        return result
    }

    suspend fun getAllLocalSongs():MutableList<LocalSong>? {
        return localSongDao.queryAllLocalSongs()
    }

    suspend fun findHistorySongBySongId(songId: String):MutableList<HistorySong>? {
        return historySongDao.queryHistorySongBySongId(songId)
    }

    suspend fun deleteHistorySong(historySong: HistorySong):Int? {
        return historySongDao.deleteHistorySong(historySong)
    }
    suspend fun saveToHistorySong(historySong: HistorySong):Long? {
        val song = historySongDao.queryHistorySongBySongId(historySong.songId?:"")
        return if (song != null && song.size>0) {
            null
        } else {
            historySongDao.insertHistorySong(historySong)
        }
    }
    suspend fun queryAllHistorySongs():MutableList<HistorySong>? {
        return historySongDao.queryAllHistorySongs()
    }

    suspend fun findDownloadSongBySongId(songId: String):MutableList<DownloadSong>? {
        return downloadSongDao.queryDownloadSongBySongId(songId)
    }

    suspend fun saveToDownloadSong(downloadSong: DownloadSong):Long? {
        val song = historySongDao.queryHistorySongBySongId(downloadSong.songId?:"")
        return if (song != null && song.size>0) {
            null
        } else {
            downloadSongDao.insertDownloadSong(downloadSong)
        }
    }
    suspend fun queryAllDownloadSongs():MutableList<DownloadSong>? {
        return downloadSongDao.queryAllDownloadSongs()
    }
}