package com.journey.interview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.customizeview.CustomizeViewActivity
import com.journey.interview.recyclerview.demo.EfficientAdapterActivity
import com.journey.interview.weatherapp.ui.WeatherActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testEfficientAdapter.setOnClickListener {
            startActivity(Intent(this, EfficientAdapterActivity::class.java))
        }

        testCustomizeView.setOnClickListener {
            startActivity(Intent(this, CustomizeViewActivity::class.java))
        }
        weatherApp.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
        }
//        testSystemFunction.setOnClickListener {
//            startActivity(Intent(this,ScreenActivity::class.java))
//        }

    }

}