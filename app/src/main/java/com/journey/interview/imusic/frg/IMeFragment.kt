package com.journey.interview.imusic.frg

import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.journey.interview.R
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
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_loveSongFragment)
        }
        mRootView.findViewById<TextView>(R.id.me_tv_local).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_localFragment)
        }
        mRootView.findViewById<TextView>(R.id.me_tv_history).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_recentPlayFragment)
        }
        mRootView.findViewById<TextView>(R.id.me_tv_download).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_downloadFragment)
        }
    }
}