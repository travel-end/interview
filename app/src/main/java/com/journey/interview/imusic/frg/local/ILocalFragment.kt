package com.journey.interview.imusic.frg.local

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.imusic.vm.ILocalSongViewModel
import com.journey.interview.weatherapp.base.BaseFragment
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment

class ILocalFragment:BaseLifeCycleFragment<ILocalSongViewModel>() {
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
        type = arguments?.getInt("localType",0)
        Log.e("Jg","type=$type")

    }
}