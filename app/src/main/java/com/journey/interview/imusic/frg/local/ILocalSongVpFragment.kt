package com.journey.interview.imusic.frg.local

import androidx.fragment.app.Fragment
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.customizeview.tablayout.CustomTab
import com.journey.interview.imusic.base.BaseVpFragment
import com.journey.interview.imusic.global.Bus
import com.journey.interview.imusic.model.IMusicTab
import com.journey.interview.utils.getString
import kotlinx.android.synthetic.main.imusic_act_local_song.*

class ILocalSongVpFragment:BaseVpFragment() {
    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            ILocalFragment.newInstance(),
            ILocalMvFragment.newInstance()
        )
    override val vpTitles: ArrayList<CustomTab>
        get() = ArrayList<CustomTab>().apply {
            add(IMusicTab(R.string.imusic_tab_song.getString()))
            add(IMusicTab(R.string.mv.getString()))
        }
    override fun layoutResId()= R.layout.imusic_act_local_song
    override fun initView() {
        mTabLayout = local_title_tabLayout
        mViewPager = local_pager
    }

    override fun initData() {
        super.initData()
//        local_tv_refresh.setOnClickListener {
//            Bus.post(Constant.EVENT_APP,Constant.EVENT_REFRESH_LOCAL_SONG)
//        }
    }
}