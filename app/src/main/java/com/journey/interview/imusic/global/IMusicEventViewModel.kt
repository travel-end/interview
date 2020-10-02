package com.journey.interview.imusic.global

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @By Journey 2020/9/27
 * @Description
 */
class IMusicEventViewModel:ViewModel() {
    // 监听播放状态的改变
    val songStatus:MutableLiveData<Int> = MutableLiveData()

    // 监听 喜欢歌曲/取消喜欢歌曲的改变99
    val loveSongStatus:MutableLiveData<Boolean> = MutableLiveData()
}