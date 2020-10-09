package com.journey.interview.imusic

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.shape.ShapeAppearanceModel
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.customizeview.swipecaptcha.core.GlideUtil
import com.journey.interview.imusic.act.IPlayActivity
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.vm.IMainViewModel
import com.journey.interview.utils.FileUtil
import com.journey.interview.utils.isServiceRunning
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_main.*
import kotlinx.android.synthetic.main.imusic_main_player.view.*

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IMainActivity : BaseLifeCycleActivity<IMainViewModel>() {
    private var mSong: Song? = null
    private var mPlayServiceBinder: IMusicPlayService.PlayStatusBinder? = null
    private var isExistService: Boolean = false// 服务是否存活
    private var mFlag:Boolean = false// 用作暂停的标记
    private var mMediaPlayer:MediaPlayer?=null
    private var mTime:Int?=null // 记录暂停的时间
    private var isSeek:Boolean = false// 标记是否在暂停的时候拖动进度条
    override fun layoutResId() = R.layout.imusic_act_main

    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlayServiceBinder = service as IMusicPlayService.PlayStatusBinder
        }
    }

    override fun initView() {
        super.initView()
        // 设置圆形图片
        bottom_player.player_song_icon.shapeAppearanceModel =
            ShapeAppearanceModel.Builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL)
                .build()

        mSong = FileUtil.getSong()
        mSong?.let {
            bottom_player.player_song_name.text = it.songName
            bottom_player.player_song_author.text = it.singer
            val currentTime = it.currentTime// todo 保存当前的歌曲播放进度 在play页面展示


            if (it.imgUrl == null) {

            } else {
                GlideUtil.loadImg(
                    this@IMainActivity,
                    it.imgUrl ?: "",
                    bottom_player.player_song_icon,
                    R.drawable.icon1, null
                )
            }
        }
        if (isServiceRunning(IMusicPlayService::class.java.name)) {
            bottom_player.btn_player.isSelected = true
            rotationAnim.start()
            isExistService = true
        }
        // 处理服务
        initService()
    }

    override fun initData() {
        super.initData()
        bottom_player.main_rv_play.setOnClickListener {
            mMediaPlayer = mPlayServiceBinder?.mediaPlayer
            when {
                mPlayServiceBinder?.isPlaying == true -> {
                    mTime = mMediaPlayer?.currentPosition
                    mPlayServiceBinder?.pause()
                    mFlag=true
                }
                mFlag -> {
                    mPlayServiceBinder?.resume()
                    mFlag = false
                }
                else -> {//退出程序重新打开后的情况
                    if (FileUtil.getSong()?.isOnline == true) {
                        mPlayServiceBinder?.playOnline()
                    } else {
                        mPlayServiceBinder?.play(FileUtil.getSong()?.listType)
                    }
                    mMediaPlayer = mPlayServiceBinder?.mediaPlayer
                    val currentTime = (mSong?.currentTime)!! * 1000
                    mMediaPlayer?.seekTo(currentTime.toInt())
                }
            }
        }
        findViewById<ConstraintLayout>(R.id.bottom_player).setOnClickListener {
            val song = FileUtil.getSong()
            if (song != null) {
                if (song.songName != null) {
                    val playIntent = Intent(this,IPlayActivity::class.java)
                    // 正在播放
                    if (mPlayServiceBinder?.isPlaying == true) {
                        val song2 = FileUtil.getSong()
                        val currenttime = mPlayServiceBinder?.currentTime?:0
                        Log.e("JG","当前播放进度：$currenttime")
                        song2?.currentTime = currenttime
                        FileUtil.saveSong(song2)
                        playIntent.putExtra(Constant.PLAY_STATUS,Constant.SONG_PLAY)
                    } else {// 暂停
                        playIntent.putExtra("online",true)
                    }
                    if (FileUtil.getSong()?.imgUrl != null) {

                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(playIntent,ActivityOptions.makeSceneTransitionAnimation(this@IMainActivity).toBundle())
                    } else {
                        startActivity(playIntent)
                    }
//                    overridePendingTransition(R.anim.slide_in_bottom,0)
                }
            }

        }
    }

    private fun initService() {
        // 启动服务 表示服务已经启动
        // 具体的播放暂停等功能，由ServiceBinder交互调用

        val playIntent = Intent(this, IMusicPlayService::class.java)
        // 退出程序后依然能够播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playIntent)
        } else {
            startService(playIntent)
        }
        bindService(playIntent, playConnection, Context.BIND_AUTO_CREATE)
    }


    override fun dataObserve() {
        super.dataObserve()
        // 播放歌曲被改变
        IMusicBus.observePlayStatusChange(this) {
            when (it) {
                Constant.SONG_PAUSE -> {
                    bottom_player.btn_player.isSelected = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        rotationAnim.pause()
                    }
                }
                Constant.SONG_RESUME -> {
                    bottom_player.btn_player.isSelected = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        rotationAnim.resume()
                    }
                }
                Constant.SONG_CHANGE -> {
                    mSong = FileUtil.getSong()
                    mSong?.let { s ->
                        bottom_player.player_song_name.text = s.songName
                        bottom_player.player_song_author.text = s.singer
                        bottom_player.btn_player.isSelected = true
                        rotationAnim.start()
                        if (!s.isOnline) {

                        } else {// 在线播放
                            Log.e("JG","封面图片url:${s.imgUrl}")
                            GlideUtil.loadImg(
                                this@IMainActivity,
                                s.imgUrl ?: "",
                                bottom_player.player_song_icon,
                                R.drawable.icon1, null
                            )
                        }
                    }
                }
            }
        }
    }

    //    private val rotationAnim by lazy {
//        animSet {
//            objectAnim {
//                target = bottom_player.player_song_icon
//                rotation = floatArrayOf(0.0f, 360.0f)
//                duration = 30000
//                interpolator = LinearInterpolator()
//                repeatCount = ValueAnimator.INFINITE// 循环播放
//                repeatMode = ValueAnimator.RESTART
//            }
//        }
//
//    }
    private val rotationAnim by lazy {
        ObjectAnimator.ofFloat(bottom_player.player_song_icon,"rotation", 0.0f, 360.0f).apply {
            duration = 30000
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE// 循环播放
            repeatMode = ValueAnimator.RESTART
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(playConnection)
        // 将播放的服务提升至前台服务
        val playIntent = Intent(this, IMusicPlayService::class.java)
        // 退出程序后依然能够播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playIntent)
        } else {
            startService(playIntent)
        }
        val song = FileUtil.getSong()
        // 保存歌曲播放时长位置
        song?.currentTime = mPlayServiceBinder?.currentTime ?: 0L
        FileUtil.saveSong(song)
    }
}