package com.journey.interview.weatherapp.base

import android.widget.Toast
import androidx.lifecycle.Observer
import com.journey.interview.weatherapp.state.*
import com.kingja.loadsir.callback.SuccessCallback
import kotlinx.coroutines.GlobalScope

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseLifeCycleActivity<VM:BaseViewModel<*>>:BaseActivity<VM>() {
    override fun initView() {
        showSuccess()
        mViewModel.loadState.observe(this,stateObserver)
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
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        mLoadService.showCallback(ErrorCallback::class.java)
    }
    open fun showEmpty() {
        mLoadService.showCallback(EmptyCallback::class.java)
    }
    /**
     * 分发应用状态
     */
    private val stateObserver by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.ERROR -> showError("网络出现问题啦")
                    StateType.NETWORK_ERROR -> showError("网络出现问题啦")
                    StateType.EMPTY -> showEmpty()
                }
            }
        }
    }


}