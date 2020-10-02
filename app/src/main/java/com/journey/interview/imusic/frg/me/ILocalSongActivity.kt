package com.journey.interview.imusic.frg.me

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.journey.interview.R
import com.journey.interview.customizeview.tablayout.CustomTab
import com.journey.interview.customizeview.tablayout.OnTabSelectListener
import com.journey.interview.imusic.frg.local.ILocalFragment
import com.journey.interview.imusic.model.IMusicTab
import com.journey.interview.imusic.vm.ILocalSongViewModel
import com.journey.interview.utils.getString
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_local_song.*

class ILocalSongActivity:BaseLifeCycleActivity<ILocalSongViewModel>() {
    private val fragments = mutableListOf<Fragment>()
    override fun layoutResId()= R.layout.imusic_act_local_song
    override fun initView() {
        super.initView()
        fragments.add(ILocalFragment.newInstance(ILocalFragment.MV))
        fragments.add(ILocalFragment.newInstance(ILocalFragment.SONG))
        local_pager.run {
            offscreenPageLimit=1
            adapter = pagerAdapter
        }
        val localTitles = ArrayList<CustomTab>()
        localTitles.add(IMusicTab(R.string.imusic_tab_song.getString()))
        localTitles.add(IMusicTab(R.string.mv.getString()))
        local_title_tabLayout.setTabData(localTitles)
        local_title_tabLayout.setOnTabSelectListener(object :OnTabSelectListener{
            override fun onTabSelect(position: Int) {
                local_pager.currentItem = position
            }

            override fun onTabReselect(position: Int) {
            }

        })
        local_pager.registerOnPageChangeCallback(pagerCallback)

    }

    private val pagerAdapter = object :FragmentStateAdapter(this) {
        override fun getItemCount()=fragments.size

        override fun createFragment(position: Int)=fragments[position]

    }

    private val pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            local_title_tabLayout.currentTab = position
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        local_pager.unregisterOnPageChangeCallback(pagerCallback)
    }

}