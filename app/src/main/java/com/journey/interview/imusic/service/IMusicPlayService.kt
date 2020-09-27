package com.journey.interview.imusic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.IMainActivity
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.net.IMusicApiService
import com.journey.interview.utils.FileUtil

/**
 * @By Journey 2020/9/27
 * @Description
 */
class IMusicPlayService:Service() {
    companion object {
        const val NOTIFICATION_ID = 98
    }
    private var mIsPlaying:Boolean =false
    private var isPause:Boolean = false
    private var mListType:Int?=null
    private var mCurrent:Int?=null

    private val mMediaPlayer by lazy {
        MediaPlayer()
    }

    private val mPlayStatusBinder = PlayStatusBinder()
     var mPlayMode = Constant.PLAY_ORDER // 默认设置为顺序播放

    override fun onCreate() {
        super.onCreate()
        mListType = FileUtil.getSong()?.listType
        startForeground(NOTIFICATION_ID,getNotification("爱音乐，开启你的私人音乐之旅o(*￣▽￣*)ブ"))
    }
    override fun onBind(intent: Intent?): IBinder? {
        mMediaPlayer.setOnCompletionListener {mp->
            IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_PAUSE)
            mCurrent = FileUtil.getSong()?.position
        }
        if (mListType != 0 && mListType != null) {
            mPlayStatusBinder.play(mListType!!)
        } else {

        }

        return mPlayStatusBinder

    }

    inner class PlayStatusBinder:Binder() {
        fun setPlayMode(mode:Int) {
            this@IMusicPlayService.mPlayMode = mode
        }

        // 播放音乐
        fun play(listType:Int?) {
            try {
                this@IMusicPlayService.mListType = listType
                mCurrent = FileUtil.getSong()?.position
                // 把各项参数恢复到初始状态
                mMediaPlayer.reset()
            } catch (e:Exception) {

            }
        }


        // 播放搜索歌曲
        fun playOnline() {
            try {
                mMediaPlayer.run {
                    val song  = FileUtil.getSong()
                    reset()
                    setDataSource(song?.url?:"")
                    prepare()
                    this@IMusicPlayService.mIsPlaying = true
                    // todo 保存至最近播放数据库

                    start()
                    IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_CHANGE)
                    // 改变通知栏歌曲
                    notificationManager.notify(NOTIFICATION_ID,getNotification("${song?.songName} - ${song?.singer}"))
                }
            } catch (e:Exception) {

            }
        }

        fun pause() {
            if (mMediaPlayer.isPlaying) {
                mIsPlaying =false
                mMediaPlayer.pause()
                isPause = true
                IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_PAUSE)
            }
        }

        fun resume() {
            if (mMediaPlayer.isPlaying) {
                if (isPause){
                    mMediaPlayer.start()
                    mIsPlaying =true
                    isPause = false
                    IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_RESUME)
                }
            }
        }

        fun stop() {
            mIsPlaying = false
            mMediaPlayer.stop()
            try {
                //  在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
                mMediaPlayer.prepare()
            }catch (e:Exception) {

            }
        }
        val isPlaying get() =mIsPlaying

        val mediaPlayer get() = mMediaPlayer

        val playService get() = this@IMusicPlayService

        val  currentTime get() = mMediaPlayer.currentPosition.toLong() / 1000

    }

    private val notificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun getNotification(title:String):Notification {
        val pi = PendingIntent.getActivity(this,0, Intent(this,IMainActivity::class.java),0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "play"
            val name = "播放歌曲"
            val channel = NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
            return  Notification.Builder(this,id)
                .setSmallIcon(R.drawable.icon1)
                .setContentIntent(pi)
                .setContentTitle(title)
                .build()
        } else {
            return NotificationCompat.Builder(this,"play")
                .setSmallIcon(R.drawable.icon1)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.icon1))
                .setContentIntent(pi)
                .setContentTitle(title)
                .build()
        }
    }
}