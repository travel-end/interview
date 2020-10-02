package com.journey.interview.imusic.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.imusic.model.LoveSong
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ILoveSongViewModel:BaseViewModel() {
    val loveSongList :MutableLiveData<MutableList<LoveSong>> = MutableLiveData()

    fun getAllMyLoveSong() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.getAllMyLoveSong()
            }
            if (result != null) {
                result.ofMap()?.print().let { Log.e("JG",it?:"") }
                loveSongList.value = result.toMutableList()
            }
        }
    }

}