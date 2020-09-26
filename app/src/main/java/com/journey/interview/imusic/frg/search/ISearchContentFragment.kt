package com.journey.interview.imusic.frg.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.imusic.model.ISearchContentViewModel
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_frg_search_content.*

/**
 * @By Journey 2020/9/26
 * @Description
 */
class ISearchContentFragment :BaseLifeCycleFragment<ISearchContentViewModel>(){
    private var mSearchContent:String = ""
    private var mSearchType :String = ""
    companion object {
        const val KEY_SEARCH_CONTENT= "key_search_content"
        private const val KEY_PAGE_TYPE = "key_search_type"
        fun newInstance(searchContent:String,searchType:String):Fragment {
            val fragment = ISearchContentFragment()
            val bundle = Bundle().apply {
                putString(KEY_PAGE_TYPE,searchType)
                putString(KEY_SEARCH_CONTENT,searchContent)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun layoutResId()= R.layout.imusic_frg_search_content

    override fun initView() {
        super.initView()
        arguments?.apply {
            mSearchContent = getString(KEY_SEARCH_CONTENT,"")
            mSearchType = getString(KEY_PAGE_TYPE,"")
        }
        tv_search_content.text = mSearchType
    }

    override fun initData() {
        super.initData()
        if (mSearchType == ISearchVpFragment.TYPE_SONG) {
//            mViewModel.searchSong(mSearchContent,1)
        } else {

        }
    }

    override fun dataObserve() {
        super.dataObserve()

    }
}