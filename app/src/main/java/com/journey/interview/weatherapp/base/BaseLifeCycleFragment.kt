package com.journey.interview.weatherapp.base

import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.journey.interview.R
import com.journey.interview.utils.getClass
import com.journey.interview.weatherapp.state.*
import com.kingja.loadsir.callback.SuccessCallback
import kotlinx.android.synthetic.main.imusic_loading_view.*

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseLifeCycleFragment<VM : BaseViewModel> : BaseFragment() {
    protected lateinit var mViewModel: VM
    private var loadingView: LinearLayout? = null
    override fun initView() {
        dataObserve()
    }

    override fun initData() {
    }

    open fun dataObserve() {
        mViewModel.loadState.observe(this, observer)
    }

    open fun showLoading() {
        if (loadingView == null) {
            loadingView = mRootView.findViewById(R.id.ll_loading_view)
        }
        loadingView?.visibility = View.VISIBLE
    }

    open fun dismissLoading() {
        if (loadingView?.visibility == View.VISIBLE) {
            loadingView?.visibility = View.GONE
        }
    }

    open fun showSuccess() {
    }

    open fun showError(msg: String) {
        dismissLoading()
        if (msg.isNotEmpty()) {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    open fun showEmpty(msg: String) {
        if (msg.isNotEmpty()) {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
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
                    StateType.DISMISSING -> dismissLoading()
                    StateType.ERROR -> showError("网络异常")
                    StateType.NETWORK_ERROR -> showError("网络异常")
                    StateType.EMPTY -> showEmpty(it.message)
                }
            }
        }
    }
}