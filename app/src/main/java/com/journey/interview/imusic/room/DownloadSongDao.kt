package com.journey.interview.imusic.room

import androidx.room.*
import com.journey.interview.imusic.model.DownloadSong

@Dao
interface DownloadSongDao {

    /**
     * 插入下載歌曲
     */
    @Transaction
    @Insert(entity = DownloadSong::class)
    suspend fun insertDownloadSong(downloadSong: DownloadSong):Long?

    /**
     * 删除下載歌曲
     */
    @Transaction
    @Delete(entity = DownloadSong::class)
    suspend fun deleteDownloadSong(downloadSong: DownloadSong):Int?

    /**
     * 查找全部下載歌曲
     */
    @Transaction
    @Query("SELECT * FROM downloadsong ORDER BY id DESC")
    suspend fun queryAllDownloadSongs():MutableList<DownloadSong>?

    /**
     * 根据跟去songId查找指定歌曲
     */
    @Transaction
    @Query("SELECT * FROM downloadsong WHERE songId = (:songId) ORDER BY id DESC")
    suspend fun queryDownloadSongBySongId(songId:String):MutableList<DownloadSong>?

}