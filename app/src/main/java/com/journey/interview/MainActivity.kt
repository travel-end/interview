package com.journey.interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.journey.interview.customizeview.CustomizeViewActivity
import com.journey.interview.recyclerview.demo.EfficientAdapterActivity
import com.journey.interview.system.ScreenStatusController
import com.journey.interview.system.ScreenStatusListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var screenStatusController: ScreenStatusController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testEfficientAdapter.setOnClickListener {
            startActivity(Intent(this,EfficientAdapterActivity::class.java))
        }

        testCustomizeView.setOnClickListener {
            startActivity(Intent(this,CustomizeViewActivity::class.java))
        }


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