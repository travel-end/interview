package com.journey.interview.weatherapp.base

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.journey.interview.utils.getClass
import com.journey.interview.weatherapp.state.*
import com.kingja.loadsir.callback.SuccessCallback

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseLifeCycleFragment<VM:BaseViewModel>:BaseFragment() {
    protected lateinit var mViewModel:VM
    override fun initView() {

        dataObserve()
    }

    override fun initData() {

    }

    open fun dataObserve() {
        mViewModel.loadState.observe(this,observer)
    }
    open fun showLoading() {
//        mLoadService.showCallback(LoadingCallback::class.java)
    }

    open fun showSuccess() {
//        mLoadService.showCallback(SuccessCallback::class.java)
    }

    open fun showError(msg:String) {
        if (msg.isNotEmpty()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
//        mLoadService.showCallback(ErrorCallback::class.java)
    }

    open fun showEmpty() {
//        mLoadService.showCallback(EmptyCallback::class.java)
    }

    override fun reLoad() {
        showLoading()
        initData()
        dataObserve()
        super.reLoad()
    }

    override fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getClass(this))
    }

    private val observer by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.ERROR -> showError("网络异常")
                    StateType.NETWORK_ERROR -> showError("网络异常")
                    StateType.EMPTY -> showEmpty()
                }
            }
        }
    }
}