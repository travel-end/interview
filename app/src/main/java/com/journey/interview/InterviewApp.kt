package com.journey.interview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDexApplication
import com.journey.interview.weatherapp.state.EmptyCallback
import com.journey.interview.weatherapp.state.ErrorCallback
import com.journey.interview.weatherapp.state.LoadingCallback
import com.kingja.loadsir.core.LoadSir

/**
 * @By Journey 2020/8/25
 * @Description
 */
class InterviewApp : MultiDexApplication(), ViewModelStoreOwner {
    private var factory: ViewModelProvider.Factory? = null
    lateinit var mViewModelStore: ViewModelStore

    companion object {
        lateinit var instance: InterviewApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mViewModelStore = ViewModelStore()
//        val intent = Intent(this,LockScreenService::class.java)
//        startService(intent)
        initLoadSir()

    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .commit()
    }

    private fun getGlobalViewModelProvider(): ViewModelProvider {
        if (factory == null) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return ViewModelProvider(this, factory as ViewModelProvider.Factory)
    }

    fun <T:ViewModel> getGlobalViewModel(clazz: Class<T>) :T{
        return getGlobalViewModelProvider().get(clazz)
    }

    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }
}