package com.journey.interview

import android.app.Application

/**
 * @By Journey 2020/8/25
 * @Description
 */
class InterviewApp:Application() {
    companion object{
        lateinit var instance:InterviewApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}