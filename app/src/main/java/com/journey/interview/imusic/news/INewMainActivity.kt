package com.journey.interview.imusic.news

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.customizeview.progressbar.DigistProgressBar
import com.journey.interview.customizeview.ripple.MyRippleView
import com.journey.interview.customizeview.swipecaptcha.core.GlideUtil
import com.journey.interview.imusic.act.IPlayActivity
import com.journey.interview.imusic.download.IMusicDownloadUtil
import com.journey.interview.imusic.frg.IMainFragment
import com.journey.interview.imusic.global.Bus
import com.journey.interview.imusic.listener.OnFragmentEventListener
import com.journey.interview.imusic.model.ListBean
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.util.AssetsUtil
import com.journey.interview.imusic.util.NavigationManager
import com.journey.interview.imusic.vm.IMainViewModel
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.StringUtils
import com.journey.interview.utils.isServiceRunning
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_main_b.*
import kotlinx.android.synthetic.main.imusic_include_main.view.*

/**
 * @By Journey 2020/9/25
 * @Description
 */
class INewMainActivity : BaseLifeCycleActivity<IMainViewModel>(),
    NavigationView.OnNavigationItemSelectedListener,
    OnFragmentEventListener {
    // 当前播放的歌曲
    private var mSong: Song? = null

    // 初次使用 推荐的音乐
    private var mFirstSong: Song? = null

    // 判断当前是否有存活服务
    private var isExistService: Boolean = false

    // 用作暂停的标记
    private var mFlag: Boolean = false

    // 当前播放的时间进度
    private var mCurrentTime: Long? = null
    private var isGoPlay: Boolean = false

    private var navigationManager: NavigationManager? = null

    /* 页面组件*/
    private lateinit var bottomPlayView: ConstraintLayout
    private lateinit var bottomSongProgressBar: DigistProgressBar
    private lateinit var bottomSongCover: ShapeableImageView
    private lateinit var bottomSongName: TextView
    private lateinit var bottomSongSinger: TextView
    private lateinit var bottomPlaySongRippleView: MyRippleView
    private lateinit var bottomPlaySongBtn: ImageView
    private lateinit var bottomPlayList: ImageView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mNavigationViewHeader: View

    private var mMediaPlayer: MediaPlayer? = null
    private var mPlayServiceBinder: IMusicPlayService.PlayStatusBinder? = null

    override fun layoutResId() = R.layout.imusic_act_main_b

    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlayServiceBinder = service as IMusicPlayService.PlayStatusBinder
            if (isExistService) {
                startProgressBar()
            }
            onServiceBind()
        }
    }

    private fun onServiceBind() {
        mNavigationViewHeader = LayoutInflater.from(this)
            .inflate(R.layout.imusic_navigation_header, navigationView, false)
        navigationView.addHeaderView(mNavigationViewHeader)
        navigationView.setNavigationItemSelectedListener(this)
        if (navigationManager == null) {
            navigationManager = NavigationManager(this)
        }
    }

    override fun initView() {
        super.initView()
        initWidget()
        // 设置圆形图片
        bottomSongCover.shapeAppearanceModel =
            ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
        mSong = SongUtil.getSong()
        if (mSong != null) {
            setSongUi(mSong!!)
        } else {
            setRecommendSong()// 查询推荐音乐
        }
        // 保證播放音乐状态退出程序后 再次进入 仍然继续播放音乐（或者暂停状态退出程序 进入后仍然保持之前的状态）
        if (isServiceRunning(IMusicPlayService::class.java.name)) {
            val playStatus = mSong?.playStatus
            if (playStatus != null && playStatus == Constant.SONG_PLAY) {//如果当前是处于播放的状态
                bottomPlaySongBtn.isSelected = true
                rotationAnim.start()
                isExistService = true
            }
        }
        // 处理服务
        initService()
    }

    private fun setRecommendSong() {
        // 使用json文件中推荐的歌曲
//        val firstSong = AssetsUtil.readAssetsJson("first_song.json")
//        firstSong?.let {
//            val s = Gson().fromJson(it, ListBean::class.java)
//            val song = Song().apply {
//                songId = s.songmid //004DrG5A2nm7q2
//                singer = StringUtils.getSinger(s)// 鸾音社
//                songName = s.songname// 夜来寒雨晓来风
//                //http://y.gtimg.cn/music/photo_new/T002R180x180M000004UvnL62KXhCQ.jpg
//                imgUrl = "${Constant.ALBUM_PIC}${s.albummid}${Constant.JPG}"
//                duration = s.interval//187  (秒)
//                isOnline = true
//                mediaId = s.strMediaMid//004DrG5A2nm7q2
//                albumName = s.albumname//夜来寒雨晓来风
//                // 是否已经下载过了（初次搜索为false）
//                isDownload =
//                    IMusicDownloadUtil.isExistOfDownloadSong(s.songmid ?: "")//003IHI2x3RbXLS
//            }
//            mFirstSong = song
//            setSongUi(song)
//        }
        mViewModel.findAllRecomSongs()
    }

    private fun setSongUi(song: Song) {
        bottomSongName.text = song.songName
        bottomSongSinger.text = song.singer
        mCurrentTime = song.currentTime//
        bottomSongProgressBar.max = song.duration ?: 100
        bottomSongProgressBar.progress = song.currentTime.toInt() ?: 0
        Log.e("JG", "播放进度：$mCurrentTime")
        if (song.imgUrl == null) {// 本地音乐
            SongUtil.setLocalCoverImg(
                this@INewMainActivity,
                song.singer ?: "",
                bottomSongCover
            )
        } else {// 网络音乐
            GlideUtil.loadImg(
                this@INewMainActivity,
                song.imgUrl ?: "",
                bottomSongCover,
                R.drawable.default_disc, R.drawable.default_disc, null
            )
        }
    }

    override fun initData() {
        super.initData()
        mMediaPlayer = mPlayServiceBinder?.mediaPlayer
        /* 点击播放/点击暂停*/
        bottomPlaySongBtn.setOnClickListener {
            when {
                mPlayServiceBinder?.isPlaying == true -> {
                    mPlayServiceBinder?.pause()
                    mFlag = true
                }
                mFlag -> {
                    mPlayServiceBinder?.resume()
                    mFlag = false
                }
                else -> {//退出程序重新打开后的情况（此时处于暂停）
                    Log.e("JG", "上一次的播放进度:$mCurrentTime")
                    val song = SongUtil.getSong()
                    if (song != null) {
                        if (song.isOnline) {
                            // todo
                            mPlayServiceBinder?.playOnline(((mCurrentTime ?: 0) * 1000).toInt())
//                        mPlayServiceBinder?.resume()
                        } else {
                            if (mCurrentTime != null && mCurrentTime != 0L) {
                                mPlayServiceBinder?.play(
                                    SongUtil.getSong()?.listType,
                                    (mCurrentTime!! * 1000).toInt()
                                )
                            } else {
                                mPlayServiceBinder?.play(SongUtil.getSong()?.listType)
                            }
                        }
                    } else {
                        isGoPlay = false
                        mViewModel.getSongUrl(mFirstSong!!)
                    }
                    // todo 这里有个bug  playOnline后会reset mediaPlayer对象  seekTo方法应该是不生效的
//                    mMediaPlayer = mPlayServiceBinder?.mediaPlayer// 这个mediaPlayer对象需要重新获取  之前的由于程序退出后已经被销毁
//                    val currentTime = mCurrentTime!! * 1000
//                    mMediaPlayer?.seekTo(currentTime.toInt())
                }
            }
        }
        bottomPlayView.setOnClickListener {
            val song = SongUtil.getSong()
            val playIntent = Intent(this, IPlayActivity::class.java)
            if (song != null) {
                if (song.songName != null) {
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
                        Log.e("JG", "暂停播放进度：${bottomSongProgressBar.progress.toLong()}")
                        song3?.currentTime = bottomSongProgressBar.progress.toLong()
                        playIntent.putExtra(Constant.PLAY_STATUS, Constant.SONG_PAUSE)
                        SongUtil.saveSong(song3)
                    }
                    if (SongUtil.getSong()?.imgUrl != null) {
                        playIntent.putExtra("online", true)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(
                            playIntent,
                            ActivityOptions.makeSceneTransitionAnimation(this@INewMainActivity)
                                .toBundle()
                        )
                    } else {
                        startActivity(playIntent)
                    }
                }
            } else {
                isGoPlay = true
                mViewModel.getSongUrl(mFirstSong!!)
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
        Bus.observe<Int>(Constant.SONG_STATUS_CHANGE, this) {
//            Log.e("JG","--->坚挺到播放歌曲的改变")
            when (it) {
                Constant.SONG_CHANGE -> {
                    Log.e("JG", "---->SONG_CHANGE")
                    mSong = SongUtil.getSong()
                    mSong?.let { s ->
                        bottomSongName.text = s.songName
                        bottomSongSinger.text = s.singer
                        val duration = mSong?.duration
                        if (duration == null || duration == 0) {
                            bottomSongProgressBar.visibility = View.GONE
                        } else {
                            bottomSongProgressBar.visibility = View.VISIBLE
                            bottomSongProgressBar.max = mSong?.duration!!
                        }
                        bottomPlaySongBtn.isSelected = true
                        rotationAnim.start()
//                        mCurrentTime = mPlayServiceBinder?.currentTime
                        startProgressBar()
                        if (!s.isOnline) {// 本地音乐
                            SongUtil.setLocalCoverImg(
                                this@INewMainActivity,
                                s.singer ?: "",
                                bottomSongCover
                            )
                        } else {// 在线播放
//                            Log.e("JG", "封面图片url:${s.imgUrl}")
                            GlideUtil.loadImg(
                                this@INewMainActivity,
                                s.imgUrl ?: "",
                                bottomSongCover,
                                R.drawable.default_disc, R.drawable.default_disc, null
                            )
                        }
                    }
                }
                Constant.SONG_PAUSE -> {
                    Log.e("JG", "---->暂停")
                    bottomPlaySongBtn.isSelected = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        rotationAnim.pause()
                    }
                }
                Constant.SONG_RESUME -> {
                    Log.e("JG", "---->播放")
                    bottomPlaySongBtn.isSelected = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        rotationAnim.resume()
                    }
                    startProgressBar()
                }
            }
        }
        mViewModel.songUrlResult.observe(this, Observer {
            it?.let {
                val song = it.entries.find { entry ->
                    entry.key == "song"
                }?.value as Song
                val url = it.entries.find { entry ->
                    entry.key == "url"
                }?.value as String
                song.url = url// 播放地址
                SongUtil.saveSong(song)
                if (isGoPlay) {
                    val playIntent = Intent(this, IPlayActivity::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(
                            playIntent,
                            ActivityOptions.makeSceneTransitionAnimation(this@INewMainActivity)
                                .toBundle()
                        )
                    } else {
                        startActivity(playIntent)
                    }
                } else {
                    // 开始播放音乐
                    mPlayServiceBinder?.playOnline()
                }

            }
        })
        mViewModel.recomSongs.observe(this,Observer{
            it?.let {
                if (it.isNotEmpty()) {
                    val firstRecomSong = it[0]
                    Log.e("JG","--->firstRecomSong$firstRecomSong")
                    val song = Song().apply {
                        songId = firstRecomSong.songId //004DrG5A2nm7q2
                        singer = firstRecomSong.singer// 鸾音社
                        songName = firstRecomSong.songName// 夜来寒雨晓来风
                        imgUrl = firstRecomSong.imgUrl
                        duration = firstRecomSong.duration?:0//187  (秒)
                        isOnline = firstRecomSong.isOnline?:false
                        mediaId = firstRecomSong.mediaId//004DrG5A2nm7q2
                        albumName = firstRecomSong.albumName//夜来寒雨晓来风
                        isDownload =firstRecomSong.isDownload?:false
                    }
                    mFirstSong = song
                    setSongUi(song)
                }
            }
        })
    }

    private val rotationAnim by lazy {
        ObjectAnimator.ofFloat(bottomSongCover, "rotation", 0.0f, 360.0f).apply {
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
                    bottomSongProgressBar.progress =
                        mPlayServiceBinder!!.currentTime.toInt() // 显示当前的播放进度（mediaPlayer的currentPosition）
                    startProgressBar()
                }
            }
        }
    }

    private fun initWidget() {
        bottomPlayView = include_main.include_bottom_play_view
        bottomSongProgressBar = include_main.include_play_progress_bar
        bottomSongCover = include_main.include_player_song_icon
        bottomSongName = include_main.include_player_song_name
        bottomSongSinger = include_main.include_player_song_author
        bottomPlaySongRippleView = include_main.include_main_rv_play
        bottomPlaySongBtn = include_main.include_btn_player
        bottomPlayList = include_main.include_player_song_list
        navigationView = navigation_view
        drawerLayout = drawer_layout
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        handler?.postDelayed({
            item.isChecked = false
        }, 500)
        return navigationManager?.onNavigationItemSelected(item) ?: false
    }

    override fun sendEvent(view: View?, eventType: Int) {
        Log.e("JG", "--->sendEvent$eventType")
        when(eventType) {
            Constant.EVENT_OPEN_DRAWER->drawerLayout.openDrawer(GravityCompat.START)
            Constant.EVENT_REFRESH_HOME_DATA->{

            }
        }

    }
}