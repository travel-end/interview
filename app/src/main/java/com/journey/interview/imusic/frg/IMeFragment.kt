package com.journey.interview.imusic.frg

import android.content.Intent
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.journey.interview.R
import com.journey.interview.imusic.frg.me.IHistorySongActivity
import com.journey.interview.imusic.frg.me.ILocalSongActivity
import com.journey.interview.imusic.frg.me.ILoveSongActivity
import com.journey.interview.imusic.vm.IMeViewModel
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IMeFragment:BaseLifeCycleFragment<IMeViewModel>() {
    companion object {
        fun newInstance() : Fragment {
            return IMeFragment()
        }
    }
    override fun layoutResId()=R.layout.imusic_frg_me

    override fun initView() {
        super.initView()
        mRootView.findViewById<FrameLayout>(R.id.fl_my_love_song).setOnClickListener {
            startActivity(Intent(requireActivity(),ILoveSongActivity::class.java))
        }
        mRootView.findViewById<TextView>(R.id.me_tv_local).setOnClickListener {
            startActivity(Intent(requireActivity(),ILocalSongActivity::class.java))
        }
        mRootView.findViewById<TextView>(R.id.me_tv_history).setOnClickListener {
            startActivity(Intent(requireActivity(),IHistorySongActivity::class.java))
        }
    }
}