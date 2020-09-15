package com.journey.interview.weatherapp.base

import android.widget.Toast
import com.journey.interview.weatherapp.state.EmptyCallback
import com.journey.interview.weatherapp.state.ErrorCallback
import com.journey.interview.weatherapp.state.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseLifeCycleFragment<VM:BaseViewModel<*>>:BaseFragment<VM>() {
    override fun initView() {
//        showLoading()

        dataObserve()
    }

    open fun dataObserve() {

    }
    open fun showLoading() {
        mLoadService.showCallback(LoadingCallback::class.java)
    }

    open fun showSuccess() {
        mLoadService.showCallback(SuccessCallback::class.java)
    }

    open fun showError(msg:String) {
        if (msg.isNotEmpty()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        mLoadService.showCallback(ErrorCallback::class.java)
    }

    open fun showEmpty() {
        mLoadService.showCallback(EmptyCallback::class.java)
    }

    override fun reLoad() {
        showLoading()
        initData()
        dataObserve()
        super.reLoad()
    }
}