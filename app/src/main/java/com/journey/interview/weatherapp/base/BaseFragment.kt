package com.journey.interview.weatherapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseFragment : Fragment() {
    protected lateinit var mRootView: View
    abstract fun layoutResId(): Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(layoutResId(), container, false)
        return mRootView
    }

    /**
     * onViewCreated：紧接着onCreateView后调用
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        initView()
        initData()
    }

    open fun reLoad() = initData()

    open fun initView() {
    }

    open fun initData() {
    }

    open fun initViewModel() {
    }
}