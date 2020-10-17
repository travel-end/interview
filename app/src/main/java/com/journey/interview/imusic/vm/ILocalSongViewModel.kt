package com.journey.interview.imusic.vm

import android.provider.MediaStore.Audio.*
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.journey.interview.Constant
import com.journey.interview.InterviewApp
import com.journey.interview.R
import com.journey.interview.imusic.download.IMusicDownloadUtil
import com.journey.interview.imusic.model.LocalSong
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.utils.getString
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print
import com.journey.interview.weatherapp.base.BaseViewModel
import com.journey.interview.weatherapp.state.EmptyState
import com.journey.interview.weatherapp.state.State
import com.journey.interview.weatherapp.state.StateType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ILocalSongViewModel:BaseViewModel() {

    val localSongResult:MutableLiveData<MutableList<LocalSong>?> = MutableLiveData()

    val localSaveResult:MutableLiveData<MutableList<LocalSong>?> = MutableLiveData()

    private fun initLocalMp3Info():MutableList<LocalSong>? {

        val context = InterviewApp.instance.applicationContext
        if (context != null) {
            val songList = mutableListOf<LocalSong>()
            val cursor = context.contentResolver.query(
                Media.EXTERNAL_CONTENT_URI,null,
                null,null,
                Media.DEFAULT_SORT_ORDER
            )
            if (cursor != null) {
                for (i in 0 until cursor.count) {
                    cursor.moveToNext()
                    val map3Info = LocalSong()
                    // 标题
                    var title = cursor.getString(cursor.getColumnIndex(Media.TITLE))
                    // 歌手
                    var artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST))
                    // 时长
                    val duration = cursor.getLong(cursor.getColumnIndex(Media.DURATION))
                    // 文件大小
                    val size = cursor.getLong(cursor.getColumnIndex(Media.SIZE))
                    // 文件路径
                    val url = cursor.getString(cursor.getColumnIndex(Media.DATA))
                    // 是否为音乐
                    val isMusic = cursor.getInt(cursor.getColumnIndex(Media.IS_MUSIC))
                    // 只把音乐添加到集合当中
                    if (isMusic != 0) {
                        if (size > 1000 * 800) {
                            // 分离出歌曲名和歌手
                            if (title.contains("-")) {
                                val array = title.split("-")
                                artist = array[0]
                                title = array[1]
                            }
                            map3Info.apply {
                                name = title.trim()
                                singer = artist
                                this.duration = duration
                                this.url = url
                                songId = "$i"
                            }
                            songList.add(map3Info)
                        }
                    }

                }
            }
            cursor?.close()

            // 加上下载的所有歌曲
            val downloadedSongs = IMusicDownloadUtil.getSongFromFile(Constant.STORAGE_SONG_FILE)
            downloadedSongs?.let {
                for (song in it) {
                    val mp3Info =LocalSong().apply {
                        name = song.name
                        singer = song.singer
                        this.duration = song.duration
                        this.url = song.url
                        songId = song.songId
                    }
                    songList.add(mp3Info)
                }
            }
            return songList
        }
        return null
    }

    /**
     * 检索本地音乐
     */
    fun getLocalSongs() {
        val songList = initLocalMp3Info()
        Log.e("JG","检索本地音乐：$songList")
        if (songList != null) {
            if (songList.isNotEmpty()) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        IMusicRoomHelper.deleteAllLocalSong()
                    }
                    val result = withContext(Dispatchers.IO) {
                        IMusicRoomHelper.saveLocalSong(songList)
                    }
                    if (result != null) {
                        localSongResult.value = songList
                    }
                }
            } else {
                localSongResult.value = null
            }
        } else {
            localSongResult.value = null
        }
    }

    /**
     * 查询数据库中本地音乐
     */
    fun getSavedLocalSongs() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.getAllLocalSongs()
            }
            Log.e("JG","查询本地音乐：$result ${result?.size}")
            if (result.isNullOrEmpty()) {
                localSaveResult.value=null
                emptyState.value = EmptyState(message = R.string.no_local_song.getString())
            } else {
                localSaveResult.value = result
            }

        }
    }
}