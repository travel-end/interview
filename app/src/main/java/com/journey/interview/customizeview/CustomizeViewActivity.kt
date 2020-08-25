package com.journey.interview.customizeview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.customizeview.cornergif.CornersGifActivity
import com.journey.interview.customizeview.rounddragtag.demo.RandomDragTagActivity
import com.journey.interview.customizeview.swipecaptcha.demo.SwipeCaptchaActivity
import com.journey.interview.customizeview.spiderweb.demo.MeiSpiderWebActivity2
import kotlinx.android.synthetic.main.activity_customize_view.*

/**
 * @By Journey 2020/8/24
 * @Description
 */
class CustomizeViewActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_view)
        testSpiderNetView.setOnClickListener {
            startActivity(Intent(this, MeiSpiderWebActivity2::class.java))
        }
        testSwipeCaptchaView.setOnClickListener {
            startActivity(Intent(this, SwipeCaptchaActivity::class.java))
        }
        testCornersGifView.setOnClickListener {
            startActivity(Intent(this, CornersGifActivity::class.java))
        }
        testRandomDragView.setOnClickListener {
            startActivity(Intent(this, RandomDragTagActivity::class.java))
        }
    }
}