package com.journey.interview.imusic.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.ShapeAppearanceModel
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.model.LoveSong
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.util.ImUtils
import com.journey.interview.imusic.widget.AnimBgLayout
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.FastBlurUtil
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.StatusBarUtil
import com.journey.interview.utils.toIntPx
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.android.synthetic.main.imusic_frg_song_list.*
import java.io.IOException
import java.net.URL
import kotlin.concurrent.thread

/**
 * @By Journey 2020/10/22
 * @Description
 */
abstract class BaseSongListFragment<VM : BaseViewModel> : BaseLifeCycleFragment<VM>() {
    private lateinit var appBar: AppBarLayout
    private var mSongListType: Int = -1
//    private var minDistance: Int = 0
//    private var deltaDistance: Int = 0
    private var mLastPosition: Int = -1
    private lateinit var mRootLayout: AnimBgLayout
    private lateinit var ivRectCover: ImageView// 圆角封面图片

    //    private lateinit var ivWholeAlphaBg: ImageView// 整个的透明度背景图片
    private lateinit var ivWholeCoverBg: ImageView// 整个的不带透明的封面背景图片
    private lateinit var toolBar: Toolbar

    private var alphaAnimator: ObjectAnimator? = null
    private var coverAlphaAnimator: ObjectAnimator? = null

    protected var loveSongs = mutableListOf<LoveSong>()

    protected lateinit var rvContent: RecyclerView

    protected var playBinder: IMusicPlayService.PlayStatusBinder? = null
    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playBinder = service as IMusicPlayService.PlayStatusBinder
        }
    }

    override fun initView() {
        super.initView()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }
//        StatusBarUtil.setStautsBarColor(requireActivity(),Color.RED,125)
        val playIntent = Intent(requireActivity(), IMusicPlayService::class.java)
        requireActivity().bindService(playIntent, playConnection, Context.BIND_AUTO_CREATE)
        appBar = appbar_layout
        toolBar = song_list_toolbar
        ivRectCover = song_list_riv_album_cover
        mRootLayout = song_list_root_view
//        ivWholeAlphaBg = song_list_iv_background
//        ivWholeCoverBg = song_list_iv_cover

        rvContent = song_list_rv_content//音乐列表
        rvContent.setHasFixedSize(true)
//        minDistance = 85f.toIntPx(context = requireContext())
//        deltaDistance = 300f.toIntPx() - minDistance

        mSongListType = getSongListType()
        initToolbarAlpha()
        /* ui*/
//        initAnim()
        song_list_siv_user_avatar.shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setAllCornerSizes(ShapeAppearanceModel.PILL).build()
//        arguments?.let {
//            val albumCoverImgUrl = it.getString("albumImgUrl")
        val albumCoverImgUrl =
            "https://p1.music.126.net/68u951bt6jm-E4nCyuIuRw==/109951164342901886.jpg"
        if (!albumCoverImgUrl.isNullOrBlank()) {
            // 设置歌单封面图片
//            GlideUtil.loadNetImg(requireContext(), albumCoverImgUrl, ivRectCover)

            // 根据歌单封面图片计算出背景图片并设置背景图片
//                calculateColors(albumCoverImgUrl)

//                Glide.with(requireActivity())
//                    .load(albumCoverImgUrl)
//                    .apply(RequestOptions.bitmapTransform(BlurTransformation(10,10)))
//                    .into(ivWholeCoverBg)
//                ivWholeCoverBg.alpha = 0f
            initRectCover()
        }
//        }
        initSongList()
    }

    open fun initRectCover() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_cover_default)
        ivRectCover.setImageBitmap(bitmap)
        blurRectCoverBitmap(bitmap)

    }
    private var foregroundDrawable:Drawable?=null

    private fun blurRectCoverBitmap(bitmap: Bitmap) {
        thread {
            foregroundDrawable = getForegroundDrawable(bitmap)
            requireActivity().runOnUiThread {
                mRootLayout.foreground = foregroundDrawable
                mRootLayout.beginAnimation()
            }
        }
    }
//    private fun getForegroundDrawable(bitmap:Bitmap ):Drawable {
//        val widthHeightSize = (requireContext().getScreenWidth()*1.0/requireContext().getScreenHeight()*1.0).toFloat()
//        val cropBitmapWidth = (widthHeightSize * bitmap.height).toInt()
//        val cropBitmapWidthX = (bitmap.width - cropBitmapWidth/2.0).toInt()
//        // 切歌部分图片
//        val cropBitmap = Bitmap.createBitmap(bitmap,cropBitmapWidthX,0,cropBitmapWidth,bitmap.height)
//        // 缩小图片
//        val scaleBitmap = Bitmap.createScaledBitmap(cropBitmap,bitmap.width / 50,bitmap.height/50,false)
//        // 模糊化
//        val blurBitmap = FastBlurUtil.doBlur(scaleBitmap,8,true)
//        val foregroundDrawable = BitmapDrawable(blurBitmap)
//        //加入灰色遮罩层，避免图片过亮影响其他控件
//        foregroundDrawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY)
//        return foregroundDrawable
//    }

    private fun getForegroundDrawable(bitmap: Bitmap): Drawable? {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        val cropBitmapWidth = ((ImUtils.getScreenWidth(requireContext())
                * 1.0 / ImUtils.getScreenHeight(requireContext()) * 1.0) * bitmap.height).toInt()
        Log.e("JG","宽高比：$cropBitmapWidth")

        val cropBitmapWidthX = ((bitmap.width - cropBitmapWidth) / 2.0).toInt()
        Log.e("JG","cropBitmapWidthX：$cropBitmapWidthX")

        /*切割部分图片*/
        val cropBitmap = Bitmap.createBitmap(
            bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
            bitmap.height
        )
        /*缩小图片*/
        val scaleBitmap = Bitmap.createScaledBitmap(
            cropBitmap, bitmap.width / 50, bitmap
                .height / 50, false
        )
        /*模糊化*/
        val blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true)
