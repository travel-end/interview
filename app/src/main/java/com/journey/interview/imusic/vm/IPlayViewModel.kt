package com.journey.interview.imusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.imusic.model.LoveSong
import com.journey.interview.imusic.model.OnlineSongLrc
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/28
 * @Description
 */
class IPlayViewModel : BaseViewModel() {
    val songLrc: MutableLiveData<OnlineSongLrc> = MutableLiveData()

    val addLoveSongResult: MutableLiveData<Long?> = MutableLiveData()

    val deleteLoveSongResult: MutableLiveData<Int?> = MutableLiveData()

    val queryIsMyLoveResult : MutableLiveData<Boolean?> = MutableLiveData()

    /**
     * 获取在线歌词
     */
    fun getOnlineSongLrc(songId: String, songType: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                iMusicApiService.getOnlineSongLrc(songId)
            }
            songLrc.value = result
        }
    }

    /**
     * 添加至我的喜欢歌曲
     */
    fun addToMyLove(song: Song) {
        viewModelScope.launch {
            val loveSong = LoveSong(
                songId = song.songId,
                mediaId = song.mediaId,
                qqId = song.qqId,
                name = song.songName,
                singer = song.singer,
                url = song.url,
                pic = song.imgUrl,
                duration = song.duration,
                isOnline = song.isOnline,
                isDownload = song.isDownload
            )
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.addToMyLoveSong(loveSong)
            }
            addLoveSongResult.value = result
        }
    }

    fun deleteFromMyLoveSong(song: Song) {
        viewModelScope.launch {
            val loveSong = LoveSong(
                songId = song.songId,
                mediaId = song.mediaId,
                qqId = song.qqId,
                name = song.songName,
                singer = song.singer,
                url = song.url,
                pic = song.imgUrl,
                duration = song.duration,
                isOnline = song.isOnline,
                isDownload = song.isDownload
            )
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.deleteFromMyLoveSong(loveSong)
            }
            deleteLoveSongResult.value = result
        }
    }

    fun queryIsMyLoveSong(songId: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.isMyLoveSong(songId)
            }
            queryIsMyLoveResult.value = result
        }
    }


}