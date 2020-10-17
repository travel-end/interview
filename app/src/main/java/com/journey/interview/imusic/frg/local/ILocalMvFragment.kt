package com.journey.interview.imusic.frg.local

import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.imusic.vm.ILocalSongViewModel
import com.journey.interview.utils.getString
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment

/**
 * 本地音乐
 * 1、先去数据库查找是否有本地音乐
 * 2、如果没有 检索本地音乐 检索到了就存入数据库
 */
class ILocalMvFragment:BaseLifeCycleFragment<ILocalSongViewModel>() {
    companion object {
        fun newInstance():Fragment {
            return ILocalMvFragment()
        }
    }
    override fun layoutResId()=R.layout.imusic_frg_local_mv
    override fun initView() {
        super.initView()
        showEmpty(msg = R.string.no_mv.getString())
    }
}