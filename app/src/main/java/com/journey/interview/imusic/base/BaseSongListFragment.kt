package com.journey.interview.imusic.base

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Message
import android.view.WindowManager
import android.widget.ImageView
import com.google.android.material.appbar.AppBarLayout
import com.journey.interview.imusic.listener.AppBarStateChangeListener
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
abstract class BaseSongListFragment<VM:BaseViewModel>:BaseLifeCycleFragment<VM>() {
    private lateinit var appBar: AppBarLayout
    private  var minDistance:Int=0
    private var deltaDistance:Int=0
    private lateinit var ivAlbumCover:ImageView
    private lateinit var ivBg:ImageView
    private var albumCoverImgUrl:String?=null
    private var alphaAnimator:ObjectAnimator?=null
    private var coverAlphaAnimator:ObjectAnimator?=null
    override fun initView() {
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        appBar = appbar_layout
        ivAlbumCover = song_list_riv_album_cover
        ivBg = song_list_iv_background
        minDistance = 85f.toIntPx(context = requireContext())
        deltaDistance = 300f.toIntPx() - minDistance
        song_list_iv_cover.alpha = 0f
        /* ui*/

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

    private val drawableHandler = object :Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                ivBg.background = msg.obj as Drawable
            }

        }
    }



    override fun onResume() {
        super.onResume()
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener(){
            override fun onOffsetChanged(appBarLayout: AppBarLayout?) {
                val alphaPercent = ((song_list_ll_content.top -minDistance) / deltaDistance).toFloat()
                ivAlbumCover.alpha = alphaPercent
                song_list_siv_user_avatar.alpha = alphaPercent
                song_list_tv_user_name.alpha = alphaPercent
            }

            override fun onStateChanged(appBarLayout: AppBarLayout?, state: BarState) {
            }
        })
    }

}