package com.journey.interview.imusic.room

import androidx.room.*
import com.journey.interview.imusic.model.LocalSong

@Dao
interface LocalSongDao {

    /**
     * 插入本地音乐
     */
    @Transaction
    @Insert(entity = LocalSong::class)
    suspend fun insertLocalSong(localSong: LocalSong):Long?

    /**
     * 删除本地音乐
     */
    @Transaction
    @Delete(entity = LocalSong::class)
    suspend fun deleteLocalSongs(localSong: LocalSong):Int?

    /**
     * 查找本地音乐
     */
    @Transaction
    @Query("SELECT * FROM localsong ORDER BY id DESC")
    suspend fun queryAllLocalSongs():MutableList<LocalSong>?
}