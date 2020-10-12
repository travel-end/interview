package com.journey.interview.imusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.imusic.model.HistorySong
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IHistorySongViewModel:BaseViewModel() {

    val historySongsResult:MutableLiveData<MutableList<HistorySong>?> = MutableLiveData()
    val deleteSongsResult :MutableLiveData<Boolean> = MutableLiveData()
    fun getAllHistorySongs() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.queryAllHistorySongs()
            }
            historySongsResult.value = result
        }
    }

    fun deleteAllHistorySongs() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.deleteAllHistorySong()
            }
            if (result != null) {
                deleteSongsResult.value = true
            }
        }
    }
}