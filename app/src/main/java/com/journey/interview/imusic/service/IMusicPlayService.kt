package com.journey.interview.imusic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.IMainActivity
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.HistorySong
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.utils.FileUtil
import kotlinx.coroutines.*

/**
 * @By Journey 2020/9/27
 * @Description
 */
class IMusicPlayService : Service() {
    companion object {
        const val NOTIFICATION_ID = 98
    }

    private var mIsPlaying: Boolean = false
    private var mIsPause: Boolean = false
    private var mListType: Int? = null// 歌曲列表类别,0表示当前没有列表，即可能在播放网络歌曲
    private var mCurrent: Int? = null

    private val mMediaPlayer by lazy {
        MediaPlayer()
    }

    private val mPlayStatusBinder = PlayStatusBinder()
    var mPlayMode = Constant.PLAY_ORDER // 默认设置为顺序播放

    override fun onCreate() {
        super.onCreate()
        mListType = FileUtil.getSong()?.listType
        startForeground(NOTIFICATION_ID, getNotification("爱音乐，开启你的私人音乐之旅o(*￣▽￣*)ブ"))
    }

    override fun onBind(intent: Intent?): IBinder? {
        mMediaPlayer.setOnCompletionListener { mp ->
            IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_PAUSE)
            mCurrent = FileUtil.getSong()?.position
        }
        // 播放的可能是除了网络歌曲之外的其他模式
        if (mListType != 0 && mListType != null) {
            mPlayStatusBinder.play(mListType!!)
        } else {
            mPlayStatusBinder.stop()
        }
        /**
         * MediaPlayer切歌进入setOnCompletionListener的问题
         * 因为直接切歌会发生错误，所以增加错误监听器。返回true。就不会回调onCompletion方法了。
         */
        mMediaPlayer.setOnErrorListener { _, _, _ -> true }
        return mPlayStatusBinder

    }

    inner class PlayStatusBinder : Binder() {
        fun setPlayMode(mode: Int) {
            this@IMusicPlayService.mPlayMode = mode
        }

        // 播放音乐
        fun play(listType: Int?) {
            try {
                this@IMusicPlayService.mListType = listType
                mCurrent = FileUtil.getSong()?.position
                // 把各项参数恢复到初始状态
                mMediaPlayer.reset()
            } catch (e: Exception) {

            }
        }


        // 播放搜索歌曲
        fun playOnline() {
            try {
                val song = FileUtil.getSong()
                mMediaPlayer.run {
                    reset()
                    setDataSource(song?.url ?: "")
                    prepare()
                    this@IMusicPlayService.mIsPlaying = true
                    saveToHistorySong()
                    start()
                    IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_CHANGE)
                    // 改变通知栏歌曲
                    notificationManager.notify(
                        NOTIFICATION_ID,
                        getNotification("${song?.songName} - ${song?.singer}")
                    )
                }
            } catch (e: Exception) {

            }
        }

        fun pause() {
            if (mMediaPlayer.isPlaying) {
                mIsPlaying = false
                mMediaPlayer.pause()
                mIsPause = true
                IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_PAUSE)
            }
        }

        fun resume() {
            if (mIsPause) {
                mMediaPlayer.start()
                mIsPlaying = true
                mIsPause = false
                IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_RESUME)
            }
        }

        fun next() {
            IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_RESUME)

            // 如果是网络歌曲 todo 播放网络 歌曲的另一首



            if (mListType != 0 && mListType != null) {
                mPlayStatusBinder.play(mListType!!)
            }
        }

        fun last() {
            IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_RESUME)

            if (mListType != 0 && mListType != null) {
                mPlayStatusBinder.play(mListType!!)
            }
        }

        fun stop() {
            mIsPlaying = false
            mMediaPlayer.stop()
            try {
                //  在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
                mMediaPlayer.prepare()
            } catch (e: Exception) {

            }
        }

        val isPlaying get() = mIsPlaying

        val isPausing get() = mIsPause

        val mediaPlayer get() = mMediaPlayer

        val playService get() = this@IMusicPlayService

        // 播放实时进度
        val currentTime get() = mMediaPlayer.currentPosition.toLong() / 1000

    }
    private var job:Job?=null
    private fun saveToHistorySong() {
        val song = FileUtil.getSong()
        song?.let {
            job =GlobalScope.launch {
//                val result =withContext(Dispatchers.IO) {
//                    IMusicRoomHelper.findHistorySongBySongId(it.songId?:"")
//                }
                val historySong = HistorySong().apply {
                    songId = it.songId
                    qqId = it.qqId
                    name = it.songName
                    singer = it.singer
                    url = it.url
                    pic = it.imgUrl
                    isOnline = it.isOnline
                    isDownload = it.isDownload
                    duration = it.duration
                    mediaId = it.mediaId
                }

//                if (result != null) {
//                    if (result.size == 1) {
//                        val result2 =withContext(Dispatchers.IO) {
//                            IMusicRoomHelper.deleteHistorySong(historySong)
//                        }
//                    }
//                }
                val saveResult = withContext(Dispatchers.IO) {
                    IMusicRoomHelper.saveToHistorySong(historySong)
                }
                if (saveResult != null) {
                    // 更新界面
                    withContext(Dispatchers.Main) {
                        IMusicBus.sendSongListNumChange(Constant.LIST_TYPE_HISTORY)
                        Log.e("JG","保存最近播放曲目成功:  $saveResult")
                        // todo 如果最近播放的曲目>100 将第一个删除
                    }
                }
            }
        }
    }

    private val notificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun getNotification(title: String): Notification {
        val pi = PendingIntent.getActivity(this, 0, Intent(this, IMainActivity::class.java), 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "play"
            val name = "播放歌曲"
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
            return Notification.Builder(this, id)
                .setSmallIcon(R.drawable.icon1)
                .setContentIntent(pi)
                .setContentTitle(title)
                .build()
        } else {
            return NotificationCompat.Builder(this, "play")
                .setSmallIcon(R.drawable.icon1)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon1))
                .setContentIntent(pi)
                .setContentTitle(title)
                .build()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("JG","播放Service onUnbind--->")
        return true
    }

    override fun onDestroy() {
        Log.d("JG","播放Service被销毁了--->")
        mMediaPlayer.stop()
        mMediaPlayer.release()
        stopForeground(true)
        if (job?.isCancelled == false) {
            job?.cancel()
        }
    }
}