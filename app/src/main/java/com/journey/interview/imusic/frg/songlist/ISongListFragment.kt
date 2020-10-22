package com.journey.interview.imusic.frg.songlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.imusic.base.BaseSongListFragment
import com.journey.interview.imusic.vm.ISongListViewModel

/**
 * @By Journey 2020/10/22
 * @Description
 */
class ISongListFragment:BaseSongListFragment<ISongListViewModel>() {
    //https://p1.music.126.net/68u951bt6jm-E4nCyuIuRw==/109951164342901886.jpg
    companion object {
        fun newInstance(albumImgUrl:String):Fragment {
            val bundle = Bundle()
            bundle.putString("albumImgUrl",albumImgUrl)
            val fragment = ISongListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutResId()= R.layout.imusic_frg_song_list
    override fun initView() {
        super.initView()
    }

    override fun initData() {
        super.initData()
    }
    override fun dataObserve() {
        super.dataObserve()
    }

    override fun getSongListType(): Int {
        TODO("Not yet implemented")
    }

}