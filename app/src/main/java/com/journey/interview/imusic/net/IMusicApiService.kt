package com.journey.interview.imusic.net

import com.journey.interview.imusic.model.SearchSong
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @By Journey 2020/9/26
 * @Description
 */
interface IMusicApiService {
    /**
     * 搜索歌曲：https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p=2&n=2&w=周杰伦&format=json
     * n为一页30首
     */
    @GET("soso/fcgi-bin/client_search_cp?n=30&format=json")
    suspend fun search(@Query("w") searchContent:String,@Query("p") offSet:Int):SearchSong

    /**
     * 搜索专辑：https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p=1&n=2&w=林宥嘉&format=json&t=8
     * @param seek 搜索关键字
     * @param offset 页数
     */
    @GET("soso/fcgi-bin/client_search_cp?n=20&format=json&t=8")
    suspend fun searchAlbum(@Query("albummid") id:String)

}