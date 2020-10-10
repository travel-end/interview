package com.journey.interview.weatherapp.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.InterviewApp
import com.journey.interview.R
import com.journey.interview.utils.decorView
import com.journey.interview.utils.getClass

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseActivity<VM:BaseViewModel>:AppCompatActivity() {
    protected lateinit var mViewModel:VM
    open fun initView() {

    }
    open fun initData() {

    }
    open fun reLoad() {

    }
    abstract fun layoutResId():Int
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
            .statusBarDarkFont(true)
            .init()
    }

    protected fun hideStatusBar(isHide:Boolean) {
        if (isHide) {
            if (Build.VERSION.SDK_INT >= 22) {
                decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        } else {
            decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window?.statusBarColor = InterviewApp.instance.resources.getColor(R.color.actionBarColor)
            }
        }
    }
}