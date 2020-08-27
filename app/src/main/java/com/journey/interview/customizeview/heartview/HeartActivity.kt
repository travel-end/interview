package com.journey.interview.customizeview.heartview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import kotlinx.android.synthetic.main.activity_heart.*

/**
 * @By Journey 2020/8/27
 * @Description
 */
class HeartActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        setHeartBitmap()
        heart.setOnClickListener {
            heart.addHeart()
        }
        intervalAddHeart()
    }

    private fun intervalAddHeart() {
        toolbar.postDelayed( {
            heart.performClick()
            intervalAddHeart()
        },1000)
    }

    private fun setHeartBitmap() {
        val bitmapArray = SparseArray<Bitmap>()
        val bitmap1 = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_heart_0)
        val bitmap2 = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_heart_1)
        val bitmap3 = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_heart_2)
        val bitmap4 = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_heart_3)
        val bitmap5 = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_heart_4)
        val bitmap6 = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_heart_5)
        val bitmap7 = BitmapFactory.decodeResource(this.resources, R.mipmap.ic_heart_6)
        bitmapArray.put(HeartType.BLUE, bitmap1)
        bitmapArray.put(HeartType.GREEN, bitmap2)
        bitmapArray.put(HeartType.YELLOW, bitmap3)
        bitmapArray.put(HeartType.PINK, bitmap4)
        bitmapArray.put(HeartType.BROWN, bitmap5)
        bitmapArray.put(HeartType.PURPLE, bitmap6)
        bitmapArray.put(HeartType.RED, bitmap7)
        heart.setHeartBitmap(bitmapArray)
    }
}