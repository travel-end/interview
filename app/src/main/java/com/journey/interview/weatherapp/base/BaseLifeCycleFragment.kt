package com.journey.interview.weatherapp.base

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.journey.interview.R
import com.journey.interview.utils.getClass
import com.journey.interview.utils.getString
import com.journey.interview.weatherapp.state.EmptyState
import com.journey.interview.weatherapp.state.ErrorState
import com.journey.interview.weatherapp.state.State
import com.journey.interview.weatherapp.state.StateType

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseLifeCycleFragment<VM : BaseViewModel> : BaseFragment() {
    protected lateinit var mViewModel: VM
    private var loadingView: LinearLayout? = null
    private var emptyView: LinearLayout? = null
    private var errorView: LinearLayout? = null
    override fun initView() {
        if (emptyView == null) {
            emptyView = mRootView.findViewById(R.id.include_local_empty_view)
        }
        if (loadingView == null) {
            loadingView = mRootView.findViewById(R.id.ll_loading_view)
        }
        if (errorView == null) {
            errorView = mRootView.findViewById(R.id.ll_error_view)
        }
        dataObserve()
    }

    override fun initData() {
        errorView?.setOnClickListener {
            it?.let {
                reLoad()
            }
        }
    }

    open fun dataObserve() {
        mViewModel.loadState.observe(this, netWorkErrorObserver)
        mViewModel.emptyState.observe(this, emptyObserver)
        mViewModel.errorState.observe(this, errorObserver)
    }

    open fun showLoading() {
        loadingView?.visibility = View.VISIBLE
    }

    open fun dismissLoading() {
        if (loadingView?.visibility == View.VISIBLE) {
            loadingView?.visibility = View.GONE
        }
    }

    open fun showSuccess() {
    }

    open fun showError(errorRes: Int, msg: String, showErrorView: Boolean) {
        dismissLoading()
        if (showErrorView) {
            errorView?.visibility = View.VISIBLE
            val errorIv: ImageView? = errorView?.findViewById(R.id.error_resource)
            errorIv?.setImageResource(errorRes)
        }
        if (msg.isNotEmpty()) {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    open fun showNormalEmpty(msg: String) {
        if (msg.isNotBlank()) {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    open fun showEmpty(
        resource: Int = R.drawable.ic_xigua,
        msg: String = R.string.empty_common.getString()
    ) {
        emptyView?.visibility = View.VISIBLE
        val emptyLogo = emptyView?.findViewById<ImageView>(R.id.empty_resource)
        val emptyDesc = emptyView?.findViewById<TextView>(R.id.empty_description)
        emptyLogo?.setImageResource(resource)
        emptyDesc?.text = msg
    }

    open fun hideEmpty() {
        if (emptyView?.visibility == View.VISIBLE) {
            emptyView?.visibility = View.GONE
        }
    }

    open fun hideError() {
        if (errorView?.visibility == View.VISIBLE) {
            errorView?.visibility = View.GONE
        }
    }

    override fun reLoad() {
        hideError()
        initData()
        dataObserve()
        super.reLoad()
    }

    override fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getClass(this))
    }

    private val netWorkErrorObserver by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.DISMISSING -> dismissLoading()
                    StateType.EMPTY -> showNormalEmpty(it.message)
                }
            }
        }
    }
    private val emptyObserver by lazy {
        Observer<EmptyState> {
            it?.let {
                showEmpty(it.resource, it.message)
            }
        }
    }
    private val errorObserver by lazy {
        Observer<ErrorState> {
            it?.let { error ->
                showError(error.resource, error.message, error.showErrorIcon)
            }
        }
    }
}