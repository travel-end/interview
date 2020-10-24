package com.journey.interview.weatherapp.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.R
import com.journey.interview.utils.getClass

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    protected var handler: Handler? = null

    protected lateinit var mViewModel: VM
    open fun initView() {

    }

    open fun initData() {
        handler = Handler(Looper.getMainLooper())
    }

    open fun reLoad() {

    }

    abstract fun layoutResId(): Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        initStatusBar()
        initViewModel()
        initView()
        initData()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getClass(this))
    }

    open fun initStatusBar() {
        ImmersionBar
            .with(this)
            .statusBarView(R.id.top_view)
            .statusBarColor(R.color.colorWhite)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
    }
}