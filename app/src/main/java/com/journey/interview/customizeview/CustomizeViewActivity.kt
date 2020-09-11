package com.journey.interview.customizeview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.customizeview.backgroundlib.BackgroundLibActivity
import com.journey.interview.customizeview.cornergif.CornersGifActivity
import com.journey.interview.customizeview.cropimageview.demo.CropImageViewActivity2
import com.journey.interview.customizeview.datetimer.demo.DateTimerPickerActivity
import com.journey.interview.customizeview.heartview.HeartActivity
import com.journey.interview.customizeview.levelselectview.LevelSelectActivity
import com.journey.interview.customizeview.rounddragtag.demo.RandomDragTagActivity
import com.journey.interview.customizeview.scrollparallax.ScrollParallaxActivity
import com.journey.interview.customizeview.swipecaptcha.demo.SwipeCaptchaActivity
import com.journey.interview.customizeview.spiderweb.demo.MeiSpiderWebActivity2
import com.journey.interview.customizeview.zpwdeditext.ZEditTextActivity
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
        testPwdEditText.setOnClickListener {
            startActivity(Intent(this, ZEditTextActivity::class.java))
        }
        testDateTimerPicker.setOnClickListener {
            startActivity(Intent(this, DateTimerPickerActivity::class.java))
        }
        testCropImageView.setOnClickListener {
            startActivity(Intent(this, CropImageViewActivity2::class.java))
        }
        testScrollParallax.setOnClickListener {
            startActivity(Intent(this, ScrollParallaxActivity::class.java))
        }
        testHeartView.setOnClickListener {
            startActivity(Intent(this, HeartActivity::class.java))
        }
        testLevelSelect.setOnClickListener {
            startActivity(Intent(this, LevelSelectActivity::class.java))
        }
        testBgLib.setOnClickListener {
            startActivity(Intent(this, BackgroundLibActivity::class.java))
        }
    }
}