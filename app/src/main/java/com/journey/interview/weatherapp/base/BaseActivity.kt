package com.journey.interview.weatherapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.journey.interview.utils.getClass
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

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
        initViewModel()
        initView()
        initData()
    }
//    protected val mLoadService: LoadService<*> by lazy {
//        LoadSir.getDefault().register(this) {
//            reLoad()
//        }
//    }
    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getClass(this))
    }
}