package com.journey.interview.imusic.act

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.transition.Slide
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.DownloadSong
import com.journey.interview.imusic.model.LocalSong
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicDownloadService
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.util.ImUtils
import com.journey.interview.imusic.vm.IPlayViewModel
import com.journey.interview.imusic.widget.DiscView
import com.journey.interview.utils.FileUtil
import com.journey.interview.utils.StringUtils
import com.journey.interview.utils.getScreenHeight
import com.journey.interview.utils.getString
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_play.*
import kotlinx.android.synthetic.main.imusic_play_bottom_controller.view.*
import kotlinx.android.synthetic.main.imusic_volume_bar.view.*
import me.wcy.lrcview.LrcView

/**
 * @By Journey 2020/9/28
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class IPlayActivity : BaseLifeCycleActivity<IPlayViewModel>(), LrcView.OnPlayClickListener {
    private var mPlayStatus: Int? = null
    private var mSong: Song? = null
    private lateinit var mDiscView: DiscView
    private lateinit var mSeekBar: SeekBar
    private lateinit var mVolumeSeekBar: SeekBar
    private lateinit var mTvCurrentTime: TextView
    private lateinit var mDiscImageView: ImageView
    private lateinit var mIvPlayMode:ImageView
    private lateinit var mLrcView: LrcView
    private lateinit var mLovedSongAnimatorSet: AnimatorSet
    private var mPlayStatusBinder: IMusicPlayService.PlayStatusBinder? = null
    private var mDownloadBinder: IMusicDownloadService.DownloadBinder? = null
    private var mIsDragSeekBar: Boolean = false// 拖动进度条
    private var mIsOnline: Boolean = false// 是否为网络歌曲
    private var mCoverBitmap: Bitmap? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mPauseTime: Int? = null// 记录暂停的时间
    private var mIsSeek: Boolean = false //  标记是否在暂停的时候拖动进度条
    private var mFlag: Boolean = false // 用作暂停的标记
    private var mIsPlaying: Boolean = false
    private var mIsMyLove: Boolean = false
    private var mAudioManager: AudioManager? = null
    private var mPlayMode :Int = Constant.PLAY_ORDER
    private val mPlayConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        // 播放
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlayStatusBinder = service as IMusicPlayService.PlayStatusBinder
            // 播放模式
            mPlayMode = mViewModel.getPlayMode()
            mPlayStatusBinder?.setPlayMode(mPlayMode)

            mIsOnline = FileUtil.getSong()?.isOnline ?: false
            Log.e("JG", "是否是网络歌曲--->$mIsOnline")
            if (mIsOnline) {
                val coverImg = FileUtil.getSong()?.imgUrl
                Log.e("JG", "IPlayActivity--->coverImg:$coverImg")
                getSongCoverImg(coverImg)
                if (mPlayStatus == Constant.SONG_PLAY) {
                    mDiscView.play()
                    play_bottom_controller.iv_play.isSelected = true
                    updateSeekBarProgress()
                }
            } else {
                mSeekBar.secondaryProgress = mSong?.duration?.toInt() ?: 0
                setLocalCoverImg(mSong?.singer ?: "")
            }
            play_bottom_controller.tv_total_time.text =
                StringUtils.formatTime(mSong?.duration?.toLong() ?: 0)
            // 缓存进度条
            mPlayStatusBinder?.mediaPlayer?.setOnBufferingUpdateListener { mp, percent ->
                mSeekBar.secondaryProgress = percent * mSeekBar.progress
            }
            mSong?.songId?.let {
                mViewModel.queryIsMyLoveSong(it)
            }
        }

    }

    // 下载service connection
    private val mDownloadConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {


        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mDownloadBinder = service as IMusicDownloadService.DownloadBinder
        }

    }

    override fun initStatusBar() {
        ImmersionBar.with(this).statusBarView(R.id.top_view).fullScreen(true).init()
    }

    override fun layoutResId() = R.layout.imusic_act_play


    override fun initView() {
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide()
            window.exitTransition = Slide()
        }
        mPlayStatus = intent?.getIntExtra(Constant.PLAY_STATUS, 2)
        mSong = FileUtil.getSong() // 初始化当前播放歌曲

        mDiscView = disc_view as DiscView
        mSeekBar = play_bottom_controller.sb_progress
        mTvCurrentTime = play_bottom_controller.tv_current_time
        mIvPlayMode = play_bottom_controller.iv_mode
        mDiscImageView = mDiscView.findViewById(R.id.iv_disc_background)
        mLrcView = lrc_view
        mVolumeSeekBar = play_top_volume.sb_volume

        // 绑定服务
        val playIntent = Intent(this, IMusicPlayService::class.java)
        bindService(playIntent, mPlayConnection, Context.BIND_AUTO_CREATE)

        val downloadIntent = Intent(this, IMusicDownloadService::class.java)
        bindService(downloadIntent, mDownloadConnection, Context.BIND_AUTO_CREATE)

        // 界面ui
        mSong?.let { song ->
            play_tv_song_singer.text = song.singer
            play_tv_song_name.text = song.songName
            mTvCurrentTime.text = StringUtils.formatTime(song.currentTime)
            mSeekBar.max = song.duration
            mSeekBar.progress = song.currentTime.toInt()
            // todo 下载  喜欢


            initPlayMode()
        }
        initCoverLrc() // 初始化歌词设置
    }


    override fun initData() {
        super.initData()
        mSong?.songId?.let {
            mViewModel.queryIsMyLoveSong(it)
        }
        if (mPlayStatus == Constant.SONG_PLAY) {
            mDiscView.play()
            play_bottom_controller.iv_play.isSelected = true
            updateSeekBarProgress()
        }
        iv_back.setOnClickListener { finish() }
        // 进度条监听
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            // 进度条改变时
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            // 进度条开始拖动时调用
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 防止在拖动进度条进行进度设置时与Thread更新播放进度条冲突
                mIsDragSeekBar = true
            }

            // 进度条停止拖动的时候调用
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress?.toLong() ?: 0
                if (mLrcView.hasLrc()) {
                    mLrcView.updateTime(progress)
                }

                if (mPlayStatusBinder?.isPlaying == true) {
                    mMediaPlayer = mPlayStatusBinder?.mediaPlayer
                    mMediaPlayer?.seekTo(seekBar?.progress ?: 0 * 1000)
                    updateSeekBarProgress()
                } else {
                    mPauseTime = seekBar?.progress
                    mIsSeek = true
                }
                mIsDragSeekBar = false
                mTvCurrentTime.text = StringUtils.formatTime(progress)
            }

        })
        // 播放、暂停
        play_bottom_controller.iv_play.setOnClickListener {
            mMediaPlayer = mPlayStatusBinder?.mediaPlayer
            when {
                mPlayStatusBinder?.isPlaying == true -> {
                    mPlayStatusBinder?.pause()
                    stopUpdateSeekBarProgress()
                    mFlag = true
                    it?.isSelected = false
                    mDiscView.pause()
                }
                mFlag -> {
                    mPlayStatusBinder?.resume()
                    mFlag = false
                    if (mIsSeek) {
                        Log.d("JG", "拖动至时间onClick: $mPauseTime")
                        mMediaPlayer?.seekTo(mPauseTime ?: 0 * 1000)
                        mIsSeek = false
                    }
                    mDiscView.play()
                    it?.isSelected = true
                    updateSeekBarProgress()
                }
                else -> {
                    if (mIsOnline) {
                        mPlayStatusBinder?.playOnline()
                    }
                    mMediaPlayer?.seekTo((mSong?.currentTime ?: 0 * 1000).toInt())
                    mDiscView.play()
                    it?.isSelected = true
                    updateSeekBarProgress()
                }
            }
        }

        // 下一首
        play_bottom_controller.iv_next.setOnClickListener {
            mPlayStatusBinder?.next()
            play_bottom_controller.iv_play.isSelected = mPlayStatusBinder?.isPlaying == true
            mDiscView.next()
        }

        // 上一首
        play_bottom_controller.iv_prev.setOnClickListener {
            mPlayStatusBinder?.last()
            play_bottom_controller.iv_play.isSelected = true
            mDiscView.last()
        }

        // 点击唱碟
        mDiscView.setOnClickListener {
            if (!mIsOnline) {
                val lrc = FileUtil.getLrcFromNative(mSong?.songName ?: "imusic")
                if (lrc == null) {
                    val qqId = mSong?.qqId
                    Log.e("JG", "qqId: $qqId")
                    // 匹配不到歌词
                    if (Constant.SONG_ID_UNFIND == qqId) {
                        getLrcError(null)
                    } else if (null == qqId) {// 歌曲id未匹配到
                        mViewModel.getSongId(mSong?.songName?:"",mSong?.duration?:-1)
                    } else {// 歌词还未匹配到
                        mViewModel.getOnlineSongLrc(mSong?.songId?:"",Constant.SONG_LOCAL,mSong?.songName?:"")
                    }
                } else {
                    switchCoverLrc(false)
                    mLrcView.loadLrc(lrc)
                }
            } else {
                val songId = mSong?.songId
                if (!songId.isNullOrEmpty()) {
                    switchCoverLrc(false)
                    mViewModel.getOnlineSongLrc(songId, Constant.SONG_ONLINE, mSong?.songName ?: "")
                }
            }
        }

        // 音量
        mVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mAudioManager?.setStreamVolume(
                    AudioManager.STREAM_MUSIC, seekBar?.progress ?: 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                )
            }
        })

        mLovedSongAnimatorSet =
            AnimatorInflater.loadAnimator(this, R.animator.favorites_anim) as AnimatorSet
        mLovedSongAnimatorSet.setTarget(play_btn_love)
        // 添加至我的喜欢
        play_btn_love.setOnClickListener { v ->
            mLovedSongAnimatorSet.start()
            if (mIsMyLove) {
                v?.isSelected = false
                FileUtil.getSong()?.let { mViewModel.deleteFromMyLoveSong(it) }
            } else {
                v.isSelected = true
                val song = FileUtil.getSong()
                song?.let { mViewModel.addToMyLove(song) }
            }
            mIsMyLove = !mIsMyLove
        }
        play_iv_download.setOnClickListener {
            if (mSong?.isDownload == true) {
                Toast.makeText(this, "歌曲已经下载", Toast.LENGTH_SHORT).show()
            } else {
                mDownloadBinder?.startDownload(getDownloadSong())
            }
        }
        mIvPlayMode.setOnClickListener {
            switchPlayMode()
        }

    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
        registerReceiver(mVolumeReceiver, filter)
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.songLrc.observe(this, Observer {
            it?.let { songLrc ->
                val lrc = songLrc.lyric
                Log.d("JG", "歌词lyric: $lrc")
                mLrcView.loadLrc(lrc)
            }
        })
        mViewModel.addLoveSongResult.observe(this, Observer {
            it?.let {
                Log.d("JG", "喜欢成功")
                IMusicBus.sendLoveSongChange(true)
            }
        })
        mViewModel.deleteLoveSongResult.observe(this, Observer {
            it?.let {
                Log.d("JG", "刪除喜欢成功")
                IMusicBus.sendLoveSongChange(true)
            }
        })
        mViewModel.queryIsMyLoveResult.observe(this, Observer {
            it?.let { b ->
                play_btn_love.isSelected = b
            }
        })
        mViewModel.lrcError.observe(this, Observer {
            getLrcError(it)
        })
        mViewModel.songIdResult.observe(this,Observer{

        })

        mViewModel.localSongImg.observe(this,Observer {
            getSongCoverImg(it)
        })

        mViewModel.localSongId.observe(this, Observer{
            mSong?.qqId = it
            FileUtil.saveSong(mSong)
        })
    }

    private fun getLrcError(content:String?) {
        Log.d("JG", "获取歌词:$content")
        mSong?.qqId = content
        FileUtil.saveSong(mSong)
    }

    private fun initCoverLrc() {
        mLrcView.setDraggable(true, this)
        // 点击歌词
        mLrcView.setOnTapListener { view, x, y ->
            switchCoverLrc(true)
        }
        initVolume() // 初始化音量设置
        switchCoverLrc(true)
    }

    private fun initVolume() {
        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mVolumeSeekBar.max = mAudioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
        val currentVolume = mAudioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
        Log.d("JG", "当前音量：$currentVolume")
        mVolumeSeekBar.progress = currentVolume
    }

    private fun switchPlayMode() {
        var mode = mViewModel.getPlayMode()
        when(mode) {
            Constant.PLAY_ORDER->{
                mode = Constant.PLAY_RANDOM
                Toast.makeText(this,R.string.random_play.getString(),Toast.LENGTH_SHORT).show()
            }
            Constant.PLAY_RANDOM->{
                mode = Constant.PLAY_SINGER
                Toast.makeText(this,R.string.single_play.getString(),Toast.LENGTH_SHORT).show()
            }
            Constant.PLAY_SINGER->{
                mode = Constant.PLAY_ORDER
                Toast.makeText(this,R.string.order_play.getString(),Toast.LENGTH_SHORT).show()
            }
        }
        mViewModel.setPlayMode(mode)
        initPlayMode()
    }

    private fun initPlayMode() {
        mPlayMode = mViewModel.getPlayMode()
        mPlayStatusBinder?.setPlayMode(mPlayMode)
        mIvPlayMode.setImageLevel(mPlayMode)
    }


    // 是否显示封面/歌词
    private fun switchCoverLrc(showCover: Boolean) {
        if (showCover) {
            ll_lrc.visibility = View.GONE
            mDiscView.visibility = View.VISIBLE
        } else {
            ll_lrc.visibility = View.VISIBLE
            mDiscView.visibility = View.GONE
        }
    }

    private fun getSongCoverImg(cover: String?) {
        Glide.with(this)
            .load(cover ?: "")
            .apply(RequestOptions.placeholderOf(R.drawable.icon1))
            .apply(RequestOptions.errorOf(R.drawable.icon2))
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    mCoverBitmap = (resource as BitmapDrawable).bitmap
                    // 如果是本地音乐
                    if (!mIsOnline) {
                        // 保存图片到本地
                        FileUtil.saveImgToNative(mCoverBitmap, mViewModel.getSingerName() ?: "")
                        // 将封面地址保存到数据库中
                        val localSong = LocalSong()
                        localSong.pic =
                            "${Constant.STORAGE_IMG_FILE}${FileUtil.getSong()?.singer}.jpg"
                        mViewModel.updateLocalSong(localSong)
                    }
                    setDiscViewCover(mCoverBitmap!!)
                }
            })
    }

    private fun updateSeekBarProgress() {
        stopUpdateSeekBarProgress()
        updateSeekBarHandler.sendEmptyMessageDelayed(0, 1000)
    }

    private fun stopUpdateSeekBarProgress() {
        updateSeekBarHandler.removeMessages(0)
    }

    private fun setDiscViewCover(bitmap: Bitmap) {
        mDiscImageView.setImageDrawable(mDiscView.getDiscDrawable(bitmap))
        val marginTop = (ImUtils.SCALE_DISC_MARGIN_TOP * getScreenHeight()).toInt()
        val lp = mDiscImageView.layoutParams as RelativeLayout.LayoutParams
        lp.setMargins(0, marginTop, 0, 0)
        mDiscImageView.layoutParams = lp
    }

//    private fun setCoverBackground(bitmap: Bitmap?) {
//        bitmap?.let {
//            thread {
//                val widthHeightSize = (getScreenWidth() * 1.0 / getScreenHeight() * 1.0).toFloat()
//                val cropBitmapWidth = (widthHeightSize * it.height).toInt()
//                val cropBitmapWidthX = (it.width - cropBitmapWidth/2.0).toInt()
//                /*切割部分图片*/
//                val cropBitmap = Bitmap.createBitmap(it,cropBitmapWidthX,0,cropBitmapWidth,it.height)
//                if (cropBitmap != null) {
//                    val scaleBitmap = Bitmap.createScaledBitmap(cropBitmap,it.width/50,it.height/50,false)
//                    val blurBitmap = FastBlurUtil.doBlur(scaleBitmap,8,true)
//                    val foregroundDrawable = BitmapDrawable(blurBitmap)
//                    foregroundDrawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY)
//                }
//
//            }.start()
//        }
//    }

    @SuppressLint("HandlerLeak")
    private val updateSeekBarHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (!mIsDragSeekBar) {
                val progress = mPlayStatusBinder?.currentTime
//                Log.e("JG", "播放进度progress=$progress")
                val pro = progress?.toInt() ?: 0
                mSeekBar.progress = pro
                mTvCurrentTime.text = StringUtils.formatTime(mSeekBar.progress.toLong())
                updateSeekBarProgress()
//                Log.e("JG", "歌词是否有效${lrc_view.hasLrc()}")
                if (mLrcView.hasLrc()) {
                    mLrcView.updateTime(progress ?: 0L)
                }
            }
        }
    }

    private fun getDownloadSong(): DownloadSong {
        return DownloadSong().apply {
            singer = mSong?.singer
            progress = 0
            songId = mSong?.songId
            url = mSong?.url
            songName = mSong?.songName
            duration = mSong?.duration
            albumName = mSong?.albumName
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(mPlayConnection)
        unbindService(mDownloadConnection)
        stopUpdateSeekBarProgress()
        updateSeekBarHandler.removeCallbacksAndMessages(null)
        unregisterReceiver(mVolumeReceiver)
    }

    private val mVolumeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            mVolumeSeekBar.progress = mAudioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
        }
    }

    private fun setLocalCoverImg(singer: String) {
        val imgUrl = "${Constant.STORAGE_IMG_FILE}${StringUtils.formatSinger(singer)}.jpg"
        Glide.with(this)
            .load(imgUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (mSong?.songName != null) {
                        mViewModel.getLocalSongImg(mViewModel.getSingerName()?:"",mSong?.songName!!,mSong?.duration?:0)
                    }
                    setDiscViewCover(
                        BitmapFactory.decodeResource(
                            resources,
                            R.drawable.default_disc
                        )
                    )
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .apply(RequestOptions.placeholderOf(R.drawable.icon1))
            .apply(RequestOptions.errorOf(R.drawable.icon2))
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    mCoverBitmap = (resource as BitmapDrawable).bitmap
                    setDiscViewCover(mCoverBitmap!!)
                }

            })
    }

    /**
     * 歌词播放按钮点击监听器，点击后应该跳转到指定播放位置
     */
    override fun onPlayClick(view: LrcView?, time: Long): Boolean {
        if (mPlayStatusBinder?.isPlaying == true || mPlayStatusBinder?.isPausing == true) {
            mMediaPlayer?.seekTo(time.toInt())
            if (mPlayStatusBinder?.isPausing == true) {
                // todo
            }
            return true
        }
        return false
    }
}