package com.journey.interview.imusic.global

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.journey.interview.InterviewApp
import com.journey.interview.imusic.model.DownloadEvent

/**
 * @By Journey 2020/9/27
 * @Description
 */
object IMusicBus {

    val musicEvent = InterviewApp.instance.getGlobalViewModel(IMusicEventViewModel::class.java)

    /**
     * 播放状态的改变
     */
    fun sendPlayStatusChangeEvent(status:Int) {
        musicEvent.songStatus.value = status
    }

    fun observePlayStatusChange(o: LifecycleOwner, block:(Int)->Unit) {
        musicEvent.songStatus.observe(o, Observer{
            block.invoke(it)
        })
    }

    /**
     * 喜欢/取消喜欢的音乐
     */
    fun sendLoveSongChange(status:Boolean) {
        musicEvent.loveSongStatus.value = status
    }

    fun observeLoveSongChange(o: LifecycleOwner, block:(Boolean)->Unit) {
        musicEvent.loveSongStatus.observe(o, Observer{
            block.invoke(it)
        })
    }

    /**
     * 播放曲目列表数量的改变
     */
    fun sendSongListNumChange(songType:Int) {
        musicEvent.songListNum.value = songType
    }

    fun observeSongListNumChange(o:LifecycleOwner,block: (Int) -> Unit) {
        musicEvent.songListNum.observe(o,Observer{
            block.invoke(it)
        })
    }

    /**
     * 下載歌曲的狀態
     */
    fun sendDownloadSongStatusChange(downloadEvent: DownloadEvent) {
        musicEvent.downloadSongStatus.value =downloadEvent
    }

    /**
     * 刷新本地歌曲
     */
    fun sendRefreshLocalSong(event:Int) {
        musicEvent.commonEventStatus.value = event
    }

    fun observeRefreshLocalSongChange(o:LifecycleOwner,block: (Int) -> Unit) {
        musicEvent.commonEventStatus.observe(o,Observer<Int>{
            block.invoke(it)
        })
    }
}