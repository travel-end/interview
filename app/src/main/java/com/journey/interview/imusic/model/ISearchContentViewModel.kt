package com.journey.interview.imusic.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/26
 * @Description
 */
class ISearchContentViewModel:BaseViewModel() {
    val searchResult:MutableLiveData<SearchSong> = MutableLiveData()

    fun searchSong(searchContent:String,page:Int) {
        viewModelScope.launch {
            val result =  withContext(Dispatchers.IO) {
                iMusicApiService.search(searchContent,page)
            }
            result.ofMap()?.print().let { Log.e("JG","searchSongResult：$it") }
            searchResult.value= result
        }
    }
}