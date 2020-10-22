package com.journey.interview.imusic.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.customizeview.swipecaptcha.core.GlideUtil
import com.journey.interview.imusic.listener.AppBarStateChangeListener
import com.journey.interview.imusic.model.LoveSong
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.toIntPx
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import com.journey.interview.weatherapp.base.BaseViewModel
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.imusic_frg_song_list.*
import java.io.IOException
import java.net.URL
import kotlin.concurrent.thread
import kotlin.math.floor

/**
 * @By Journey 2020/10/22
 * @Description
 */
abstract class BaseSongListFragment<VM:BaseViewModel>:BaseLifeCycleFragment<VM>() {
    private lateinit var appBar: AppBarLayout
    private var mSongListType:Int = -1
    private  var minDistance:Int=0
    private var deltaDistance:Int=0
    private var mLastPosition:Int = -1
    private lateinit var ivRectCover:ImageView// 圆角封面图片
    private lateinit var ivWholeAlphaBg:ImageView// 整个的透明度背景图片
    private lateinit var ivWholeCoverBg:ImageView// 整个的不带透明的封面背景图片

    private var alphaAnimator:ObjectAnimator?=null
    private var coverAlphaAnimator:ObjectAnimator?=null

    protected var loveSongs = mutableListOf<LoveSong>()

    protected lateinit var rvContent:RecyclerView

