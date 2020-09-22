package com.journey.interview.weatherapp.room

import androidx.room.*
import com.journey.interview.weatherapp.model.Place

/**
 * @By Journey 2020/9/22
 * @Description
 */
@Dao
interface PlaceDao {
    // 插入数据
    @Transaction
    @Insert(entity = Place::class)
    suspend fun insertPlace(place: Place):Long

    // 查询数据（查询所有已选城市）
    @Transaction
    @Query("SELECT * FROM place ORDER BY primaryKey desc")
    suspend fun queryAllPlace():MutableList<Place>

    @Transaction
    @Query("SELECT * FROM place WHERE name = (:name)")
    suspend fun queryPlaceByName(name:String):Place?

    @Transaction
    @Query("SELECT * FROM place ORDER BY primaryKey desc")
    suspend fun queryFirstPlace() :Place?

    @Transaction
    @Delete(entity = Place::class)
    suspend fun deletePlace(place: Place):Int

    @Transaction
    @Query("DELETE FROM place")
    suspend fun deleteAll()

}