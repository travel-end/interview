package com.journey.interview.imusic.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.Constant
import com.journey.interview.imusic.model.Song
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.state.State
import com.journey.interview.weatherapp.state.StateType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IMainViewModel:BaseViewModel() {
    val songUrlResult : MutableLiveData<Map<String, Any>> = MutableLiveData()
    /**
     * 獲取播放地址
     */
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
}