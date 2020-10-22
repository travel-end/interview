package com.journey.interview.imusic.frg.love

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.imusic.base.BaseSongListFragment
import com.journey.interview.imusic.vm.ILoveSongViewModel

/**
 * @By Journey 2020/10/22
 * @Description
 */
class INewLoveSongFragment :BaseSongListFragment<ILoveSongViewModel>(){
    override fun layoutResId()= R.layout.imusic_frg_song_list
    companion object {
        fun newInstance(albumImgUrl:String): Fragment {
            val bundle = Bundle()
            bundle.putString("albumImgUrl",albumImgUrl)
            val fragment = INewLoveSongFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}