package com.journey.interview.weatherapp.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.net.IMusicApiService
import com.journey.interview.imusic.net.IMusicRetrofitClient
import com.journey.interview.utils.getString
import com.journey.interview.weatherapp.net.ApiService
import com.journey.interview.weatherapp.net.RetrofitClient
import com.journey.interview.weatherapp.state.EmptyState
import com.journey.interview.weatherapp.state.ErrorState
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

    val errorState by lazy {
        MutableLiveData<ErrorState>()
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
                handlerException(e = it, state = ErrorState())
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
            try {
                withContext(Dispatchers.IO) {
                    block.invoke()
                }
            } catch (e:Exception) {
                handlerException(e = e, state = ErrorState(message = R.string.request_error.getString()))
            }

        }
    }


    protected fun handlerException(e: Throwable, state: ErrorState) {
            when (e) {
                is HttpException -> {
                    errorState.value = state
                }
                is ConnectException -> {
                    errorState.value = state
                }
                is ConnectTimeoutException -> {
                    errorState.value = state
                }
                is UnknownHostException -> {
                    errorState.value = state
                }
                is JsonParseException -> {
                    errorState.value = state
                }
                is NoClassDefFoundError -> {
                    errorState.value = state
                }
                else->{
                    errorState.value = ErrorState(message = R.string.error_donot_know.getString())
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
                handlerException(it, ErrorState())
            }
        }
    }
}