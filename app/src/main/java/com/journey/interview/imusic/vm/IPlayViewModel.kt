package com.journey.interview.imusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.Constant
import com.journey.interview.imusic.model.*
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.utils.FileUtil
import com.journey.interview.utils.SpUtil
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

    val queryIsMyLoveResult: MutableLiveData<Boolean?> = MutableLiveData()

    val lrcError: MutableLiveData<String?> = MutableLiveData()

    val songIdResult: MutableLiveData<String> = MutableLiveData()

    val localSongId: MutableLiveData<String> = MutableLiveData()

    val localSongImg: MutableLiveData<String?> = MutableLiveData()

    /**
     * 获取在线歌词
     */
    fun getOnlineSongLrc(songId: String, songType: Int, songName: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                iMusicApiService.getOnlineSongLrc(songId)
            }
            songLrc.value = result
            // 如果是本地音乐 则将歌词保存起来
            if (songType == Constant.SONG_LOCAL) {
                FileUtil.saveLrcToNative(result.lyric, songName)
            }
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

    fun getSingerName(): String? {
        val song = FileUtil.getSong()
        song?.let {
            val singer = it.singer
            if (singer != null) {
                return if (singer.contains("/")) {
                    val s = singer.split("/")
                    s[0].trim()
                } else {
                    singer.trim()
                }
            }
        }
        return null
    }

    fun updateLocalSong(localSong: LocalSong) {
        ioRequest {
            IMusicRoomHelper.updateLocalSong(localSong)
        }
    }

    fun getSongId(song: String, duration: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                iMusicApiService.search(song, 1)
            }
            if (result.code == 0) {
                matchSong(result.data?.song?.list, duration)
            } else {
                lrcError.value = null
            }
        }
    }

    private fun matchSong(listBeans: List<ListBean>?, duration: Int) {
        var isFind: Boolean = false
        if (!listBeans.isNullOrEmpty()) {
            for (bean in listBeans) {
                if (duration == bean.interval) {
                    isFind = true
                    songIdResult.value = bean.songmid
                }
            }
        }
        // 如果找不到歌曲id就传输找不到歌曲的消息
        if (!isFind) {
            lrcError.value = Constant.SONG_ID_UNFIND
        }
    }

    private fun matchLrc(listBeans: List<ListBean>?, duration: Int) {
        var isFind: Boolean = false
        if (!listBeans.isNullOrEmpty()) {
            for (bean in listBeans) {
                if (duration == bean.interval) {
                    isFind = true
                    localSongId.value = bean.songmid
                }
            }
        }
        // 如果找不到歌曲id就传输找不到歌曲的消息
        if (!isFind) {
            lrcError.value = Constant.SONG_ID_UNFIND
        }
    }

    fun getLocalSongImg(singer: String, songName: String, duration: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                singerApiService.getLocalSingerImg(singer)
            }
            result.result?.artists?.let {
                val img = it[0].img1v1Url
                localSongImg.value = img
            }

            val searchResult = withContext(Dispatchers.IO) {
                iMusicApiService.search(songName, 1)
            }
            if (searchResult.code == 0) {
                matchLrc(searchResult.data?.song?.list, duration)
            } else {
                lrcError.value = null
            }
        }
    }

    fun setPlayMode(mode:Int) {
        SpUtil.saveValue(Constant.KEY_PLAY_MODE,mode)
    }

    fun getPlayMode():Int {
        return SpUtil.getInt(Constant.KEY_PLAY_MODE,Constant.PLAY_ORDER)
    }
}