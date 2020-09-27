package com.journey.interview.imusic.frg.search

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.journey.interview.R
import com.journey.interview.imusic.vm.ISearchViewModel
import com.journey.interview.utils.hideKeyboards
import com.journey.interview.utils.showKeyBoard
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_search.*

/**
 * @By Journey 2020/9/25
 * @Description 搜索总页面
 */
class ISearchFragment : BaseLifeCycleFragment<ISearchViewModel>() {
    override fun layoutResId() = R.layout.imusic_search
    override fun initView() {
        super.initView()
        replaceFragment(IHotSearchFragment.newInstance())
    }

    override fun initData() {
        super.initData()
        search_et.showKeyBoard(requireContext())
        search_et.doAfterTextChanged {
            val content = it?.toString()
            content?.let {s->
                if (s.isNotEmpty()) {
                    replaceFragment(ISearchVpFragment.newInstance(s))
                    requireActivity().hideKeyboards()
                }
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(
            R.id.search_container,
            fragment
        ).commitAllowingStateLoss()

    }
}