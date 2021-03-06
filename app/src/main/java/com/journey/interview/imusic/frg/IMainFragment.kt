package com.journey.interview.imusic.frg

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.customizeview.tablayout.CustomTab
import com.journey.interview.imusic.base.BaseVpFragment
import com.journey.interview.imusic.listener.OnFragmentEventListener
import com.journey.interview.imusic.model.IMusicTab
import com.journey.interview.utils.getString

/**
 * @By Journey 2020/9/25
 * @Description 使用viewPager  负责显示4个滑动的fragment
 */
class IMainFragment : BaseVpFragment() {
    private var eventListener: OnFragmentEventListener?=null
    override fun layoutResId() = R.layout.imusic_frg_main

    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            IMeFragment.newInstance(),
            IHomeFragment.newInstance(),
            IFindPoetryFragment.newInstance(),
            IVideoFragment.newInstance()
        )
    override val vpTitles = ArrayList<CustomTab>().apply {
        add(IMusicTab(R.string.imusic_tab_me.getString()))
        add(IMusicTab(R.string.imusic_tab_find.getString()))
        add(IMusicTab(R.string.imusic_tab_cloud_village.getString()))
        add(IMusicTab(R.string.imusic_tab_video.getString()))
    }

    override fun initData() {
        super.initData()
        mViewPager.setCurrentItem(1,false)
        eventListener = requireActivity() as OnFragmentEventListener
        eventListener?.sendEvent(null,Constant.EVENT_REFRESH_HOME_DATA)
        mTitleSearch.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_searchFragment)
        }
        mTitleList.setOnClickListener {
            eventListener?.sendEvent(it,Constant.EVENT_OPEN_DRAWER)
        }
    }
}