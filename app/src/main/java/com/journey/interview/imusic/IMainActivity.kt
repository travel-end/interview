package com.journey.interview.imusic

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.customizeview.progressbar.DigistProgressBar
import com.journey.interview.customizeview.swipecaptcha.core.GlideUtil
import com.journey.interview.imusic.act.IPlayActivity
import com.journey.interview.imusic.download.IMusicDownloadUtil
import com.journey.interview.imusic.global.Bus
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.ListBean
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.util.AssetsUtil
import com.journey.interview.imusic.vm.IMainViewModel
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.StringUtils
import com.journey.interview.utils.isServiceRunning
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_main.*
import kotlinx.android.synthetic.main.imusic_main_player.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IMainActivity : BaseLifeCycleActivity<IMainViewModel>() {
    private var mSong: Song? = null
    private var mFirstSong:Song?=null
    private var mPlayServiceBinder: IMusicPlayService.PlayStatusBinder? = null
    private var isExistService: Boolean = false// 服务是否存活
    private var mFlag: Boolean = false// 用作暂停的标记
    private lateinit var mPlayBtn: ImageView
    private var mMediaPlayer: MediaPlayer?=null
//    private var mTime: Int? = null // 记录暂停的时间
    private var isSeek: Boolean = false// 标记是否在暂停的时候拖动进度条
    private var isChange: Boolean = false
    private var mCurrentTime: Long? = null
    private lateinit var progressBar: DigistProgressBar
    private var progressBarThread: Thread? = null
    override fun layoutResId() = R.layout.imusic_act_main

    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlayServiceBinder = service as IMusicPlayService.PlayStatusBinder
            Log.e("JG","mPlayServiceBinder$mPlayServiceBinder")
            if (isExistService) {
                startProgressBar()
            }
        }
    }

    override fun initView() {
        super.initView()
        // 设置圆形图片
        bottom_player.player_song_icon.shapeAppearanceModel =
            ShapeAppearanceModel.Builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL)
                .build()
        progressBar = bottom_player.play_progress_bar
        mSong = SongUtil.getSong()

        mSong?.let {
            setSongUi(it)
        }
        // 没有歌曲
        if (mSong == null) {
            queryLocalSong()// 查询本地音乐
        }

        // 保證播放音乐状态退出程序后 再次进入 仍然继续播放音乐（或者暂停状态退出程序 进入后仍然保持之前的状态）
        if (isServiceRunning(IMusicPlayService::class.java.name)) {
            val playStatus = mSong?.playStatus
            if (playStatus!=null && playStatus==Constant.SONG_PLAY) {//如果当前是处于播放的状态
                bottom_player.btn_player.isSelected = true
                rotationAnim.start()
                isExistService = true
            }
        }
        // 处理服务
        initService()
    }

    private fun queryLocalSong() {
        GlobalScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.getAllLocalSongs()
            }
            // 如果有本地音乐  则使用本地音乐的第一首歌作为首次见面
            if (!result.isNullOrEmpty()) {
                val firstLocalSong = result[0]
                val song  = Song()
                song.songName = firstLocalSong.name
                song.singer = firstLocalSong.singer
                song.url = firstLocalSong.url
                song.duration = firstLocalSong.duration?.toInt()?:0
                song.position = 0
                song.isOnline = false
                song.songId = firstLocalSong.songId
                song.listType = Constant.LIST_TYPE_LOCAL
                mSong = song
                SongUtil.saveSong(mSong)
                setSongUi(song)
            } else {// 本地音乐也没有  使用json文件中推荐的歌曲
                val firstSong = AssetsUtil.readAssetsJson("first_song.json")
                firstSong?.let {
                    val s = Gson().fromJson(it,ListBean::class.java)
                    val song= Song().apply {
                        songId = s.songmid //004DrG5A2nm7q2
                        singer = StringUtils.getSinger(s)// 鸾音社
                        songName = s.songname// 夜来寒雨晓来风
                        //http://y.gtimg.cn/music/photo_new/T002R180x180M000004UvnL62KXhCQ.jpg
                        imgUrl = "${Constant.ALBUM_PIC}${s.albummid}${Constant.JPG}"
                        duration = s.interval//187  (秒)
                        isOnline = true
                        mediaId = s.strMediaMid//004DrG5A2nm7q2
                        albumName = s.albumname//夜来寒雨晓来风
                        // 是否已经下载过了（初次搜索为false）
                        isDownload = IMusicDownloadUtil.isExistOfDownloadSong(s.songmid?:"")//003IHI2x3RbXLS
                    }
                    mFirstSong = song
                    withContext(Dispatchers.Main) {
                        setSongUi(song)
                    }
//                    mViewModel.getSongUrl(song)
                }
            }
        }
    }

    private fun setSongUi(song: Song) {
        bottom_player.player_song_name.text = song.songName
        bottom_player.player_song_author.text = song.singer
        mCurrentTime = song.currentTime// todo 保存当前的歌曲播放进度 在play页面展示
        progressBar.max = song.duration ?: 100
        progressBar.progress = song.currentTime.toInt() ?: 0
        Log.e("JG", "播放进度：$mCurrentTime")
        if (song.imgUrl == null) {
            SongUtil.setLocalCoverImg(
                this@IMainActivity,
                song.singer ?: "",
                bottom_player.player_song_icon
            )
        } else {
            GlideUtil.loadImg(
                this@IMainActivity,
                song.imgUrl ?: "",
                bottom_player.player_song_icon,
                R.drawable.error_icon, R.drawable.error_icon,null
            )
        }
    }

    override fun initData() {
        super.initData()
        mMediaPlayer = mPlayServiceBinder?.mediaPlayer
        /* 点击播放/点击暂停*/
        bottom_player.main_rv_play.setOnClickListener {
//            Log.e("JG","mMediaPlayer$mMediaPlayer")
            when {
                mPlayServiceBinder?.isPlaying == true -> {
//                    mTime = mMediaPlayer?.currentPosition// 记录暂停的时间
                    mPlayServiceBinder?.pause()
                    mFlag = true
                }
                mFlag -> {
                    mPlayServiceBinder?.resume()
                    mFlag = false
                }
                else -> {//退出程序重新打开后的情况（此时处于暂停）
                    Log.e("JG","上一次的播放进度:$mCurrentTime")
                    val song = SongUtil.getSong()
                    if (song != null) {
                        if (song.isOnline) {
                            // todo
                            mPlayServiceBinder?.playOnline(((mCurrentTime?:0) * 1000).toInt())
//                        mPlayServiceBinder?.resume()
                        } else {
                            mPlayServiceBinder?.play(SongUtil.getSong()?.listType)
                        }
                    } else {
                        mViewModel.getSongUrl(mFirstSong!!)
                    }

                    // todo 这里有个bug  playOnline后会reset mediaPlayer对象  seekTo方法应该是不生效的
//                    mMediaPlayer = mPlayServiceBinder?.mediaPlayer// 这个mediaPlayer对象需要重新获取  之前的由于程序退出后已经被销毁
//                    val currentTime = mCurrentTime!! * 1000
//                    mMediaPlayer?.seekTo(currentTime.toInt())
                }
            }
        }
        findViewById<ConstraintLayout>(R.id.bottom_player).setOnClickListener {
            val song = SongUtil.getSong()
            if (song != null) {
                if (song.songName != null) {
                    val playIntent = Intent(this, IPlayActivity::class.java)
                    // 正在播放
                    if (mPlayServiceBinder?.isPlaying == true) {
                        val song2 = SongUtil.getSong()
                        val currenttime = mPlayServiceBinder?.currentTime ?: 0
//                        Log.e("JG","当前播放进度：$currenttime")
                        song2?.currentTime = currenttime
                        SongUtil.saveSong(song2)// 传递当前的播放进度
                        playIntent.putExtra(Constant.PLAY_STATUS, Constant.SONG_PLAY)// 传递当前的播放状态
                    } else {// 暂停
                        val song3 = SongUtil.getSong()
                        Log.e("JG", "暂停播放进度：${progressBar.progress.toLong()}")
                        song3?.currentTime = progressBar.progress.toLong()
                        playIntent.putExtra(Constant.PLAY_STATUS, Constant.SONG_PAUSE)
                        SongUtil.saveSong(song3)
                    }
                    if (SongUtil.getSong()?.imgUrl != null) {
                        playIntent.putExtra("online", true)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(
                            playIntent,
                            ActivityOptions.makeSceneTransitionAnimation(this@IMainActivity)
                                .toBundle()
                        )
                    } else {
                        startActivity(playIntent)
                    }
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
        Bus.observe<Int>(Constant.SONG_STATUS_CHANGE,this) {
//            Log.e("JG","--->坚挺到播放歌曲的改变")
            when(it) {
                Constant.SONG_CHANGE -> {
                    Log.e("JG", "---->播放完整曲目")
                    mSong = SongUtil.getSong()
                    mSong?.let { s ->
                        bottom_player.player_song_name.text = s.songName
                        bottom_player.player_song_author.text = s.singer
//                        progressBar.progress = 0
                        val duration = mSong?.duration
                        if (duration == null || duration == 0) {
                            progressBar.visibility = View.GONE
                        } else {
                            progressBar.visibility = View.VISIBLE
                            progressBar.max = mSong?.duration!!
                        }
                        bottom_player.btn_player.isSelected = true
                        rotationAnim.start()
//                        mCurrentTime = mPlayServiceBinder?.currentTime
                        startProgressBar()
                        if (!s.isOnline) {
                            SongUtil.setLocalCoverImg(
                                this@IMainActivity,
                                s.singer ?: "",
                                bottom_player.player_song_icon
                            )
                        } else {// 在线播放
//                            Log.e("JG", "封面图片url:${s.imgUrl}")
                            GlideUtil.loadImg(
                                this@IMainActivity,
                                s.imgUrl ?: "",
                                bottom_player.player_song_icon,
                                R.drawable.error_icon, R.drawable.error_icon,null
                            )
                        }
                    }
                }
                Constant.SONG_PAUSE -> {
                    Log.e("JG", "---->暂停")
                    bottom_player.btn_player.isSelected = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        rotationAnim.pause()
                    }
                }
                Constant.SONG_RESUME -> {
                    Log.e("JG", "---->播放")
                    bottom_player.btn_player.isSelected = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        rotationAnim.resume()
                    }
                    startProgressBar()
                }
            }
        }
        mViewModel.songUrlResult.observe(this,Observer{
            it?.let {
                val song = it.entries.find {entry->
                    entry.key == "song"
                }?.value as Song
                val url = it.entries.find {entry->
                    entry.key == "url"
                }?.value as String
                song.url = url// 播放地址
                SongUtil.saveSong(song)
                // 开始播放音乐
                mPlayServiceBinder?.playOnline()
            }
        })
    }

    private val rotationAnim by lazy {
        ObjectAnimator.ofFloat(bottom_player.player_song_icon, "rotation", 0.0f, 360.0f).apply {
            duration = 30000
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE// 循环播放
            repeatMode = ValueAnimator.RESTART
        }
    }

    private fun startProgressBar() {
        progressBarHandler.removeMessages(1)
        progressBarHandler.sendEmptyMessageDelayed(1, 1000)
    }

    @SuppressLint("HandlerLeak")
    private val progressBarHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (mPlayServiceBinder != null) {
                if (mPlayServiceBinder!!.isPlaying) {
//                    Log.e("JG", "progress: ${mPlayServiceBinder!!.currentTime.toInt()}")
                    progressBar.progress = mPlayServiceBinder!!.currentTime.toInt() // 显示当前的播放进度（mediaPlayer的currentPosition）
                    startProgressBar()
                }
            }
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
        val song = SongUtil.getSong()
        // 保存歌曲播放时长位置
        song?.currentTime = mPlayServiceBinder?.currentTime ?: 0L
        if (mFlag) {// 暂停的状态
            song?.playStatus = Constant.SONG_PAUSE
        } else {
            song?.playStatus = Constant.SONG_PLAY
        }
        SongUtil.saveSong(song)
        progressBarHandler.removeMessages(1)
        progressBarHandler.removeCallbacksAndMessages(null)
    }
}