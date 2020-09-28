package com.journey.interview.imusic.frg.search

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.customizeview.tablayout.CustomTab
import com.journey.interview.imusic.base.BaseVpFragment
import com.journey.interview.imusic.model.IMusicTab
import com.journey.interview.utils.getString
import kotlinx.android.synthetic.main.imusic_frg_search_vp.*

/**
 * @By Journey 2020/9/26
 * @Description
 */
class ISearchVpFragment : BaseVpFragment() {
    private var searchContent: String? = null

    companion object {
        const val TYPE_SONG = "song"
        const val TYPE_ALBUM = "album"
        fun newInstance(searchContent: String): Fragment {
            val fragment = ISearchVpFragment()
            fragment.arguments = Bundle().apply {
                putString(ISearchContentFragment.KEY_SEARCH_CONTENT, searchContent)
            }
            return fragment
        }
    }

    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            ISearchContentFragment.newInstance(searchContent ?: "", TYPE_SONG),
            ISearchContentFragment.newInstance(searchContent ?: "", TYPE_ALBUM)
        )
    override val vpTitles = ArrayList<CustomTab>().apply {
        add(IMusicTab(R.string.imusic_tab_song.getString()))
        add(IMusicTab(R.string.imusic_tab_album.getString()))
    }

    override fun layoutResId() = R.layout.imusic_frg_search_vp

    override fun initView() {
        searchContent = arguments?.getString(ISearchContentFragment.KEY_SEARCH_CONTENT)
//        super.initView()
        mTabLayout = search_title_tabLayout
        mViewPager = search_imusic_main_pager
    }
}