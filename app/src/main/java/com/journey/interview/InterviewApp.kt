package com.journey.interview

import android.app.Application
import android.content.Intent
import com.journey.interview.system.LockScreenService

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
        val intent = Intent(this,LockScreenService::class.java)
        startService(intent)
    }
}