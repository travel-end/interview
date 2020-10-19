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

    @Transaction
    @Query("SELECT * FROM downloadsong WHERE id > (:id)")
    suspend fun queryDownloadSongById(id:Long):MutableList<DownloadSong>?

    @Transaction
    @Query("UPDATE downloadsong SET status=(:status) WHERE songId=(:songId)")
    suspend fun updateDownloadSongStatus(status:Int,songId: String):Int?

    @Transaction
    @Query("UPDATE downloadsong SET id=(:id) WHERE songId=(:songId)")
    suspend fun updateDownloadSongId(id:Long,songId: String):Int?

    @Transaction
    @Query("SELECT * FROM downloadsong WHERE id>(:id)")
    suspend fun findDownloadSongUpId(id: Long):MutableList<DownloadSong>?
}