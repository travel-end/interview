package com.journey.interview.imusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.imusic.model.OnlineSongLrc
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/28
 * @Description
 */
class IPlayViewModel:BaseViewModel() {
    val songLrc:MutableLiveData<OnlineSongLrc> = MutableLiveData()

    fun getOnlineSongLrc(songId:String,songType:Int) {
        viewModelScope.launch {
            val result  = withContext(Dispatchers.IO) {
                iMusicApiService.getOnlineSongLrc(songId)
            }
            songLrc.value = result
        }
    }
}