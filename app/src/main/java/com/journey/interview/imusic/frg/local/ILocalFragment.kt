package com.journey.interview.imusic.frg.local

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.LocalSong
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.vm.ILocalSongViewModel
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.getString
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_frg_local.*

/**
 * 本地音乐
 * 1、先去数据库查找是否有本地音乐
 * 2、如果没有 检索本地音乐 检索到了就存入数据库
 */
class ILocalFragment:BaseLifeCycleFragment<ILocalSongViewModel>() {
    private val localSongsList = mutableListOf<LocalSong>()
    private lateinit var mRvLocalSong:RecyclerView
    private var playBinder: IMusicPlayService.PlayStatusBinder?=null
    private var mLastPosition:Int = -1
    private var listNumCode:Int = -1
    private lateinit var layoutManager: LinearLayoutManager
    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playBinder = service as IMusicPlayService.PlayStatusBinder
        }
    }
    private var type :Int? = null
    companion object {
        const val SONG = 1
        const val MV = 2
        fun newInstance(type:Int):Fragment {
            val fragment = ILocalFragment()
            val bundle = Bundle().apply {
                putInt("localType",type)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun layoutResId()=R.layout.imusic_frg_local


    override fun initView() {
        super.initView()
        val playIntent = Intent(requireActivity(),IMusicPlayService::class.java)
        requireActivity().bindService(playIntent,playConnection, Context.BIND_AUTO_CREATE)
        mRvLocalSong = rv_local_songs
        type = arguments?.getInt("localType",0)
        Log.e("Jg","type=$type")
        layoutManager = LinearLayoutManager(requireContext())
        if (type == SONG) {
            mViewModel.getSavedLocalSongs()
            mRvLocalSong.setup<LocalSong> {
                withLayoutManager {
                    layoutManager
                }
                adapter {
                    addItem(R.layout.imusic_item_local_song) {
                        bindViewHolder { data, pos, holder ->
                            setText(R.id.tv_item_local_song_name,data?.name)
                            setText(R.id.tv_item_local_song_singer,data?.singer)
                            val currentSongId = SongUtil.getSong()?.songId
                            if (currentSongId != null &&
                                data?.songId == currentSongId) {
                                itemView?.findViewById<ImageView>(R.id.iv_item_local_song_laba)?.visibility = View.VISIBLE
                                mLastPosition = pos
                            } else {
                                itemView?.findViewById<ImageView>(R.id.iv_item_local_song_laba)?.visibility = View.GONE
                            }
                            itemClicked(View.OnClickListener {
                                if (pos != mLastPosition) {
                                    notifyItemChanged(mLastPosition)
                                    mLastPosition = pos
                                }
                                notifyItemChanged(pos)
                                val mp3Info = localSongsList[pos]
                                val song = Song().apply {
                                    songName = mp3Info.name
                                    singer = mp3Info.singer
                                    url = mp3Info.url
                                    duration = mp3Info.duration?.toInt()?:0
                                    position = pos
                                    isOnline = false
                                    songId = mp3Info.songId
                                    listType = Constant.LIST_TYPE_LOCAL
                                }
                                SongUtil.saveSong(song)
                                playBinder?.play(Constant.LIST_TYPE_LOCAL)
                            })
                        }
                    }
                }
            }
            // 滚动至当前播放的位置
            if (SongUtil.getSong() != null) {
                layoutManager.scrollToPositionWithOffset(SongUtil.getSong()!!.position-4,mRvLocalSong.height)
            }
        } else if (type == MV) {
            local_tv_songs.visibility = View.VISIBLE
            local_tv_songs.text = R.string.no_local_mv.getString()
        }
    }

    override fun initData() {
        super.initData()
        local_tv_songs.setOnClickListener {
            if (type == SONG) {
                mViewModel.getLocalSongs()
            }
        }
    }

    override fun dataObserve() {
        super.dataObserve()
        // 从本地检索  结果
        mViewModel.localSongResult.observe(this,Observer{
            if (it.isNullOrEmpty()) {
                // 本地音乐为空
                Toast.makeText(requireContext(),R.string.no_local_song.getString(),Toast.LENGTH_SHORT).show()
            } else {
                localSongsList.clear()
                local_tv_songs.visibility = View.GONE
                Toast.makeText(requireContext(),"找到了您手机上藏起来的${it.size}首音乐(*^_^*)",Toast.LENGTH_SHORT).show()
                localSongsList.addAll(it)
                mRvLocalSong.submitList(localSongsList)
            }
        })

        // 从数据库去查询是否已经有储存的本地歌曲
        mViewModel.localSaveResult.observe(this,Observer{
            if (it.isNullOrEmpty()) {
                local_tv_songs.visibility = View.VISIBLE
            } else {
                if (listNumCode == -1) {
                    local_tv_songs.visibility = View.GONE
                }
                localSongsList.clear()
                localSongsList.addAll(it)
                mRvLocalSong.submitList(localSongsList)
            }
        })
        IMusicBus.observeSongListNumChange(this) {
//            Log.e("JG","observeSongListNumChange--->$it")
            listNumCode = it
            if (it == Constant.LIST_TYPE_DOWNLOAD) {
                if (type == SONG) {
                    local_tv_songs.visibility = View.VISIBLE
                    local_tv_songs.text = R.string.refresh_music.getString()
                }
            }
        }
//        IMusicBus.observeRefreshLocalSongChange(this,{
//            if (type == SONG) {
//                mViewModel.getLocalSongs()
//            }
//        })
//        IMusicBus.musicEvent.commonEventStatus.observe(this,observer)
    }

//    private val observer = Observer<Int> {
//        if (type == SONG) {
//            mViewModel.getLocalSongs()
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unbindService(playConnection)
//        IMusicBus.musicEvent.commonEventStatus.removeObserver(observer)
    }
}