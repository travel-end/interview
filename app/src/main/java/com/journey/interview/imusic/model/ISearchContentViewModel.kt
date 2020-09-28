package com.journey.interview.imusic.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.Constant
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.state.State
import com.journey.interview.weatherapp.state.StateType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/26
 * @Description
 */
class ISearchContentViewModel:BaseViewModel() {
    val searchResult:MutableLiveData<SearchSong> = MutableLiveData()

    val searchAlbumResult :MutableLiveData<Album> = MutableLiveData()

    val songUrlResult :MutableLiveData<Map<String,Any>> = MutableLiveData()

    fun searchSong(searchContent:String,page:Int) {
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                iMusicApiService.search(searchContent,page)
            }
            result.ofMap()?.print().let { Log.e("JG","searchSongResult：$it") }
            searchResult.value= result
        }
    }

    fun searchAlbum(searchContent: String, offSet:Int) {
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                iMusicApiService.searchAlbum(searchContent,offSet)
            }
            result.ofMap()?.print().let { Log.e("JG","searchAlbumResult：$it") }
            searchAlbumResult.value= result
        }
    }

    fun getSongUrl(song: Song) {
        val songUrl = "${Constant.SONG_URL_DATA_LEFT}${song.songId}${Constant.SONG_URL_DATA_RIGHT}"
        Log.e("JG","songUrl: $songUrl")
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    songUrlApiService.getSongUrl(songUrl)
                }
            }.onSuccess {
                if (it.code == 0) {
                    val sipList = it.req_0?.data?.sip
                    var sip=""
                    if (sipList != null) {
                        if (sipList.isNotEmpty()) {
                            sip = sipList[0]
                        }
                    }
                    val purlList = it.req_0?.data?.midurlinfo
                    var pUrl:String?=""
                    if (purlList != null) {
                        if (purlList.isNotEmpty()) {
                            pUrl=purlList[0].purl
                        }
                    }
                    if (pUrl.isNullOrEmpty()) {
                        loadState.value = State(StateType.EMPTY,"该歌曲暂时没有版权，搜搜其它歌曲吧")
                    } else {
                        val pair="song" to song
                        val pair2 = "url" to "$sip$pUrl"
                        val map = mapOf(pair,pair2)
                        Log.e("JG","获取播放地址成功 sip&pUrl: ${pair2.second}")
                        songUrlResult.value = map
                    }
                } else {
                    loadState.value = State(StateType.EMPTY,"${it.code} :获取不到歌曲播放地址")
                }
            }.onFailure {
                handlerException(it,loadState)
            }
        }
    }


    // 歌手可能不止一个
    fun getSinger(data:ListBean):String? {
        val singerList = data.singer
        singerList?.let {
            if (it.isNotEmpty()) {
                val singer = StringBuilder(it[0].name?:"")
                if (it.size > 1) {
                    for (bean in it) {
                        singer.append("、").append(bean.name)
                    }
                }
                return singer.toString()
            }
        }
        return null
    }
}