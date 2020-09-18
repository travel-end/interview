package com.journey.interview.system.scopestore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import kotlinx.android.synthetic.main.activity_test_android_r.*

/**
 * @By Journey 2020/9/16
 * @Description
 */
class TestAndroid11Activity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_android_r)
        tvTest.setOnClickListener {

        }
    }

    private fun saveFile() {

    }
}