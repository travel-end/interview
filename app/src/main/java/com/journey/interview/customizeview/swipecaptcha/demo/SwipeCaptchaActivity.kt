package com.journey.interview.customizeview.swipecaptcha.demo

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.customizeview.swipecaptcha.core.GlideUtil
import com.journey.interview.customizeview.swipecaptcha.core.SwipeCaptchaView2
import kotlinx.android.synthetic.main.activity_swipe_demo.*

/**
 * @By Journey 2020/8/21
 * @Description 仿斗鱼滑动验证码
 */
class SwipeCaptchaActivity:AppCompatActivity() {
//    private val url = "https://frogx-ieasyloan-person-profile.oss-ap-south-1.aliyuncs.com/AADHAAR_CARD/20200817/121518eb9a9f22-bca2-4d23-9ed9-26d1d0ecb7ce.jpg"
    private val url = "https://icashnow-resource-open.oss-ap-south-1.aliyuncs.com/third-app-logos/iEasyLoan.png"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_demo)
        btnChange.setOnClickListener {
            swipeCaptchaView.createCaptcha()
            dragBar.isEnabled = true
            dragBar.progress = 0
        }
        swipeCaptchaView.setOnCaptchaMatchCallback(object :
            SwipeCaptchaView2.OnCaptchaMatchCallback{
            override fun matchSuccess(swipeCaptchaView: SwipeCaptchaView2?) {
                Toast.makeText(this@SwipeCaptchaActivity, "恭喜你啊 验证成功 可以搞事情了", Toast.LENGTH_SHORT).show();
                dragBar.isEnabled = false
            }

            override fun matchFailed(swipeCaptchaView: SwipeCaptchaView2?) {
                Log.d("zxt", "matchFailed() called with: swipeCaptchaView = [$swipeCaptchaView]");
                Toast.makeText(this@SwipeCaptchaActivity, "你有80%的可能是机器人，现在走还来得及", Toast.LENGTH_SHORT).show();
                swipeCaptchaView?.resetCaptcha()
                dragBar.progress = 0
            }
        })
        dragBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                swipeCaptchaView.setCurrentSwipeValue(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                dragBar.max = swipeCaptchaView.maxSwipeValue
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [$p0]");
                swipeCaptchaView.matchCaptcha()
            }

        })
        //测试从网络加载图片是否ok
        GlideUtil.loadImg(this,url,swipeCaptchaView,R.drawable.ic_default,R.drawable.ic_default,object :
            GlideUtil.OnGlideLoadReady{
            override fun ready(isOk: Boolean) {
                swipeCaptchaView.createCaptcha()
            }
        })

    }
}