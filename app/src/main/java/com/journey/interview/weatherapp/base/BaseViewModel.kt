package com.journey.interview.weatherapp.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.journey.interview.Constant
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.net.IMusicApiService
import com.journey.interview.imusic.net.IMusicRetrofitClient
import com.journey.interview.utils.getClass
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print
import com.journey.interview.weatherapp.net.ApiService
import com.journey.interview.weatherapp.net.RetrofitClient
import com.journey.interview.weatherapp.state.EmptyState
import com.journey.interview.weatherapp.state.State
import com.journey.interview.weatherapp.state.StateType
import kotlinx.coroutines.*
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @By Journey 2020/9/15
 * @Description
 */
open class BaseViewModel : ViewModel() {
    val loadState by lazy {
        MutableLiveData<State>()
    }

    val emptyState by lazy {
        MutableLiveData<EmptyState>()
    }

    val songUrlResult : MutableLiveData<Map<String, Any>> = MutableLiveData()

    protected val apiService: ApiService by lazy {
        RetrofitClient.instance.createApiService(ApiService::class.java)
    }
    protected val iMusicApiService: IMusicApiService by lazy {
        IMusicRetrofitClient.instance.apiService
    }
    protected val singerApiService: IMusicApiService by lazy {
        IMusicRetrofitClient.instance.singerApiService
    }
    protected val songUrlApiService: IMusicApiService by lazy {
        IMusicRetrofitClient.instance.songUrlApiService
    }

    protected fun request(block: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching {
                Log.d("BaseViewModel", "Success")
                block()
            }.onSuccess {

                Log.d("BaseViewModel", "Success")
            }.onFailure {
                Log.d("BaseViewModel", "Failed")
                handlerException(e = it, loadState = loadState)
            }
        }
    }

    protected fun <T> requestAsync(block:suspend () -> T):Deferred<T> {
        return viewModelScope.async {
            block.invoke()
        }
    }


    protected fun ioRequest(block:suspend () ->Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                block.invoke()
            }
        }
    }


    protected fun handlerException(e: Throwable, loadState: MutableLiveData<State>?) {
        loadState?.let {
            when (e) {
                is HttpException -> {
                    loadState.value = State(StateType.NETWORK_ERROR)
                }
                is ConnectException -> {
                    loadState.value = State(StateType.NETWORK_ERROR)
                }
                is ConnectTimeoutException -> {
                    loadState.value = State(StateType.NETWORK_ERROR)
                }
                is UnknownHostException -> {
                    loadState.value = State(StateType.NETWORK_ERROR)
                }
                is JsonParseException -> {
                    loadState.value = State(StateType.NETWORK_ERROR)
                }
                is NoClassDefFoundError -> {
                    loadState.value = State(StateType.NETWORK_ERROR)
                }
            }
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
}