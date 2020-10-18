package com.journey.interview.imusic.act

import android.animation.*
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
import android.view.animation.LinearInterpolator
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
import com.journey.interview.customizeview.lrcview.LrcView
import com.journey.interview.imusic.global.Bus
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.DownloadSong
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicDownloadService
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.util.ImUtils
import com.journey.interview.imusic.vm.IPlayViewModel
import com.journey.interview.imusic.widget.DiscView
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.StringUtils
import com.journey.interview.utils.getScreenHeight
import com.journey.interview.utils.getString
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_play.*
import kotlinx.android.synthetic.main.imusic_play_bottom_controller.view.*
import kotlinx.android.synthetic.main.imusic_volume_bar.view.*

/**
 * @By Journey 2020/9/28
 * @Description
 * , LrcView.OnPlayClickListener
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class IPlayActivity : BaseLifeCycleActivity<IPlayViewModel>() {
    private var mPlayStatus: Int? = null
    private var mSong: Song? = null
    private lateinit var mDiscView: DiscView
    private lateinit var mSeekBar: SeekBar
    private lateinit var mVolumeSeekBar: SeekBar
    private lateinit var mTvCurrentTime: TextView
    private lateinit var mDiscImageView: ImageView
    private lateinit var mIvPlayMode: ImageView
    private lateinit var mLrcView: LrcView
    private var alphaAnim1:ObjectAnimator?=null
    private var alphaAnim2:ObjectAnimator?=null
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
    private var mPlayMode: Int = Constant.PLAY_ORDER
    private var mListType: Int? = null
    private var hasLrc:Boolean = false
    private var hasDownloaded:Boolean = false

    private val mPlayConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        // 播放
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("JG", "--->onServiceConnected")
            //这里的Binder对象和IMainActivity中的Binder对象是同一个
            mPlayStatusBinder = service as IMusicPlayService.PlayStatusBinder
//            Log.e("JG","IPlayActivity mPlayServiceBinder$mPlayStatusBinder")
            // 播放模式
            mPlayMode = mViewModel.getPlayMode()
            mPlayStatusBinder?.setPlayMode(mPlayMode)

            mIsOnline = SongUtil.getSong()?.isOnline ?: false
//            Log.e("JG", "是否是网络歌曲--->$mIsOnline")
            if (mIsOnline) {// 是网络歌曲
                val coverImg = SongUtil.getSong()?.imgUrl
//                Log.e("JG", "IPlayActivity--->coverImg:$coverImg")
                setSongCoverImg(coverImg)// 设置网络歌曲封面（封面图片路径为网络路径）
                if (mPlayStatus == Constant.SONG_PLAY) {
                    mDiscView.play()
                    play_bottom_controller.iv_play.isSelected = true
                    updateSeekBarProgress()
                }
            } else {// 播放不是网络歌曲
                mSeekBar.secondaryProgress = mSong?.duration?: 0
                setLocalCoverImg(mSong?.singer ?: "imsuic")// 设置本地歌曲封面
            }
//            // todo 本地歌曲的duration不对
//            play_bottom_controller.tv_total_time.text =
//                StringUtils.formatTime(mSong?.duration?.toLong() ?: 0)


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
        ImmersionBar
            .with(this)
            .statusBarView(R.id.top_view)
            .transparentBar()
            .fullScreen(true)
            .init()
    }

    override fun layoutResId() = R.layout.imusic_act_play


    override fun initView() {
        Log.e("JG", "--->initView")
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide()
            window.exitTransition = Slide()
        }
        //当前播放状态
        mPlayStatus = intent?.getIntExtra(Constant.PLAY_STATUS, 2)
        Log.e("JG", "--->mPlayStatus: $mPlayStatus")

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

        mSong = SongUtil.getSong() // 初始化当前播放歌曲

        // 绘制界面ui
        mSong?.let { song ->
            mListType = song.listType// 歌曲类型
            play_tv_song_singer.text = song.singer
            play_tv_song_name.text = song.songName
            Log.e("JG", "当前播放进度：${song.currentTime}")
            Log.e("JG", "当前播放总时长：${mSong?.duration?.toLong()}")//263593
            mTvCurrentTime.text = StringUtils.formatTime(song.currentTime)//当前进度TextView
            play_bottom_controller.tv_total_time.text =
                StringUtils.formatTime(mSong?.duration?.toLong() ?: 0)// 总时长TextView
            mSeekBar.max = song.duration// 进度条总长度（单位 s）
            val time = if (song.currentTime < 1L) {
                0
            } else {
                song.currentTime.toInt()
            }
            mSeekBar.progress = time
            initPlayMode()
        }
        initCoverLrc() // 初始化歌词设置
        initAnim()//初始化动画（唱碟的透明度动画）
    }


    override fun initData() {
        super.initData()
        mSong?.songId?.let {
            mViewModel.queryIsMyLoveSong(it)
        }
        // 如果不是网络歌曲 也要处于播放状态
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
                Log.e("JG","滑动至：${seekBar?.progress ?: 0}")
                if (mLrcView.hasLrc()) {
                    mLrcView.updateTime(progress*1000)
                }
                if (mPlayStatusBinder?.isPlaying == true) {
                    mMediaPlayer = mPlayStatusBinder?.mediaPlayer
                    mMediaPlayer?.seekTo((seekBar?.progress ?: 0) * 1000)
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
                    if (mIsSeek) {// 在暂停的时候拖动了进度条
                        Log.d("JG", "拖动至时间onClick: $mPauseTime")
                        mMediaPlayer?.seekTo((mPauseTime ?: 0) * 1000)
                        mIsSeek = false
                    }
                    mDiscView.play()
                    it?.isSelected = true
                    updateSeekBarProgress()
                }
                else -> {
                    val restartTime = ((mSong?.currentTime ?: 0) * 1000).toInt()
                    if (mIsOnline) {
                        mPlayStatusBinder?.playOnline(restartTime)
//                        mPlayStatusBinder?.resume()
                    } else {
                        mPlayStatusBinder?.play(mListType)
                    }
//                    mMediaPlayer?.seekTo(restartTime)
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

        // *******点击唱碟**********/
        mDiscView.setOnClickListener {
            alphaAnim1?.start()
        }
        alphaAnim1?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (!mIsOnline) {
                    Log.e("JG", "--->查询离线歌词")
                    val lrc = SongUtil.getLrcFromNative(mSong?.songName ?: "imusic")
                    if (lrc == null) {
                        val qqId = mSong?.qqId
                        Log.e("JG", "--->qqId: $qqId")
                        // 匹配不到歌词
                        if (Constant.SONG_ID_UNFIND == qqId) {
                            getLrcError(null)
                        } else if (null == qqId) {// 歌曲id未匹配到
                            mViewModel.getSongId(mSong?.songName ?: "", mSong?.duration ?: -1)
                        } else {// 歌词还未匹配到
                            mViewModel.getOnlineSongLrc(
                                qqId,
                                Constant.SONG_LOCAL,
                                mSong?.songName ?: ""
                            )
                        }
                    } else {
                        switchCoverLrc(false)
                        mLrcView.loadLrc(lrc)
                    }
                } else {
                    if (hasLrc) {
                        switchCoverLrc(false)
                    } else {
                        Log.e("JG", "--->查询在线歌词")
                        val songId = mSong?.songId
                        mLrcView.setLabel("小i努力查找中… ヾ(◍°∇°◍)ﾉﾞ ")
                        switchCoverLrc(false)
                        if (!songId.isNullOrEmpty()) {
                            mViewModel.getOnlineSongLrc(
                                songId,
                                Constant.SONG_ONLINE,
                                mSong?.songName ?: ""
                            )
                        }
                    }
                }
            }
        })

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
                SongUtil.getSong()?.let { mViewModel.deleteFromMyLoveSong(it) }
            } else {
                v.isSelected = true
                val song = SongUtil.getSong()
                song?.let { mViewModel.addToMyLove(song) }
            }
            mIsMyLove = !mIsMyLove
        }
        play_iv_download.setOnClickListener {
            if (mListType==Constant.LIST_TYPE_LOCAL) {
                Toast.makeText(this, R.string.local_no_need_download.getString(), Toast.LENGTH_SHORT).show()
            } else {
                if (hasDownloaded) {
                    Toast.makeText(this, R.string.song_has_download.getString(), Toast.LENGTH_SHORT).show()
                } else {
                    if (mSong?.isDownload == true) {
                        Toast.makeText(this, R.string.song_has_download.getString(), Toast.LENGTH_SHORT).show()
                    } else {
                        mDownloadBinder?.startDownload(getDownloadSong())
                    }
                }
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
            if (it != null) {
                hasLrc = true
//                mLrcView.setLabel("")
                mLrcView.loadLrc(it.lyric)
                if (!mIsOnline) {
                    switchCoverLrc(false)
                }
//                if (mSong?.currentTime != null && mSong?.currentTime!= 0L) {
//                    val currentTime = (mSong?.currentTime?:0) *1000
//                    if (mLrcView.hasLrc()) {
//                        mLrcView.updateTime(currentTime)
//                    }
//                }
            } else {
                hasLrc = false
                mLrcView.setLabel("没有发现歌词o(π﹏π)o")
                Log.e("JG", "查询歌词为null")
            }
        })
        mViewModel.addLoveSongResult.observe(this, Observer {
            it?.let {
                Log.e("JG", "喜欢成功")
                IMusicBus.sendLoveSongChange(true)
            }
        })
        mViewModel.deleteLoveSongResult.observe(this, Observer {
            it?.let {
                Log.e("JG", "刪除喜欢成功")
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
        mViewModel.songIdResult.observe(this, Observer {
            mSong?.qqId = it
            SongUtil.saveSong(mSong)
            mViewModel.getOnlineSongLrc(it,Constant.LIST_TYPE_LOCAL,mSong?.songName?:"")
        })

        mViewModel.localSongImg.observe(this, Observer {
            setSongCoverImg(it)
        })

        mViewModel.localSongId.observe(this, Observer {
            mSong?.qqId = it
            SongUtil.saveSong(mSong)
        })
        Bus.observe<Int>(Constant.DOWNLOAD_RESULT,this) {
            if (it == Constant.TYPE_DOWNLOAD_SUCCESS) {
                hasDownloaded = true
            }
        }
//        IMusicBus.observePlayStatusChange(this,{
//            if (it == Constant.SONG_COMPLETE) {
//                Log.e("JG","--->歌曲播放完毕")
//                mLrcView.updateTime(0)
//            }
//        })
    }

    private fun getLrcError(content:String?) {
//        Log.e("JG", "获取歌词:$content")
        switchCoverLrc(false)
        mLrcView.setLabel(Constant.SONG_ID_UNFIND)
        mSong?.qqId = content
        SongUtil.saveSong(mSong)
    }

    private fun initCoverLrc() {
        // true:支持手动拖动   回调：滚动至某处，并点击播放按钮时回调
        mLrcView.setDraggable(true
        ) { _, time ->
            //滚动至某处并点击
            mMediaPlayer?.seekTo(time.toInt())
            if (mPlayStatusBinder?.isPlaying == false) {
                mPlayStatusBinder?.resume()
                updateSeekBarProgress()
                play_bottom_controller.iv_play.isSelected = true
            }
            true
        }
        // 点击歌词
        mLrcView.setOnTapListener { view, x, y ->
            switchCoverLrc(true)
            alphaAnim2?.start()
        }
        initVolume() // 初始化音量设置
        switchCoverLrc(true)
    }

    private fun initVolume() {
        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mVolumeSeekBar.max = mAudioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
        val currentVolume = mAudioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
//        Log.e("JG", "当前音量：$currentVolume")
        mVolumeSeekBar.progress = currentVolume
    }

    private fun switchPlayMode() {
        var mode = mViewModel.getPlayMode()
        when (mode) {
            Constant.PLAY_ORDER -> {
                mode = Constant.PLAY_RANDOM
                Toast.makeText(this, R.string.random_play.getString(), Toast.LENGTH_SHORT).show()
            }
            Constant.PLAY_RANDOM -> {
                mode = Constant.PLAY_SINGER
                Toast.makeText(this, R.string.single_play.getString(), Toast.LENGTH_SHORT).show()
            }
            Constant.PLAY_SINGER -> {
                mode = Constant.PLAY_ORDER
                Toast.makeText(this, R.string.order_play.getString(), Toast.LENGTH_SHORT).show()
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
            play_ll_function.visibility = View.VISIBLE
        } else {
            ll_lrc.visibility = View.VISIBLE
            mDiscView.visibility = View.GONE
            play_ll_function.visibility = View.GONE
        }
    }

    private fun setSongCoverImg(cover: String?) {
        Glide.with(this)
            .load(cover ?: "")
            .apply(RequestOptions.placeholderOf(R.drawable.default_disc))
            .apply(RequestOptions.errorOf(R.drawable.disc_error))
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    mCoverBitmap = (resource as BitmapDrawable).bitmap
                    setDiscViewCover(mCoverBitmap!!)
                    // 如果是本地音乐
                    if (!mIsOnline) {
                        val result = SongUtil.saveImgToNative(mCoverBitmap, mViewModel.getSingerName() ?: "imusic")
                        Log.e("JG","图片保存至本地：$result")
                        // 将封面地址保存到数据库中  这样在首页播放的时候就能拿到了
                        val pic = "${Constant.STORAGE_IMG_FILE}${SongUtil.getSong()?.singer}.jpg"
                        mViewModel.updateLocalSong(pic,SongUtil.getSong()?.songId?:"")
                    }
                }
            })
    }

    private fun updateSeekBarProgress() {
        stopUpdateSeekBarProgress()
        updateSeekBarHandler.sendEmptyMessageDelayed(0, 300)
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
                val pro = progress?.toInt() ?: 0
//                Log.e("JG", "pro=$pro")
                mSeekBar.progress = pro
                mTvCurrentTime.text = StringUtils.formatTime(mSeekBar.progress.toLong())
                updateSeekBarProgress()
                if (mLrcView.hasLrc()) {
//                    Log.e("JG", "歌词滚动$progress")
                    mLrcView.updateTime(progress!!*1000)
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
        val song = SongUtil.getSong()
        // 记录退出当前页面时歌曲的播放状态（暂停/播放）
        if (mFlag) {// 暂停的状态
            song?.playStatus = Constant.SONG_PAUSE
        } else {
            song?.playStatus = Constant.SONG_PLAY
        }
        SongUtil.saveSong(song)
        hasLrc = false
    }

    private val mVolumeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            mVolumeSeekBar.progress = mAudioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
        }
    }

    private fun setLocalCoverImg(singer: String) {
        // 由于是本地歌曲 获取不到封面图片  所有第一次播放的时候 使用搜索功能搜索出imgUrl，然后将其保存在本地
        val imgUrl = "${Constant.STORAGE_IMG_FILE}${StringUtils.formatSinger(singer)}.jpg"
        Log.e("JG","本地音乐歌手名称--->$singer")
        Log.e("JG","本地音乐图片--->$imgUrl")
        Glide.with(this)
            .load(imgUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 如果加载本地的歌曲封面出错   再次从网络上去搜索资源补上
                    if (mSong?.songName != null) {
                        mViewModel.getLocalSongImg(
                            mViewModel.getSingerName() ?: "",
                            mSong?.songName!!,
                            mSong?.duration ?: 0
                        )
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
            .apply(RequestOptions.placeholderOf(R.drawable.default_disc))
            .apply(RequestOptions.errorOf(R.drawable.disc_error))
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


    private fun initAnim() {
        alphaAnim1 = ObjectAnimator.ofFloat(mDiscView, "alpha", 1.0f, 0.0f)
        alphaAnim1?.duration=240
        alphaAnim1?.interpolator = LinearInterpolator()

        alphaAnim2 = ObjectAnimator.ofFloat(mDiscView, "alpha",  0f, 1.0f)
        alphaAnim2?.duration=240
        alphaAnim2?.interpolator = LinearInterpolator()
    }
}