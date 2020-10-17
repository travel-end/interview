package com.journey.interview.weatherapp.base

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.journey.interview.R
import com.journey.interview.weatherapp.state.*

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseLifeCycleActivity<VM:BaseViewModel>:BaseActivity<VM>() {
    private var emptyView: LinearLayout?=null
    override fun initView() {
        mViewModel.loadState.observe(this,stateObserver)
        mViewModel.emptyState.observe(this, emptyObserver)
        dataObserve()
    }

    open fun dataObserve() {

    }

    open fun showLoading() {
    }
    open fun showSuccess() {
    }
    open fun dismissLoading() {

    }

    open fun showError(msg:String) {
        if (msg.isNotEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
    open fun showEmpty() {
    }

    open fun showEmpty(resource:Int,msg:String) {
        if (emptyView == null) {
            emptyView = findViewById(R.id.include_local_empty_view)
        }
        emptyView?.visibility = View.VISIBLE
        val emptyLogo = emptyView?.findViewById<ImageView>(R.id.empty_resource)
        val emptyDesc =  emptyView?.findViewById<TextView>(R.id.empty_description)
        emptyLogo?.setImageResource(resource)
        emptyDesc?.text = msg
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
                    StateType.DISMISSING->dismissLoading()
                }
            }
        }
    }
    private val emptyObserver by lazy {
        Observer<EmptyState> {
            it?.let {
                showEmpty(it.resource,it.message)
            }
        }
    }

}