    protected var playBinder: IMusicPlayService.PlayStatusBinder?=null
    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playBinder = service as IMusicPlayService.PlayStatusBinder
        }
    }
    override fun initView() {
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        val playIntent = Intent(requireActivity(),IMusicPlayService::class.java)
        requireActivity().bindService(playIntent,playConnection, Context.BIND_AUTO_CREATE)
        appBar = appbar_layout
        ivRectCover = song_list_riv_album_cover
        ivWholeAlphaBg = song_list_iv_background
//        ivWholeCoverBg = song_list_iv_cover

        rvContent = song_list_rv_content//音乐列表
        rvContent.setHasFixedSize(true)
        minDistance = 85f.toIntPx(context = requireContext())
        deltaDistance = 300f.toIntPx() - minDistance

        mSongListType = getSongListType()
        /* ui*/
//        initAnim()

//        arguments?.let {
//            val albumCoverImgUrl = it.getString("albumImgUrl")
            val albumCoverImgUrl = "https://p1.music.126.net/68u951bt6jm-E4nCyuIuRw==/109951164342901886.jpg"
            if (!albumCoverImgUrl.isNullOrBlank()) {
                // 设置歌单封面图片
                GlideUtil.loadNetImg(requireContext(),albumCoverImgUrl,ivRectCover)

                // 根据歌单封面图片计算出背景图片并设置背景图片
                calculateColors(albumCoverImgUrl)

//                Glide.with(requireActivity())
//                    .load(albumCoverImgUrl)
//                    .apply(RequestOptions.bitmapTransform(BlurTransformation(10,10)))
//                    .into(ivWholeCoverBg)
//                ivWholeCoverBg.alpha = 0f
            }
//        }
        initSongList()
    }

    private fun initSongList() {
                rvContent.setup<Any> {
                    adapter {
                        addItem(R.layout.imusic_item_common_song) {
                            bindViewHolder { data, pos, holder ->
                                when(mSongListType) {
                                    Constant.SONG_LIST_TYPE_LOVE-> {
                                        val s = data as LoveSong
                                        setText(R.id.common_item_tv_song_name,s.name)
                                        setText(R.id.common_item_tv_song_singer,s.singer)
                                        setVisible(R.id.common_item_iv_is_download,s.isDownload==true)
                                        val currentSongId = SongUtil.getSong()?.songId
                                        if (currentSongId != null &&
                                            s.songId == currentSongId) {
                                            setVisible(R.id.common_item_iv_is_playing,true)
                                            mLastPosition = pos
                                        } else {
                                            itemView?.findViewById<ImageView>(R.id.iv_item_song_laba)?.visibility = View.GONE
                                        }
                                        itemClicked(View.OnClickListener {
                                            if (pos != mLastPosition) {
                                                notifyItemChanged(mLastPosition)
                                                mLastPosition = pos
                                            }
                                            notifyItemChanged(pos)
                                            val loveSong = loveSongs[pos]
                                            val song = Song().apply {
                                                songId = loveSong.songId
                                                qqId = loveSong.qqId
                                                songName = loveSong.name
                                                singer = loveSong.singer
                                                isOnline = loveSong.isOnline?:false
                                                url = loveSong.url
                                                imgUrl = loveSong.pic
                                                position = pos
                                                duration = loveSong.duration?:0
                                                listType = Constant.LIST_TYPE_LOVE
                                                mediaId = loveSong.mediaId
                                            }
                                            SongUtil.saveSong(song)
                                            playBinder?.play(Constant.LIST_TYPE_LOVE)
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
    }

    abstract fun getSongListType():Int


    private fun calculateColors(url:String) {
        if (url.isNotEmpty()) {
            thread {
                try {
                    val fileUrl = URL(url)
                    val bitmap:Bitmap?
                    val conn = fileUrl.openConnection()
                    if (conn != null) {
                        conn.doInput = true
                        conn.connect()
                        val input = conn.getInputStream()
                        val opt = BitmapFactory.Options()
                        opt.inSampleSize = 8
                        bitmap = BitmapFactory.decodeStream(input,Rect(),opt)
                        val msg = Message.obtain()
                        msg.what = 0
                        msg.obj = bitmap
                        drawableHandler.sendMessage(msg)
                        input.close()
                    }
                } catch (e:IOException) {
                    Log.e("JG","---calculateColors error:_${e.message}")
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private val drawableHandler = object :Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                val bitmap = msg.obj as Bitmap
                Log.e("JG","bitmap=$bitmap")
//                ivWholeAlphaBg.background = msg.obj as Drawable
//                alphaAnimator?.start()
//                coverAlphaAnimator?.start()
//                Palette.from(bitmap).generate {
//                    it?.let {p->
//                        val vibrant = p.dominantSwatch
//                        // 这样获取的颜色可以进行改变。
//                        val rgb = vibrant?.rgb ?: -15171336
//                        Log.e("JG","rgb=$rgb")
//                        val color = changeColor(rgb)
//                        ivWholeAlphaBg.setBackgroundColor(color)
//                    }
//                }
            }

        }
    }
    private fun initAnim() {
        if (alphaAnimator == null) {
            alphaAnimator = ObjectAnimator.ofFloat(ivWholeAlphaBg,"alpha",0f,0.5f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
            }
        }
        if (coverAlphaAnimator == null) {
            coverAlphaAnimator = ObjectAnimator.ofFloat(ivWholeCoverBg,"alpha",0f,0.5f).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
            }
        }
    }



    override fun onResume() {
        super.onResume()
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener(){
            override fun onOffsetChanged(appBarLayout: AppBarLayout?) {
//                val alphaPercent = ((song_list_ll_content.top -minDistance) / deltaDistance).toFloat()
//                ivRectCover.alpha = alphaPercent
//                song_list_siv_user_avatar.alpha = alphaPercent
//                song_list_tv_user_name.alpha = alphaPercent
            }

            override fun onStateChanged(appBarLayout: AppBarLayout?, state: BarState) {
            }
        })
    }

//    private fun changeColor(rgb:Int):Int {
//        var red = rgb shr 16 and 0xFF
//        var green = rgb shr 8 and 0xFF
//        var blue = rgb and 0xFF
//        red = floor(red.toDouble()).toInt()
//        green = floor(green.toDouble()).toInt()
//        blue = floor(blue.toDouble()).toInt()
//        return Color.rgb(red, green, blue)
//    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unbindService(playConnection)
        if (alphaAnimator != null) {
            if (alphaAnimator?.isRunning==true) {
                alphaAnimator?.cancel()
            }
            alphaAnimator = null
        }
        if (coverAlphaAnimator != null) {
            if (coverAlphaAnimator?.isRunning==true) {
                coverAlphaAnimator?.cancel()
            }
            coverAlphaAnimator = null
        }
    }
}