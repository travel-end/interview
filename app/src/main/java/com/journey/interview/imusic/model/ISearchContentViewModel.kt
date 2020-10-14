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
 * ListBean(
 * albumid=6598824,
 * albummid=004UvnL62KXhCQ,
 * albumname=夜来寒雨晓来风,
 * albumname_hilight=<em>夜来寒雨</em>晓来风,
 * alertid=2,
 * belongCD=0,
 * cdIdx=1,
 * chinesesinger=0,
 * docid=1591846276313292817,
 * interval=187,
 * isonly=0,
 * lyric=,
 * lyric_hilight=,
 * media_mid=004DrG5A2nm7q2,
 * msgid=16, newStatus=2,
 * nt=3754327673,
 * pubtime=1554825600,
 * pure=0,
 * size128=3004082,
 * size320=7509891,
 * sizeape=0,
 * sizeflac=42191137,
 * sizeogg=4526726,
 * songid=231254728,
 * songmid=004DrG5A2nm7q2,
 * songname=夜来寒雨晓来风,
 * songname_hilight=<em>夜来寒雨</em>晓来风,
 * strMediaMid=004DrG5A2nm7q2,
 * stream=1,
 * switchX=0, t=1, tag=11, type=0, ver=0,
 * singer=[SingerBean(id=2684565, mid=003oe0Gi4LoOZ3, name=鸾音社, name_hilight=鸾音社)])
 */
class ISearchContentViewModel:BaseViewModel() {
    val searchResult:MutableLiveData<SearchSong> = MutableLiveData()

    val searchAlbumResult :MutableLiveData<Album> = MutableLiveData()

    val songUrlResult :MutableLiveData<Map<String,Any>> = MutableLiveData()

    fun searchSong(searchContent:String,page:Int) {
        loadState.value = State(StateType.LOADING)
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                iMusicApiService.search(searchContent,page)
            }
//            result.ofMap()?.print().let { Log.e("JG","关键词搜索结果：$it") }
            val songList = result.data?.song?.list
            Log.e("JG","关键词搜索结果：${songList}")
            Log.e("JG","关键词搜索结果第一条：${songList!![0]}")
            loadState.value = State(StateType.DISMISSING)
            searchResult.value= result
        }
    }

    fun searchAlbum(searchContent: String, offSet:Int) {
        loadState.value = State(StateType.LOADING)
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                iMusicApiService.searchAlbum(searchContent,offSet)
            }
            result.ofMap()?.print().let { Log.e("JG","searchAlbumResult：$it") }
            loadState.value = State(StateType.DISMISSING)
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
//                        Log.e("JG","获取播放地址成功 sip&pUrl: ${pair2.second}")
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