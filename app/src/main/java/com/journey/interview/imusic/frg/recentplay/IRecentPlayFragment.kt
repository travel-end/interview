package com.journey.interview.imusic.frg.recentplay

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.model.HistorySong
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.imusic.vm.IHistorySongViewModel
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.FileUtil
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_act_history.*

/**
 * 最近播放的曲目
 */
class IRecentPlayFragment:BaseLifeCycleFragment<IHistorySongViewModel>() {
    private lateinit var mRvHistory:RecyclerView
    private var mLastPosition:Int = -1
    private var historySongs= mutableListOf<HistorySong>()
    private var playBinder: IMusicPlayService.PlayStatusBinder?=null
    private val playConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playBinder = service as IMusicPlayService.PlayStatusBinder
        }
    }
    override fun layoutResId()=R.layout.imusic_act_history
    override fun initView() {
        super.initView()
        mRvHistory = rv_history_songs
        val playIntent = Intent(requireActivity(),IMusicPlayService::class.java)
        requireActivity().bindService(playIntent,playConnection, Context.BIND_AUTO_CREATE)
        mRvHistory.setup<HistorySong> {
            adapter {
                addItem(R.layout.imusic_item_song) {
                    bindViewHolder { data, pos, holder ->
                        setText(R.id.tv_item_song_name,data?.name)
                        setText(R.id.tv_item_song_singer,data?.singer)
                        val currentSongId = FileUtil.getSong()?.songId
                        if (currentSongId != null &&
                            data?.songId == currentSongId) {
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
                            val historySong = historySongs[pos]
                            Log.e("JG","历史播放音乐路径：${historySong.url}")
                            val song = Song().apply {
                                songId = historySong.songId
                                songName = historySong.name
                                singer = historySong.singer
                                isOnline = historySong.isOnline
                                url = historySong.url
                                imgUrl = historySong.pic
                                position = pos
                                duration = historySong.duration?.toInt()?:0
                                listType = Constant.LIST_TYPE_HISTORY
                                mediaId = historySong.mediaId
                            }
                            FileUtil.saveSong(song)
                            playBinder?.play(Constant.LIST_TYPE_HISTORY)
                        })
                    }
                }
            }
        }
        mViewModel.getAllHistorySongs()
    }

    override fun initData() {
        super.initData()
        history_tv_clear.setOnClickListener {
            mViewModel.deleteAllHistorySongs()
        }
    }
    /**
     * 把最近下载的歌曲放在最上面
     */
    private fun orderList(tempList:MutableList<HistorySong>?):MutableList<HistorySong>? {
        val loveList= mutableListOf<HistorySong>()
        loveList.clear()
        return if (tempList != null) {
            for (i in tempList.indices.reversed()) {
                loveList.add(tempList[i])
            }
            loveList
        } else {
            null
        }
    }
    override fun dataObserve() {
        super.dataObserve()
        mViewModel.historySongsResult.observe(this,Observer{
            it?.let {list->
                if (list.isNotEmpty()) {
                    historySongs.clear()
                    historySongs.addAll(orderList(list)!!)
                    mRvHistory.submitList(historySongs)
                }
            }
        })
        mViewModel.deleteSongsResult.observe(this,Observer{
            if (it) {
                historySongs.clear()
                mRvHistory.submitList(historySongs)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unbindService(playConnection)
    }
}