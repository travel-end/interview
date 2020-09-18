package com.journey.interview.weatherapp.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.journey.interview.utils.getClass
import com.journey.interview.weatherapp.state.State
import com.journey.interview.weatherapp.state.StateType
import kotlinx.coroutines.launch
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @By Journey 2020/9/15
 * @Description
 */
open class BaseViewModel<R:BaseRepository>:ViewModel() {
    val loadState by lazy {
        MutableLiveData<State>()
    }

    val mRepository:R by lazy {
        getClass<R>(this).getDeclaredConstructor(MutableLiveData::class.java).newInstance(loadState)
    }

    protected fun launch(block:suspend () -> Unit) {
        viewModelScope.launch {
            runCatching {
                Log.d("BaseViewModel","Success")
                block()
            }.onSuccess {
                Log.d("BaseViewModel","Success")
            }.onFailure {
                Log.d("BaseViewModel","Failed")
                handlerException(e = it,loadState = loadState)
            }
        }
    }

    private fun handlerException(e:Throwable,loadState:MutableLiveData<State>?) {
        loadState?.let {
            when(e) {
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
}