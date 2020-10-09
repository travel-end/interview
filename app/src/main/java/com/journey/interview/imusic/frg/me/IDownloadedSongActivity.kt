//package com.journey.interview.imusic.frg.me
//
//import androidx.fragment.app.Fragment
//import com.journey.interview.R
//import com.journey.interview.customizeview.tablayout.CustomTab
//import com.journey.interview.imusic.base.BaseVpActivity
//import com.journey.interview.imusic.frg.download.IDownloadedFragment
//import com.journey.interview.imusic.frg.download.IDownloadingFragment
//import com.journey.interview.imusic.model.IMusicTab
//import com.journey.interview.imusic.vm.IDownloadViewModel
//import com.journey.interview.utils.getString
//import kotlinx.android.synthetic.main.imusic_act_download_vp.*
//
///**
// * @By Journey 2020/10/9
// * @Description
// */
//class IDownloadedSongActivity:BaseVpActivity<IDownloadViewModel>() {
//    override val vpFragments: Array<Fragment>
//        get() = arrayOf(
//            IDownloadedFragment.newInstance(),
//            IDownloadingFragment.newInstance()
//        )
//    override val vpTitles = ArrayList<CustomTab>().apply {
//        add(IMusicTab(R.string.downloaded.getString()))
//        add(IMusicTab(R.string.downloading.getString()))
//    }
//
//    override fun layoutResId()= R.layout.imusic_act_download_vp
//    override fun initView() {
//        mViewPager = download_vp
//        mTabLayout = download_title_tabLayout
//    }
//}