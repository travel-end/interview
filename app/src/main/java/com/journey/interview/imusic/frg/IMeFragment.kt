package com.journey.interview.imusic.frg

import androidx.fragment.app.Fragment
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
}