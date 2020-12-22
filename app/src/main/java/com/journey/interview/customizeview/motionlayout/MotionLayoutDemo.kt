package com.journey.interview.customizeview.motionlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.utils.openActivity
import kotlinx.android.synthetic.main.activity_motion_layout_sample.*

/**
 * @By Journey 2020/12/22
 * @Description
 */
class MotionLayoutDemo:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout_sample)
        btnHWTel.setOnClickListener {
            openActivity<HuaweiTelActivity>()
        }
    }
}