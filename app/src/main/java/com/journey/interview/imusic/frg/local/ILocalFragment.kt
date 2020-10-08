package com.journey.interview.imusic.frg.local

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.journey.interview.R
import com.journey.interview.imusic.model.LocalSong
import com.journey.interview.imusic.vm.ILocalSongViewModel
import com.journey.interview.recyclerview.core.addItem
import com.journey.interview.recyclerview.core.setText
import com.journey.interview.recyclerview.core.setup
import com.journey.interview.recyclerview.core.submitList
import com.journey.interview.weatherapp.base.BaseFragment
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
        mRvLocalSong = rv_local_songs
        type = arguments?.getInt("localType",0)
        Log.e("Jg","type=$type")
        mViewModel.getSavedLocalSongs()
        mRvLocalSong.setup<LocalSong> {
            adapter {
                addItem(R.layout.imusic_item_search_song) {
                    bindViewHolder { data, position, holder ->
                        setText(R.id.tv_song_name,data?.name)
                    }
                }
            }
        }

    }

    override fun initData() {
        super.initData()
        local_tv_songs.setOnClickListener {
            mViewModel.getLocalSongs()
        }
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.localSongResult.observe(this,Observer{
            localSongsList.clear()
            if (it !=null) {
                if (it.isNotEmpty()) {
                    localSongsList.addAll(it)
                    mRvLocalSong.submitList(it)
                } else {
                    Log.e("JG","empty 本地音乐列表为空")
                }
            } else {
                Log.e("JG","null 本地音乐列表为空")
            }
        })
    }
}