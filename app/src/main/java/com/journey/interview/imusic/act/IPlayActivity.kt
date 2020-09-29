package com.journey.interview.imusic.act

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.transition.Slide
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.util.ImUtils
import com.journey.interview.imusic.vm.IPlayViewModel
import com.journey.interview.imusic.widget.DiscView
import com.journey.interview.utils.FileUtil
import com.journey.interview.utils.StringUtils
import com.journey.interview.utils.getScreenHeight
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_play.*
import kotlinx.android.synthetic.main.imusic_play_bottom_controller.view.*
import me.wcy.lrcview.LrcView

/**
 * @By Journey 2020/9/28
 * @Description
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class IPlayActivity:BaseLifeCycleActivity<IPlayViewModel>() ,LrcView.OnPlayClickListener{
    private var mPlayStatus:Int?=null
    private var mSong:Song?=null
    private lateinit var mDiscView:DiscView
    private lateinit var mSeekBar:SeekBar
    private lateinit var mTvCurrentTime:TextView
    private lateinit var mDiscImageView:ImageView
    private lateinit var mLrcView: LrcView
    private var mPlayStatusBinder:IMusicPlayService.PlayStatusBinder?=null
    private var mIsDragSeekBar:Boolean = false// 拖动进度条
    private var mIsOnline:Boolean = false// 是否为网络歌曲
    private var mCoverBitmap:Bitmap?=null
    private var mMediaPlayer:MediaPlayer?=null
    private var mPauseTime:Int?=null// 记录暂停的时间
    private var mIsSeek:Boolean = false //  标记是否在暂停的时候拖动进度条
    private var mFlag:Boolean = false // 用作暂停的标记
    private var mIsPlaying:Boolean = false
    private val mPlayConnection =object :ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }
        // 播放
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlayStatusBinder = service as IMusicPlayService.PlayStatusBinder
            mIsOnline = FileUtil.getSong()?.isOnline?:false
            Log.e("JG","是否是网络歌曲--->$mIsOnline")
            if (mIsOnline) {
                val coverImg = FileUtil.getSong()?.imgUrl
                Log.e("JG","IPlayActivity--->coverImg:$coverImg")
                getSongCoverImg(coverImg)
                if (mPlayStatus == Constant.SONG_PLAY) {
                    mDiscView.play()
                    play_bottom_controller.iv_play.isSelected = true
                    updateSeekBarProgress()
                }
            } else {

            }
            play_bottom_controller.tv_total_time.text = StringUtils.formatTime(mSong?.duration?.toLong()?:0)
            // 缓存进度条
            mPlayStatusBinder?.mediaPlayer?.setOnBufferingUpdateListener { mp, percent ->
                mSeekBar.secondaryProgress = percent * mSeekBar.progress
            }
        }

    }

    override fun initStatusBar() {
//        ImmersionBar.with(this).statusBarColor(R.color.translate).init()
//        hideStatusBar(true)
        hide()
    }
    override fun layoutResId()= R.layout.imusic_act_play


    override fun initView() {
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide()
            window.exitTransition = Slide()
        }
        mPlayStatus = intent?.getIntExtra(Constant.PLAY_STATUS,2)
        mSong = FileUtil.getSong() // 初始化当前播放歌曲

        mDiscView= disc_view as DiscView
        mSeekBar = play_bottom_controller.sb_progress
        mTvCurrentTime = play_bottom_controller.tv_current_time
        mDiscImageView = mDiscView.findViewById(R.id.iv_disc_background)
        mLrcView = lrc_view

        // 绑定服务
        val playIntent = Intent(this,IMusicPlayService::class.java)
        bindService(playIntent,mPlayConnection,Context.BIND_AUTO_CREATE)

        // 界面ui
        mSong?.let {song->
            play_tv_song_singer.text = song.singer
            play_tv_song_name.text = song.songName
            mTvCurrentTime.text = StringUtils.formatTime(song.currentTime)
            mSeekBar.max = song.duration
            mSeekBar.progress = song.currentTime.toInt()
            // todo 下载  喜欢

        }
        initCoverLrc()
    }


    override fun initData() {
        super.initData()
        if (mPlayStatus == Constant.SONG_PLAY) {
            mDiscView.play()
            play_bottom_controller.iv_play.isSelected = true
            updateSeekBarProgress()
        }
        iv_back.setOnClickListener { finish() }
        // 进度条监听
        mSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 防止在拖动进度条进行进度设置时与Thread更新播放进度条冲突
                mIsDragSeekBar = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress?.toLong()?:0
                if (mLrcView.hasLrc()) {
                    mLrcView.updateTime(progress)
                }

                if (mPlayStatusBinder?.isPlaying == true) {
                    mMediaPlayer = mPlayStatusBinder?.mediaPlayer
                    mMediaPlayer?.seekTo(seekBar?.progress?:0 * 1000)
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
                        Log.d("JG","拖动至时间onClick: $mPauseTime")
                        mMediaPlayer?.seekTo(mPauseTime?:0 * 1000)
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
                    mMediaPlayer?.seekTo((mSong?.currentTime?:0 * 1000).toInt())
                    mDiscView.play()
                    it?.isSelected = true
                    updateSeekBarProgress()
                }
            }
        }

        // 下一首

        // 上一首


        // 点击唱碟
        mDiscView.setOnClickListener {
            if (!mIsOnline) {

            } else {
                val songId = mSong?.songId
                if (!songId.isNullOrEmpty()) {
                    switchCoverLrc(false)
                    mViewModel.getOnlineSongLrc(songId,Constant.SONG_ONLINE)
                }
            }
        }

    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.songLrc.observe(this,Observer{
            it?.let {songLrc->
                val lrc= songLrc.lyric
                Log.d("JG","歌词lyric: $lrc")
                mLrcView.loadLrc(lrc)
            }
        })
    }

    private fun initCoverLrc() {
        mLrcView.setDraggable(true,this)
        mLrcView.setOnTapListener { view, x, y ->
            switchCoverLrc(true)
        }
        switchCoverLrc(true)
    }

    // 是否显示封面/歌词
    private fun switchCoverLrc(showCover:Boolean) {
        if (showCover) {
            ll_lrc.visibility = View.GONE
            mDiscView.visibility = View.VISIBLE
        } else {
            ll_lrc.visibility = View.VISIBLE
            mDiscView.visibility = View.GONE

        }
    }

    private fun getSongCoverImg(cover:String?) {
        Glide.with(this)
            .load(cover?:"")
            .apply(RequestOptions.placeholderOf(R.drawable.icon1))
            .apply(RequestOptions.errorOf(R.drawable.icon2))
            .into(object :SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    mCoverBitmap = (resource as BitmapDrawable).bitmap
                    // 如果是本地音乐
                    if (!mIsOnline) {

                    }
                    setDiscViewCover()

                }

            })
    }

    private fun updateSeekBarProgress() {
        stopUpdateSeekBarProgress()
        updateSeekBarHandler.sendEmptyMessageDelayed(0,1000)
    }

    private fun stopUpdateSeekBarProgress() {
        updateSeekBarHandler.removeMessages(0)
    }

    private fun setDiscViewCover() {
        mDiscImageView.setImageDrawable(mDiscView.getDiscDrawable(mCoverBitmap))
        val marginTop = (ImUtils.SCALE_DISC_MARGIN_TOP * getScreenHeight()).toInt()
        val lp = mDiscImageView.layoutParams as RelativeLayout.LayoutParams
        lp.setMargins(0,marginTop,0,0)
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

    private val  updateSeekBarHandler = object :Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (!mIsDragSeekBar) {
                val progress = mPlayStatusBinder?.currentTime
                Log.e("JG","播放进度progress=$progress")
                val pro = progress?.toInt()?:0
                mSeekBar.progress = pro
                mTvCurrentTime.text = StringUtils.formatTime(mSeekBar.progress.toLong())
                updateSeekBarProgress()
                Log.e("JG","歌词是否有效${lrc_view.hasLrc()}")
                if (mLrcView.hasLrc()) {
                    mLrcView.updateTime(progress?:0L)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(mPlayConnection)
        stopUpdateSeekBarProgress()
        updateSeekBarHandler.removeCallbacksAndMessages(null)
    }

    private fun hide() {
        val decorView: View = window.decorView
            if (Build.VERSION.SDK_INT >= 22) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.statusBarColor = Color.TRANSPARENT
            }
    }

    override fun onPlayClick(view: LrcView?, time: Long): Boolean {

        return false
    }
}