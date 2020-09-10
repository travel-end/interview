package com.journey.interview.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * @By Journey 2020/9/9
 * @Description
 */
class ScreenStatusController(context: Context) {
    private var mContext: Context? = context
    private var mScreenStatusFilter: IntentFilter? = null
    private var mScreenStatusListener: ScreenStatusListener? = null
    init {
        mScreenStatusFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_USER_PRESENT)
        }
    }
    private val mScreenStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1?.action
            if (action != null) {
                when (action) {
                    Intent.ACTION_SCREEN_ON -> {
                        mScreenStatusListener?.onScreenOn()
                    }
                    Intent.ACTION_SCREEN_OFF -> {
                        mScreenStatusListener?.onScreenOff()
                    }
                    Intent.ACTION_USER_PRESENT -> {
                        mScreenStatusListener?.userPresent()
                    }
                }

            }

        }

    }
    // 设置监听
    fun setScreenStatusListener(l: ScreenStatusListener) {
        this.mScreenStatusListener = l
    }
    // 开始监听
    fun startListen() {
        mContext?.registerReceiver(mScreenStatusReceiver, mScreenStatusFilter)
    }
    fun stopListen() {
        mContext?.unregisterReceiver(mScreenStatusReceiver)
    }


}