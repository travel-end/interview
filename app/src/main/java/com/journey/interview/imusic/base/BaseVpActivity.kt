package com.journey.interview.imusic.base

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.journey.interview.customizeview.tablayout.CommonTabLayout
import com.journey.interview.customizeview.tablayout.CustomTab
import com.journey.interview.customizeview.tablayout.OnTabSelectListener
import com.journey.interview.weatherapp.base.BaseActivity
import com.journey.interview.weatherapp.base.BaseViewModel
import kotlinx.android.synthetic.main.imusic_frg_main.*
import kotlinx.android.synthetic.main.imusic_title_bar.*

/**
 * @By Journey 2020/10/9
 * @Description
 */
abstract class BaseVpActivity<VM:BaseViewModel>:BaseActivity<VM>() {
    abstract val vpFragments:Array<Fragment>
    abstract val vpTitles:ArrayList<CustomTab>
    private var mPageChangeCallback:PageChangeCallback?=null
    protected lateinit var mViewPager: ViewPager2
    protected lateinit var mTitleList: ImageView
    protected lateinit var mTitleSearch: ImageView
    protected lateinit var mTabLayout: CommonTabLayout

    private val vpAdapter:VpAdapter by lazy {
        VpAdapter(this).apply {
            addFragment(vpFragments)
        }
    }

    override fun initView() {
        mViewPager = imusic_main_pager
        mTitleList = iv_title_list
        mTitleSearch = iv_title_search
        mTabLayout = title_tabLayout
    }

    override fun initData() {
        super.initData()
        mViewPager.run {
            offscreenPageLimit = 1
            adapter = vpAdapter
        }
        mTabLayout.run {
            setTabData(vpTitles)
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    mViewPager.currentItem = position
                }

                override fun onTabReselect(position: Int) {
                }
            })
        }
        mPageChangeCallback = PageChangeCallback()
        mViewPager.registerOnPageChangeCallback(mPageChangeCallback!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPageChangeCallback?.let {
            mViewPager.unregisterOnPageChangeCallback(it)
        }
        mPageChangeCallback=null
    }

    inner class VpAdapter(act: FragmentActivity): FragmentStateAdapter(act) {
        private val fragments = mutableListOf<Fragment>()
        fun addFragment(frg:Array<Fragment>) {
            fragments.addAll(frg)
        }
        override fun getItemCount()=fragments.size
        override fun createFragment(position: Int)=fragments[position]
    }

    inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mTabLayout.currentTab = position
        }
    }

}