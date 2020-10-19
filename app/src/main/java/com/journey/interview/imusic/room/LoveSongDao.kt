package com.journey.interview.imusic.room

import androidx.room.*
import com.journey.interview.imusic.model.LoveSong

/**
 * @By Journey 2020/9/30
 * @Description
 */
@Dao
interface LoveSongDao {

    /**
     * 添加至我的喜欢
     */
    @Transaction
    @Insert(entity = LoveSong::class)
    suspend fun insertToMyLove(loveSong: LoveSong):Long?

    /**
     * 查找是否为我的喜欢歌曲
     */
    @Transaction
    @Query("SELECT * FROM lovesong WHERE songId=( :songId)")
    suspend fun queryIsMyLove(songId:String):MutableList<LoveSong>?

    /**
     * 查找全部喜欢歌曲
     */
    @Transaction
    @Query("SELECT * FROM lovesong ORDER BY id DESC")
    suspend fun queryAllMyLove():MutableList<LoveSong>?

    /**
     * 删除我的喜欢歌曲
     */
    @Transaction
    @Delete(entity = LoveSong::class)
    suspend fun deleteMyLove(loveSong: LoveSong):Int?

    @Transaction
    @Query("DELETE FROM lovesong WHERE songId=(:songId)")
    suspend fun deleteLoveSongBySongId(songId: String):Int?
}