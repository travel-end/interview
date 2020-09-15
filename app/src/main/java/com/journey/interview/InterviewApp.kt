package com.journey.interview

import androidx.multidex.MultiDexApplication
import com.journey.interview.weatherapp.state.EmptyCallback
import com.journey.interview.weatherapp.state.ErrorCallback
import com.journey.interview.weatherapp.state.LoadingCallback
import com.kingja.loadsir.core.LoadSir

/**
 * @By Journey 2020/8/25
 * @Description
 */
class InterviewApp:MultiDexApplication() {
    companion object{
        lateinit var instance:InterviewApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
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
}