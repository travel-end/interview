package com.journey.interview.imusic.frg.download

import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.weatherapp.base.BaseFragment

/**
 * @By Journey 2020/10/9
 * @Description
 */
class IDownloadingFragment:BaseFragment() {
    companion object {
        fun newInstance(): Fragment {
            return IDownloadingFragment()
        }
    }
    override fun layoutResId()=R.layout.imusic_frg_downloading
}