package com.journey.interview.imusic.room

import android.util.Log
import com.journey.interview.InterviewApp
import com.journey.interview.imusic.model.*
import com.journey.interview.imusic.model.recommend.RecomSong

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

    suspend fun addToMyLoveSong(loveSong: LoveSong): Long? {
//        loveSong.songId?.let {
//            loveSongDao.queryIsMyLove(it)?.let { data ->
//                if (data.size > 0) {
//                    for (i in data) {
//                        val result = loveSongDao.deleteMyLove(i)
//                        Log.d("JG", "删除我的喜欢：$i")
//                    }
//                }
//            }
//        }
        return loveSongDao.insertToMyLove(loveSong)
    }

    suspend fun isMyLoveSong(songId: String?): Boolean {
        songId?.let {
            loveSongDao.queryIsMyLove(it)?.let { data ->
                return data.size != 0
            }
        }
        return false
    }

    suspend fun deleteFromMyLoveSong(loveSong: LoveSong): Int? {
        return loveSongDao.deleteMyLove(loveSong)
    }
    suspend fun deleteLoveSongBySongId(songId: String):Int?{
        return loveSongDao.deleteLoveSongBySongId(songId)
    }

    suspend fun getAllMyLoveSong(): MutableList<LoveSong>? {
        return loveSongDao.queryAllMyLove()
    }

    /* *****本地音乐********/
    suspend fun saveLocalSong(localSongs: MutableList<LocalSong>): Long? {
        var result: Long? = null//i in localSongs.indices.reversed()
        for (i in localSongs.indices.reversed()) {
//            val s = localSongDao.queryLocalSongsBySongId(song.songId?:"")
//            Log.e("JG","根据id查询：$s")
//            if (s.isEmpty()) {
                result = localSongDao.insertLocalSong(localSongs[i])
//            }
        }
        return result
    }
    suspend fun saveLocalSong(localSong:LocalSong):Long?{
        return localSongDao.insertLocalSong(localSong)
    }

    suspend fun deleteLocalSong(localSong: LocalSong) :Int?{
        return localSongDao.deleteLocalSongs(localSong)
    }

    suspend fun deleteAllLocalSong():Int?{
        return localSongDao.deleteAllLocalSong()
    }
    suspend fun queryLocalSongsBySongId(songId: String?):MutableList<LocalSong>? {
        return localSongDao.queryLocalSongsBySongId(songId?:"")
    }
    suspend fun updateLocalSongPicBySongId(pic:String,songId: String):Int?{
        return localSongDao.updateLocalSongPicBySongId(pic,songId)
    }

    suspend fun getAllLocalSongs(): MutableList<LocalSong>? {
        return localSongDao.queryAllLocalSongs()
    }

    suspend fun updateLocalSong(localSong:LocalSong):Int? {
        return localSongDao.updateLocalSong(localSong)
    }

    /* 历史播放记录音乐*/
    suspend fun findHistorySongBySongId(songId: String): MutableList<HistorySong>? {
        return historySongDao.queryHistorySongBySongId(songId)
    }

    suspend fun deleteHistorySong(historySong: HistorySong): Int? {
        return historySongDao.deleteHistorySong(historySong)
    }

    suspend fun saveToHistorySong(historySong: HistorySong): Long? {
        val song = historySongDao.queryHistorySongBySongId(historySong.songId ?: "")
        return if (song != null && song.size > 0) {
            null
        } else {
            historySongDao.insertHistorySong(historySong)
        }
    }

    suspend fun deleteAllHistorySong():Int? {
        return historySongDao.deleteAllHistory()
    }

    suspend fun queryAllHistorySongs(): MutableList<HistorySong>? {
        return historySongDao.queryAllHistorySongs()
    }

    /* ----------下载歌曲------------*/
    suspend fun findDownloadSongBySongId(songId: String): MutableList<DownloadSong>? {
        return downloadSongDao.queryDownloadSongBySongId(songId)
    }

    suspend fun saveToDownloadSong(downloadSong: DownloadSong): Long? {
//        val song = downloadSongDao.queryDownloadSongBySongId(downloadSong.songId ?: "")
//        return if (song != null && song.size > 0) {
//            null
//        } else {
//
//        }
        return downloadSongDao.insertDownloadSong(downloadSong)
    }

    suspend fun queryAllDownloadSongs(): MutableList<DownloadSong>? {
        return downloadSongDao.queryAllDownloadSongs()
    }

    suspend fun queryDownloadSongById(id: Long): MutableList<DownloadSong>? {
        return downloadSongDao.queryDownloadSongById(id)
    }

    suspend fun updateDownloadSongStatus(status:Int,songId: String):Int? {
        return downloadSongDao.updateDownloadSongStatus(status,songId)
    }

    suspend fun findDownloadSongUpId(id: Long): MutableList<DownloadSong>? {
        return downloadSongDao.findDownloadSongUpId(id)
    }

    suspend fun updateDownloadSongId(id: Long,songId: String):Int? {
        return downloadSongDao.updateDownloadSongId(id,songId)
    }
    suspend fun deleteDownloadSong(downloadSong: DownloadSong):Int? {
        return downloadSongDao.deleteDownloadSong(downloadSong)
    }


    private val recommendSongDatabase by lazy {
        RecommendSongDatabase.getInstance(InterviewApp.instance)
    }

    private val recommendSongDao by lazy {
        recommendSongDatabase.recommendSongDao()
    }

    suspend fun addRecomSongs(recomSong: List<RecomSong>){
        recommendSongDao.addRecommendSongs(recomSong)
    }
    suspend fun findAllRecomSongs() :MutableList<RecomSong>? {
        return recommendSongDao.queryAllRecommendSong()
    }
}