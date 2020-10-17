package com.journey.interview.weatherapp.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.journey.interview.R

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

//    open fun initStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                requireActivity().window.statusBarColor = ContextCompat.getColor(
//                    requireActivity(),
//                    R.color.always_white_text
//                )
//            }
//        }
//        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                ColorUtils.calculateLuminance(requireContext().getColor(R.color.always_white_text)) >= 0.5
//            } else {
//                TODO("VERSION.SDK_INT < M")
//            }
//        ) {
//            // 设置状态栏中字体的颜色为黑色
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requireActivity().window.decorView.systemUiVisibility =
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//        } else {
//            // 跟随系统
//            requireActivity().window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        }
//    }
}