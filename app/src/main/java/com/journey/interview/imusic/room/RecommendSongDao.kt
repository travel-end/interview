package com.journey.interview.imusic.room

import androidx.room.*
import com.journey.interview.imusic.model.recommend.RecomSong

/**
 * @By Journey 2020/10/24
 * @Description
 * OnConflictStrategy:插入重复则 替换
 * 对于查询出来的数据直接更新ui的  可以使用LiveData接受
 * desc 降序
 * asc 升序
 */
@Dao
interface RecommendSongDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = RecomSong::class)
    suspend fun addRecommendSong(recomSong: RecomSong)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = RecomSong::class)
    suspend fun addRecommendSongs(recomSongs: List<RecomSong>)

    @Transaction
    @Query("SELECT * FROM recomSong ORDER BY id")
    suspend fun queryAllRecommendSong():MutableList<RecomSong>?

    @Transaction
    @Delete(entity = RecomSong::class )
    suspend fun deleteAllRecommendSongs(recomSongs: List<RecomSong>)


}