package com.journey.interview.imusic.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.journey.interview.customizeview.swipecaptcha.core.GlideUtil
import com.journey.interview.imusic.listener.AppBarStateChangeListener
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.utils.toIntPx
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import com.journey.interview.weatherapp.base.BaseViewModel
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.imusic_frg_song_list.*
import java.io.IOException
import java.net.URL
import kotlin.concurrent.thread

/**
 * @By Journey 2020/10/22
 * @Description
 */
abstract class BaseSongListFragment<VM:BaseViewModel>:BaseLifeCycleFragment<VM>() {
    private lateinit var appBar: AppBarLayout
    private  var minDistance:Int=0
    private var deltaDistance:Int=0
    private lateinit var ivRectCover:ImageView// 圆角封面图片
    private lateinit var ivWholeAlphaBg:ImageView// 整个的透明度背景图片
    private lateinit var ivWholeCoverBg:ImageView// 整个的不带透明的封面背景图片

    private var alphaAnimator:ObjectAnimator?=null
    private var coverAlphaAnimator:ObjectAnimator?=null
    protected lateinit var rvContent:RecyclerView
    private var playBinder: IMusicPlayService.PlayStatusBinder?=null
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
        ivWholeCoverBg = song_list_iv_cover

        rvContent = song_list_rv_content//音乐列表
        rvContent.setHasFixedSize(true)
        minDistance = 85f.toIntPx(context = requireContext())
        deltaDistance = 300f.toIntPx() - minDistance
        /* ui*/
        initAnim()
        arguments?.let {
            val albumCoverImgUrl = it.getString("albumImgUrl")
            if (!albumCoverImgUrl.isNullOrBlank()) {
                // 设置歌单封面图片
                GlideUtil.loadNetImg(requireContext(),albumCoverImgUrl,ivRectCover)

                // 根据歌单封面图片计算出背景图片并设置背景图片
                calculateColors(albumCoverImgUrl)
                Glide.with(requireActivity())
                    .load(albumCoverImgUrl)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(10,10)))
                    .into(ivWholeCoverBg)
                ivWholeCoverBg.alpha = 0f
            }
        }

    }

    override fun initData() {
        super.initData()
    }

    private fun calculateColors(url:String) {
        if (url.isNotEmpty()) {
            thread {
                try {
                    val fileUrl:URL = URL(url)
                    var bitmap:Bitmap?=null
                    val conn = fileUrl.openConnection()
                    if (conn != null) {
                        conn.doInput = true
                        conn.connect()
                        val input = conn.getInputStream()
                        val opt = BitmapFactory.Options()
                        opt.inSampleSize = 270
                        bitmap = BitmapFactory.decodeStream(input,Rect(),opt)
                        val msg = Message.obtain()
                        msg.what = 0
                        msg.obj = BitmapDrawable(bitmap)
                        drawableHandler.sendMessage(msg)
                        input.close()
                    }
                } catch (e:IOException) {
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private val drawableHandler = object :Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                ivWholeAlphaBg.background = msg.obj as Drawable
                alphaAnimator?.start()
                coverAlphaAnimator?.start()
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
                val alphaPercent = ((song_list_ll_content.top -minDistance) / deltaDistance).toFloat()
                ivRectCover.alpha = alphaPercent
                song_list_siv_user_avatar.alpha = alphaPercent
                song_list_tv_user_name.alpha = alphaPercent
            }

            override fun onStateChanged(appBarLayout: AppBarLayout?, state: BarState) {
            }
        })
    }

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