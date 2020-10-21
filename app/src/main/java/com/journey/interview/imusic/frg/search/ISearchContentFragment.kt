package com.journey.interview.imusic.frg.search

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.customizeview.swipecaptcha.core.GlideUtil
import com.journey.interview.imusic.download.IMusicDownloadUtil
import com.journey.interview.imusic.model.AlListBean
import com.journey.interview.imusic.model.ISearchContentViewModel
import com.journey.interview.imusic.model.ListBean
import com.journey.interview.imusic.model.Song
import com.journey.interview.imusic.service.IMusicPlayService
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.SongUtil
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_frg_search_content.*

/**
 * @By Journey 2020/9/26
 * @Description
 */
class ISearchContentFragment :BaseLifeCycleFragment<ISearchContentViewModel>(){
    private var mSearchContent:String = ""
    private var mSearchType :String = ""
    private var mPlayStatusBinder:IMusicPlayService.PlayStatusBinder?=null
    companion object {
        const val KEY_SEARCH_CONTENT= "key_search_content"
        private const val KEY_PAGE_TYPE = "key_search_type"
        fun newInstance(searchContent:String,searchType:String):Fragment {
            val fragment = ISearchContentFragment()
            val bundle = Bundle().apply {
                putString(KEY_PAGE_TYPE,searchType)
                putString(KEY_SEARCH_CONTENT,searchContent)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun layoutResId()= R.layout.imusic_frg_search_content

    private val playConnection = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlayStatusBinder = service as IMusicPlayService.PlayStatusBinder
        }

    }

    override fun initView() {
        super.initView()
        arguments?.apply {
            mSearchContent = getString(KEY_SEARCH_CONTENT,"")
            mSearchType = getString(KEY_PAGE_TYPE,"")
        }

        // 启动服务
        val playIntent = Intent(requireActivity(),IMusicPlayService::class.java)
        requireContext().bindService(playIntent,playConnection,Context.BIND_AUTO_CREATE)

        if (mSearchType==ISearchVpFragment.TYPE_SONG) {
            rv_search_song.setup<ListBean> {
                adapter {
//                    addItem(R.layout.imusic_item_search_song_title) {
//                        isForViewType { data, position -> data is FindTitle }
//                        bindViewHolder { data, position, holder ->
//                            val d = data as FindTitle
//                            setText(R.id.tv_search_song_title,d.titleMain)
//                            setText(R.id.tv_search_song_duoxuan,d.titleSecond)
//                        }
//                    }
                    addItem(R.layout.imusic_item_search_song) {
//                        isForViewType { data, position -> data is MutableList<SingerBean> }
                        bindViewHolder { data, position, holder ->
                            data?.let {
                                val sinGer = mViewModel.getSinger(data)
                                setText(R.id.tv_song_singer,sinGer)
                                setText(R.id.tv_song_name,data.songname)
                                setText(R.id.tv_song_album_name,data.albumname)
                                if (!data.lyric.isNullOrBlank()) {
                                    setVisible(R.id.tv_song_lyric,true)
                                    setText(R.id.tv_song_lyric,data.albumname)
                                }
                                itemClicked(View.OnClickListener {
                                    // 组装搜索到的歌曲的有效信息
                                    val song= Song().apply {
                                        songId = data.songmid //004DrG5A2nm7q2
                                        singer = sinGer// 鸾音社
                                        songName = data.songname// 夜来寒雨晓来风
                                        //http://y.gtimg.cn/music/photo_new/T002R180x180M000004UvnL62KXhCQ.jpg
                                        imgUrl = "${Constant.ALBUM_PIC}${data.albummid}${Constant.JPG}"
                                        duration = data.interval//187  (秒)
                                        isOnline = true
                                        mediaId = data.strMediaMid//004DrG5A2nm7q2
                                        albumName = data.albumname//夜来寒雨晓来风
                                        // 是否已经下载过了（初次搜索为false）
                                        isDownload = IMusicDownloadUtil.isExistOfDownloadSong(data.songmid?:"")//003IHI2x3RbXLS
                                    }
                                    mViewModel.getSongUrl(song)
                                })
                            }
                        }
                    }

                }
            }
        }else {
            rv_search_song.setup<AlListBean> {
                adapter {
                    addItem(R.layout.imusic_item_search_album) {
                        bindViewHolder { data, position, holder ->
                            val iv = itemView?.findViewById<ImageView>(R.id.iv_search_album_icon)
                            GlideUtil.loadImg(requireContext(),data?.albumPic?:"",iv!!,R.drawable.ic_default,R.drawable.ic_default,null)
                            setText(R.id.tv_search_album_name,data?.albumName)
                            setText(R.id.tv_search_album_singer,data?.singerName)
                            setText(R.id.tv_search_album_date,data?.publicTime)
                        }
                    }
                }
            }

        }
    }

    override fun initData() {
        super.initData()
        if (mSearchType == ISearchVpFragment.TYPE_SONG) {
            mViewModel.searchSong(mSearchContent,1)
        } else if (mSearchType == ISearchVpFragment.TYPE_ALBUM){
            rl_search_view_title.visibility = View.GONE
            mViewModel.searchAlbum(mSearchContent,1)
        }
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.searchResult.observe(this,Observer{
            it?.let {
                val song = it.data?.song
                if (song!=null ) {
                    val songList = song.list
                    songList?.let {list->
                        if (list.isNotEmpty()) {
                            hideError()
                            rl_search_view_title.visibility = View.VISIBLE
                            rv_search_song.submitList(songList.toMutableList())
                            rv_search_song.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
        mViewModel.searchAlbumResult.observe(this,Observer{
            it?.let {
                val albumList = it.data?.album?.list
                if (!albumList.isNullOrEmpty()) {
                    hideError()
                    rv_search_song.submitList(albumList.toMutableList())
                }
            }
        })
        mViewModel.songUrlResult.observe(this,Observer{
            it?.let {
                val song = it.entries.find {entry->
                    entry.key == "song"
                }?.value as Song
                val url = it.entries.find {entry->
                    entry.key == "url"
                }?.value as String
                song.url = url// 播放地址
                SongUtil.saveSong(song)
                // 开始播放音乐
                mPlayStatusBinder?.playOnline()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unbindService(playConnection)
    }
}