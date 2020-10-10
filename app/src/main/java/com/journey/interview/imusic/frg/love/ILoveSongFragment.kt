package com.journey.interview.imusic.frg.love

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.Downloaded
import com.journey.interview.imusic.model.LoveSong
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.vm.ILoveSongViewModel
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.DensityUtil
import com.journey.interview.utils.FileUtil
import com.journey.interview.utils.toIntPx
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_act_love_song.*

class ILoveSongFragment:BaseLifeCycleFragment<ILoveSongViewModel>() {
    private lateinit var appBar:AppBarLayout
    private lateinit var toolBar:Toolbar
    private var mLastPosition:Int = -1
    private var loveSongs = mutableListOf<LoveSong>()
    override fun layoutResId()= R.layout.imusic_act_love_song
    private var playBinder:IMusicPlayService.PlayStatusBinder?=null
    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playBinder = service as IMusicPlayService.PlayStatusBinder
        }
    }

    override fun initView() {
        super.initView()
        val playIntent = Intent(requireActivity(),IMusicPlayService::class.java)
        requireActivity().bindService(playIntent,playConnection, Context.BIND_AUTO_CREATE)
        appBar = love_appbar
        toolBar = love_toolbar
        initToolbar()
        val loveSongRv = mRootView.findViewById<RecyclerView>(R.id.rv_love_song)
        loveSongRv.setHasFixedSize(true)
        loveSongRv.setup<LoveSong> {
//            dataSource(test())
            adapter {
                addItem(R.layout.imusic_item_song) {
//                        isForViewType { data, position -> data is MutableList<SingerBean> }
                    bindViewHolder { data, pos, holder ->
                        data?.let {
                            setText(R.id.tv_item_song_name,data.name)
                            setText(R.id.tv_item_song_singer,data.singer)
                            setVisible(R.id.tv_item_download_ok,data.isDownload==true)
                            val currentSongId = FileUtil.getSong()?.songId
                            if (currentSongId != null &&
                                data.songId == currentSongId) {
                                itemView?.findViewById<ImageView>(R.id.iv_item_song_laba)?.visibility = View.VISIBLE
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
//                                Log.e("JG","本地音乐路径：${loveSong.url}")
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
                                FileUtil.saveSong(song)
                                playBinder?.play(Constant.LIST_TYPE_LOVE)
                            })
                        }
                    }
                }
            }
        }
    }
    
    private fun initToolbar() {
//        ImmersionBar.with(this)
//            .transparentBar()
//            .init()
//        ImmersionBar.with(this).statusBarView(R.id.top_view).fullScreen(true).init()
//        try {
//            val statusBatHeight = DensityUtil.getStatusBarHeight(requireActivity())
//            Log.e("JG","statusBatHeight= $statusBatHeight")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                setStatusBarStyle()
//                toolBar.setPadding(0,statusBatHeight,0,0)
//                toolBar.layoutParams.height = 46f.toIntPx() + statusBatHeight
//            }
//        } catch (e:Exception) {
//
//        }

        val alphaMaxOffset = 150f.toIntPx()
        toolBar.background.alpha = 0
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // 设置toolbar背景
            if (verticalOffset > -alphaMaxOffset) {
                toolBar.background.alpha = 255 * -verticalOffset / alphaMaxOffset
            } else {
                toolBar.background.alpha = 255
            }
        })
    }

    /**
     * 沉浸式状态栏
     */
    private fun setStatusBarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = requireActivity().window
            window?.run {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                requireActivity().window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getAllMyLoveSong()
    }

    // todo
//    override fun initStatusBarColor() {
//        ImmersionBar.with(requireActivity()).statusBarView(R.id.love_top_view)
//            .fullScreen(true).init()
//    }

    private fun orderList(tempList:MutableList<LoveSong>):MutableList<LoveSong> {
        val loveList= mutableListOf<LoveSong>()
        loveList.clear()
        for (i in tempList.indices.reversed()) {
            loveList.add(tempList[i])
        }
        return loveList
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.loveSongList.observe(this, Observer {
            it?.let {list->
                if (list.isEmpty()) {
//                    love_btn_add_song.visibility = View.VISIBLE
                    rv_love_song.visibility = View.GONE
                } else {
                    loveSongs.clear()
                    loveSongs.addAll(orderList(list))
                    rv_love_song.submitList(list)
                }
            }
        })
        IMusicBus.observeLoveSongChange(this) {
                mViewModel.getAllMyLoveSong()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unbindService(playConnection)
    }

    private fun test():MutableList<LoveSong> {
        val list = mutableListOf<LoveSong>()
        for (i in 0..14) {
            val bean = LoveSong()
            bean.name="$i zzz"
            list.add(bean)
        }
        return list
    }
}