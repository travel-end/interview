package com.journey.interview.system

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R

/**
 * @By Journey 2020/9/10
 * @Description 锁屏页
 */
class LockScreenActivity : AppCompatActivity() {

    companion object {
        const val MSG_LAUNCH_HOME = 0
    }

    inner class MainHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MSG_LAUNCH_HOME) {
                Log.i("JG", "LockScreenActivity--->finish")
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_lock)
        disableSystemScreen2()
        hideNavBar()
        setTranslucentStatusBar()
        val inFil = IntentFilter("NOTIFY_USER_PRESENT")
        registerReceiver(finishActBroadcastReceiver, inFil)
        val moveView = findViewById<ImageView>(R.id.moveView)
        val underView = findViewById<UnderView>(R.id.underView)
        val mainHandler = MainHandler()
        underView.setMoveView(moveView)
        underView.setMainHandler(mainHandler)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNavBar()
        }
    }

    // 去掉系统锁屏页方法  已经不推荐使用
    private fun disableSystemScreen() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        val keyGuardLock = keyguardManager.newKeyguardLock("CustomLockScreen")
        keyGuardLock.disableKeyguard()

    }

    // 去掉系统锁屏页方法
    private fun disableSystemScreen2() {
        // 去掉系统锁屏页
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        // 使activity在锁屏时仍然能够显示
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
    }

    /**
     * 屏蔽按键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        event?.let { e ->
            return when (e.keyCode) {
                KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_MENU -> true
                else -> super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(finishActBroadcastReceiver)
    }

    /**
     * 隐藏导航栏
     */
    private fun hideNavBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    /**
     * 设置透明状态栏
     */
    private fun setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.run {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = 0
            }
        }
    }

    private val finishActBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "NOTIFY_USER_PRESENT") {
                    Log.i("JG", "finishActBroadcastReceiver--->NOTIFY_USER_PRESENT")
                    this@LockScreenActivity.finish()
                }
            }
        }

    }
}