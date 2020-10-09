package com.journey.interview.imusic.frg.local

import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.customizeview.tablayout.CustomTab
import com.journey.interview.imusic.base.BaseVpFragment
import com.journey.interview.imusic.model.IMusicTab
import com.journey.interview.utils.getString
import kotlinx.android.synthetic.main.imusic_act_local_song.*

class ILocalSongVpFragment:BaseVpFragment() {
    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            ILocalFragment.newInstance(ILocalFragment.SONG),
            ILocalFragment.newInstance(ILocalFragment.MV)
        )
    override val vpTitles: ArrayList<CustomTab>
        get() = ArrayList<CustomTab>().apply {
            add(IMusicTab(R.string.imusic_tab_song.getString()))
            add(IMusicTab(R.string.mv.getString()))
        }

    //    private val fragments = mutableListOf<Fragment>()
    override fun layoutResId()= R.layout.imusic_act_local_song

    override fun initView() {
        mTabLayout = local_title_tabLayout
        mViewPager = local_pager
//        fragments.add(ILocalFragment.newInstance(ILocalFragment.MV))
//        fragments.add(ILocalFragment.newInstance(ILocalFragment.SONG))
//        local_pager.run {
//            offscreenPageLimit=1
//            adapter = pagerAdapter
//        }
//        val localTitles = ArrayList<CustomTab>()
//        localTitles.add(IMusicTab(R.string.imusic_tab_song.getString()))
//        localTitles.add(IMusicTab(R.string.mv.getString()))
//        local_title_tabLayout.setTabData(localTitles)
//        local_title_tabLayout.setOnTabSelectListener(object :OnTabSelectListener{
//            override fun onTabSelect(position: Int) {
//                local_pager.currentItem = position
//            }
//
//            override fun onTabReselect(position: Int) {
//            }
//
//        })
//        local_pager.registerOnPageChangeCallback(pagerCallback)

    }
//
//    private val pagerAdapter = object :FragmentStateAdapter(this) {
//        override fun getItemCount()=fragments.size
//
//        override fun createFragment(position: Int)=fragments[position]
//
//    }
//
//    private val pagerCallback = object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//            local_title_tabLayout.currentTab = position
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        local_pager.unregisterOnPageChangeCallback(pagerCallback)
//    }

}