package com.journey.interview.weatherapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.journey.interview.utils.getClass
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * @By Journey 2020/9/15
 * @Description
 */
abstract class BaseFragment<VM:BaseViewModel<*>> :Fragment(){
    protected lateinit var mLoadService: LoadService<*>
    protected lateinit var mViewModel:VM
    abstract fun layoutResId():Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutResId(),container,false)
        mLoadService = LoadSir.getDefault().register(view) {
            reLoad()
        }
        return mLoadService.loadLayout
    }

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

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getClass(this))
    }
}