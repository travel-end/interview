package com.journey.interview.imusic.frg.love

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.base.BaseSongListFragment
import com.journey.interview.imusic.model.LoveSong
import com.journey.interview.imusic.vm.ILoveSongViewModel
import com.journey.interview.recyclerview.core.submitList
import kotlinx.android.synthetic.main.imusic_act_love_song.*

/**
 * @By Journey 2020/10/22
 * @Description
 */
class INewLoveSongFragment :BaseSongListFragment<ILoveSongViewModel>(){
    override fun layoutResId()= R.layout.imusic_frg_song_list
    companion object {
        fun newInstance(albumImgUrl:String): Fragment {//todo 通過Navigation传递直
            val bundle = Bundle()
            val demo = "http://y.gtimg.cn/music/photo_new/T002R180x180M000003RMaRI1iFoYd.jpg"
//            bundle.putString("albumImgUrl",albumImgUrl)
            bundle.putString("albumImgUrl",demo)
            val fragment = INewLoveSongFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getAllMyLoveSong()
    }

    override fun dataObserve() {
        super.dataObserve()
        mViewModel.loveSongList.observe(this, Observer {
            it?.let {list->
                if (list.isEmpty()) {
                    rvContent.visibility = View.GONE
                } else {
                    loveSongs.clear()
                    loveSongs.addAll(orderList(list))
                    rvContent.submitList(list)
                }
            }
        })
    }

    private fun orderList(tempList:MutableList<LoveSong>):MutableList<LoveSong> {
        val loveList= mutableListOf<LoveSong>()
        loveList.clear()
        for (i in tempList.indices.reversed()) {
            loveList.add(tempList[i])
        }
        return loveList
    }

    override fun getSongListType()=Constant.SONG_LIST_TYPE_LOVE
}