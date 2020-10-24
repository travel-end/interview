package com.journey.interview.imusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.imusic.model.recommend.RecomSong
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IMainViewModel:BaseViewModel() {
    val recomSongs:MutableLiveData<List<RecomSong>?> = MutableLiveData()
    fun findAllRecomSongs() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.findAllRecomSongs()
            }
            recomSongs.value = result
        }
    }
}