package com.journey.interview.imusic.room

import androidx.room.*
import com.journey.interview.imusic.model.HistorySong

@Dao
interface HistorySongDao {

    /**
     * 插入历史歌曲
     */
    @Transaction
    @Insert(entity = HistorySong::class)
    suspend fun insertHistorySong(historySong: HistorySong):Long?

    /**
     * 删除历史歌曲
     */
    @Transaction
    @Delete(entity = HistorySong::class)
    suspend fun deleteHistorySong(historySong: HistorySong):Int?

    /**
     * 查找全部历史歌曲
     */
    @Transaction
    @Query("SELECT * FROM historySong ORDER BY id DESC")
    suspend fun queryAllHistorySongs():MutableList<HistorySong>?

    /**
     * 根据跟去songId查找指定歌曲
     */
    @Transaction
    @Query("SELECT * FROM historySong WHERE songId = (:songId) ORDER BY id DESC")
    suspend fun queryHistorySongBySongId(songId:String):MutableList<HistorySong>?

    /**
     * 删除全部数据
     */
    @Transaction
    @Query("DELETE FROM historysong")
    suspend fun deleteAllHistory():Int?

}