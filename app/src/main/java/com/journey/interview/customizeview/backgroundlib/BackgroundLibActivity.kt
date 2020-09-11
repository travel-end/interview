package com.journey.interview.customizeview.backgroundlib

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.journey.interview.R
import com.journey.interview.utils.toFloatPx
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_bg_drawable.*

/**
 * @By Journey 2020/9/11
 * @Description
 */
class BackgroundLibActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bg_drawable)


        setBtnJump()
    }

    /**
     * btn_jump 在xml中可以设置有波纹触摸反馈的按钮 ，下面是用代码设置 同样可以达到新效果
     */
    private fun setBtnJump() {
        val drawable3 = DrawableCreator.Builder()
            .setCornersRadius(20f.toFloatPx())
            .setRipple(true,Color.parseColor("#71C671"))
            .setSolidColor(Color.parseColor("#7CFC00"))
            .setStrokeColor(Color.parseColor("#8c6822"))
            .setStrokeWidth(2f.toFloatPx())
            .build()
        btn_jump.background = drawable3

        btn_jump.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),10)
        }
    }
}