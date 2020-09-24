package com.journey.interview.weatherapp.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.R
import com.journey.interview.utils.getClass
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseFragment<VM:BaseViewModel> :Fragment(){
//    protected lateinit var mLoadService: LoadService<*>
    protected lateinit var mViewModel:VM
    protected lateinit var mRootView:View
    abstract fun layoutResId():Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(layoutResId(),container,false)
//        mLoadService = LoadSir.getDefault().register(view) {
//            reLoad()
//        }
        return mRootView
//        return mLoadService.loadLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        initView()
        initData()
        initStatusBarColor()
//        ImmersionBar.with(requireActivity()).statusBarColor(R.color.colorWhite).init()
    }

    open fun reLoad() = initData()

    open fun initView() {

    }
    open fun initData() {

    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getClass(this))
    }

    private fun initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                requireActivity().window.statusBarColor = ContextCompat.getColor(
                    requireActivity(),
                    R.color.always_white_text
                )
            }
        }
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ColorUtils.calculateLuminance(requireContext().getColor(R.color.always_white_text)) >= 0.5
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            // 设置状态栏中字体的颜色为黑色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            // 跟随系统
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    open fun hideKeyboards() {
        // 当前焦点的 View
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}