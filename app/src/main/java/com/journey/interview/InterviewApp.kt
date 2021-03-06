package com.journey.interview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDexApplication
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.imusic.util.AssetsUtil
import com.journey.interview.utils.PhoneUtil
import com.journey.interview.utils.SpUtil
import com.journey.interview.weatherapp.state.EmptyCallback
import com.journey.interview.weatherapp.state.ErrorCallback
import com.journey.interview.weatherapp.state.LoadingCallback
import com.kingja.loadsir.core.LoadSir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

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

        initData()
    }

    private fun initData() {
        if (SpUtil.getString(Constant.FIRST_LAUNCH).isEmpty()) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val recomSongs = AssetsUtil.loadRecommendSongs()
                    if (!recomSongs.isNullOrEmpty()) {
                        IMusicRoomHelper.addRecomSongs(recomSongs)
                        SpUtil.saveValue(Constant.FIRST_LAUNCH,"has_launch")
                    }
                }
            }
        }
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