//        val blurBitmap = FastBlurUtil.blurBitmap(requireContext(),bitmap,8f)
        val foregroundDrawable: Drawable = BitmapDrawable(blurBitmap)
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(
            Color.GRAY,
            PorterDuff.Mode.MULTIPLY
        )
        return foregroundDrawable
    }


    private fun initSongList() {
        rvContent.setup<Any> {
            adapter {
                addItem(R.layout.imusic_item_common_song) {
                    bindViewHolder { data, pos, holder ->
                        when (mSongListType) {
                            Constant.SONG_LIST_TYPE_LOVE -> {
                                val s = data as LoveSong
                                setText(R.id.common_item_tv_song_name, s.name)
                                setText(R.id.common_item_tv_song_singer, s.singer)
                                setVisible(R.id.common_item_iv_is_download, s.isDownload == true)
                                val currentSongId = SongUtil.getSong()?.songId
                                if (currentSongId != null &&
                                    s.songId == currentSongId
                                ) {
                                    setVisible(R.id.common_item_iv_is_playing, true)
                                    mLastPosition = pos
                                } else {
                                    itemView?.findViewById<ImageView>(R.id.iv_item_song_laba)?.visibility =
                                        View.GONE
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
                                        isOnline = loveSong.isOnline ?: false
                                        url = loveSong.url
                                        imgUrl = loveSong.pic
                                        position = pos
                                        duration = loveSong.duration ?: 0
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

    abstract fun getSongListType(): Int


    private fun calculateColors(url: String) {
        if (url.isNotEmpty()) {
            thread {
                try {
                    val fileUrl = URL(url)
                    val bitmap: Bitmap?
                    val conn = fileUrl.openConnection()
                    if (conn != null) {
                        conn.doInput = true
                        conn.connect()
                        val input = conn.getInputStream()
                        val opt = BitmapFactory.Options()
                        opt.inSampleSize = 8
                        bitmap = BitmapFactory.decodeStream(input, Rect(), opt)
                        val msg = Message.obtain()
                        msg.what = 0
                        msg.obj = bitmap
                        drawableHandler.sendMessage(msg)
                        input.close()
                    }
                } catch (e: IOException) {
                    Log.e("JG", "---calculateColors error:_${e.message}")
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private val drawableHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                val bitmap = msg.obj as Bitmap
                Log.e("JG", "bitmap=$bitmap")
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
//        if (alphaAnimator == null) {
//            alphaAnimator = ObjectAnimator.ofFloat(ivWholeAlphaBg, "alpha", 0f, 0.5f).apply {
//                duration = 1500
//                interpolator = AccelerateDecelerateInterpolator()
//            }
//        }
//        if (coverAlphaAnimator == null) {
//            coverAlphaAnimator = ObjectAnimator.ofFloat(ivWholeCoverBg, "alpha", 0f, 0.5f).apply {
//                duration = 1500
//                interpolator = AccelerateDecelerateInterpolator()
//            }
//        }
    }

    private fun initToolbarAlpha() {
        val alphaMaxOffset = 150f.toIntPx()
        toolBar.background.alpha = 0
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // 设置toolbar背景
            Log.e("JG","verticalOffset=$verticalOffset")
            if (verticalOffset > -alphaMaxOffset) {
                Log.e("JG","----111")
                toolBar.background.alpha = 255 * -verticalOffset / alphaMaxOffset
                song_list_tv_title_name.text = "歌单"
            } else {
                // todo 根据获取的颜色改变状态栏颜色
                Log.e("JG","----222")
                toolBar.background.alpha = 255
                toolBar.background = foregroundDrawable
                song_list_tv_title_name.text = "回答1998"
            }
        })
    }


//    override fun onResume() {
//        super.onResume()
//        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener(){
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?) {
//                val alphaPercent = ((song_list_ll_content.top -minDistance) / deltaDistance).toFloat()
//                ivRectCover.alpha = alphaPercent
//                song_list_siv_user_avatar.alpha = alphaPercent
//                song_list_tv_user_name.alpha = alphaPercent
//            }
//
//            override fun onStateChanged(appBarLayout: AppBarLayout?, state: BarState) {
//            }
//        })
//    }

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
            if (alphaAnimator?.isRunning == true) {
                alphaAnimator?.cancel()
            }
            alphaAnimator = null
        }
        if (coverAlphaAnimator != null) {
            if (coverAlphaAnimator?.isRunning == true) {
                coverAlphaAnimator?.cancel()
            }
            coverAlphaAnimator = null
        }
    }
}