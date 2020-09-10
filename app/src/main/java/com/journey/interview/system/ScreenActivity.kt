package com.journey.interview.system

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R

/**
 * @By Journey 2020/9/10
 * @Description 监听锁屏
 */
class ScreenActivity:AppCompatActivity() {
    private lateinit var screenStatusController: ScreenStatusController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_test)
        listenScreenStatus()
    }

    private fun listenScreenStatus() {
        screenStatusController = ScreenStatusController(this)
        screenStatusController.setScreenStatusListener(object : ScreenStatusListener {
            override fun onScreenOn() {
                Log.e("JG","----->onScreenOn")
            }
            override fun onScreenOff() {
                Log.e("JG","----->onScreenOff")
                val lockIntent = Intent(this@ScreenActivity,LockScreenActivity::class.java)
                startActivity(lockIntent)
            }
            override fun userPresent() {
                Log.e("JG","----->userPresent")
            }
        })
        screenStatusController.startListen()
    }

    override fun onDestroy() {
        super.onDestroy()
        screenStatusController.stopListen()
    }

}