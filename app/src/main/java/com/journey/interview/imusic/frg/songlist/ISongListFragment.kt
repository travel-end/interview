package com.journey.interview.imusic.frg.songlist

import com.google.android.material.appbar.AppBarLayout
import com.journey.interview.R
import com.journey.interview.imusic.listener.AppBarStateChangeListener
import com.journey.interview.imusic.vm.ISongListViewModel
import com.journey.interview.utils.toIntPx
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_frg_song_list.*

/**
 * @By Journey 2020/10/22
 * @Description
 */
class ISongListFragment:BaseLifeCycleFragment<ISongListViewModel>() {
    private lateinit var appBar:AppBarLayout
    private  var minDistance:Int=0
    private var deltaDistance:Int=0
    override fun layoutResId()= R.layout.imusic_frg_song_list
    override fun initView() {
        super.initView()

        appBar = appbar_layout
    }

    override fun initData() {
        super.initData()
        minDistance = 85f.toIntPx(context = requireContext())
        deltaDistance = 300f.toIntPx() - minDistance
    }

    override fun dataObserve() {
        super.dataObserve()
    }

    override fun onResume() {
        super.onResume()
        appBar.addOnOffsetChangedListener(object :AppBarStateChangeListener(){
            override fun onOffsetChanged(appBarLayout: AppBarLayout?) {
                val alphaPercent = ((song_list_ll_content.top -minDistance) / deltaDistance).toFloat()
                song_list_riv_album_cover.alpha = alphaPercent
                song_list_siv_user_avatar.alpha = alphaPercent
                song_list_tv_user_name.alpha = alphaPercent
            }

            override fun onStateChanged(appBarLayout: AppBarLayout?, state: BarState) {
            }
        })
    }
}