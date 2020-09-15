package com.journey.interview.handle

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

/**
 * @By Journey 2020/9/14
 * @Description
 * 参考：https://mp.weixin.qq.com/s/H8mHoYHyTe6oOaUwfYh6_g
 */
class HandlerActivity : AppCompatActivity() {
    private var mHandler: Handler? = null

    //    private val mHandler=MyHandler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * 简单使用
         * 1、在主线程中实现一个Handler，然后再子线程中使用它
         */
        // 在子线程中通过自定义的Handler发消息
//        thread {
//            mHandler.sendEmptyMessageDelayed(1,1000)
//        }

        /**
         * 2、有时候需要在子线程中创建运行在主线程中的Handler
         */
        thread {
            // 获得main looper 运行在主线程
            mHandler = MyHandler(Looper.getMainLooper())
            mHandler?.sendEmptyMessageDelayed(1, 1000)
        }

        /**
         * 关于looper：Handler中的handleMessage方法具体运行在哪个线程
         * 是和Looper息息相关的。那么Looper是怎么做到线程切换的呢？
         */
        // 示例图：Handler在Java层的流程示意图（image/Handler流程示意图）
    }


    // 自定义一个Handler
    class MyHandler(mainLooper: Looper) : Handler(mainLooper) {
        override fun handleMessage(msg: Message) {
            Log.i("HandlerActivity", "---->主线程：handleMessage:${msg.what}")

        }
    }
}