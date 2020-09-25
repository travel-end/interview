package com.journey.interview.imusic.frg

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.journey.interview.R
import com.journey.interview.customizeview.tablayout.CustomTab
import com.journey.interview.imusic.base.BaseVpFragment
import com.journey.interview.imusic.model.IMusicTab
import com.journey.interview.utils.getString

/**
 * @By Journey 2020/9/25
 * @Description 使用viewPager  负责显示4个滑动的fragment
 */
class IMainFragment : BaseVpFragment() {
    override fun layoutResId() = R.layout.imusic_frg_main

    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            IMeFragment.newInstance(),
            IFindFragment.newInstance(),
            ICloudVillageFragment.newInstance(),
            IVideoFragment.newInstance()
        )
    override val vpTitles = ArrayList<CustomTab>().apply {
        add(IMusicTab(R.string.imusic_tab_me.getString()))
        add(IMusicTab(R.string.imusic_tab_find.getString()))
        add(IMusicTab(R.string.imusic_tab_cloud_village.getString()))
        add(IMusicTab(R.string.imusic_tab_video.getString()))
    }

    override fun initView() {
        super.initView()
        mViewPager.currentItem = 1
    }

    override fun initData() {
        super.initData()
        mTitleSearch.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }
}