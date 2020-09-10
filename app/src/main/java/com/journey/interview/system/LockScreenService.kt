package com.journey.interview.system

import android.app.KeyguardManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class LockScreenService : Service() {
    private lateinit var screenStatusController: ScreenStatusController

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        screenStatusController = ScreenStatusController(this)
        screenStatusController.setScreenStatusListener(object : ScreenStatusListener {
            override fun onScreenOn() {
                Log.e("JG","----->onScreenOn")
            }
            override fun onScreenOff() {
                /**
                 * 关于启动Activity时Intent的Flag问题，如果不添加FLAG_ACTIVITY_NEW_TASK的标志位，
                 * 会出现“Calling startActivity() from outside of an Activity”的运行时异常，
                 * 毕竟我们是从Service启动的Activity。Activity要存在于activity的栈中，而Service在
                 * 启动activity时必然不存在一个activity的栈，所以要新起一个栈，并装入启动的activity。
                 * 使用该标志位时，也需要在AndroidManifest中声明taskAffinity，即新task的名称，否则锁屏Activity实质上还是在建立在原来App的task栈中。
                 * 标志位FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS，是为了避免在最近使用程序列表出现Service
                 * 所启动的Activity,但这个标志位不是必须的，其使用依情况而定。
                 */


                Log.e("JG","----->onScreenOff")
                val lockIntent = Intent(this@LockScreenService,LockScreenActivity::class.java)
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                startActivity(lockIntent)
            }
            override fun userPresent() {
                Log.e("JG","----->userPresent")
                val km =  getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (km.isKeyguardSecure) {// 锁屏密码的情况下
                        /**
                         * 如果是指纹解锁的情况下，发送广播，将锁屏页finish调
                         */
                        val i = Intent("NOTIFY_USER_PRESENT")
                        sendBroadcast(i)
                    }
                }
            }
        })
        screenStatusController.startListen()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        screenStatusController.stopListen()
    }

}
