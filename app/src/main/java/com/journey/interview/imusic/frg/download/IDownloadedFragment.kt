package com.journey.interview.imusic.frg.download

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.download.IMusicDownloadUtil
import com.journey.interview.imusic.model.Downloaded
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.getColor
import com.journey.interview.weatherapp.base.BaseFragment

/**
 * @By Journey 2020/10/9
 * @Description
 */
class IDownloadedFragment:BaseFragment() {
    private var downloadedSongs:MutableList<Downloaded>?=null
    private var playBinder:IMusicPlayService.PlayStatusBinder?=null
    private var mLastPosition:Int = -1
    private val playConnection = object :ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playBinder = service as IMusicPlayService.PlayStatusBinder
        }
    }
    companion object {
        fun newInstance(): Fragment {
            return IDownloadedFragment()
        }
    }
    override fun layoutResId()=R.layout.imusic_frg_downloaded
    override fun initView() {
        super.initView()
        val playIntent = Intent(requireActivity(),IMusicPlayService::class.java)
        requireActivity().bindService(playIntent,playConnection,Context.BIND_AUTO_CREATE)
        downloadedSongs = orderList(IMusicDownloadUtil.getSongFromFile(Constant.STORAGE_SONG_FILE))
        val rv= mRootView.findViewById<RecyclerView>(R.id.downloaded_rv)
        if (downloadedSongs != null && downloadedSongs?.isNotEmpty()==true) {
            rv.setup<Downloaded> {
                dataSource(downloadedSongs!!)
                adapter {
                    addItem(R.layout.imusic_item_song) {
                        bindViewHolder { data, pos, holder ->
                            setText(R.id.tv_item_song_name,data?.name)
                            setText(R.id.tv_item_song_singer,data?.singer)
                            setVisible(R.id.tv_item_download_ok,true)
                            if (data?.albumName != "") {
                                setText(R.id.tv_item_song_album_name,"- ${data?.albumName}")
                            }
                            val selectItem = pos % 2
                            if (selectItem == 0) {
                                itemView?.setBackgroundColor(R.color.colorWhite_10.getColor())
                            } else {
                                itemView?.setBackgroundColor(R.color.colorPrimaryLight2.getColor())
                            }
                            val currentSongId = SongUtil.getSong()?.songId
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
                                val downloadedSong = downloadedSongs!![pos]
                                Log.e("JG","本地音乐路径：${downloadedSong.url}")
                                val song = Song().apply {
                                    songId = downloadedSong.songId
                                    songName = downloadedSong.name
                                    singer = downloadedSong.singer
                                    isOnline = false
                                    url = downloadedSong.url
                                    imgUrl = downloadedSong.pic
                                    position = pos
                                    duration = downloadedSong.duration?.toInt()?:0
                                    listType = Constant.LIST_TYPE_DOWNLOAD
                                    mediaId = downloadedSong.mediaId
                                    isDownload = true
                                }
                                SongUtil.saveSong(song)
                                playBinder?.play(Constant.LIST_TYPE_DOWNLOAD)
//                            }
                            })
                        }
                    }
                }
            }
        }
    }

    /**
     * 把最近下载的歌曲放在最上面
     */
    private fun orderList(tempList:MutableList<Downloaded>?):MutableList<Downloaded>? {
        val loveList= mutableListOf<Downloaded>()
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

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unbindService(playConnection)
    }
}