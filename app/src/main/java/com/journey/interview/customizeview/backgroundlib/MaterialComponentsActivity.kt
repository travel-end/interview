package com.journey.interview.customizeview.backgroundlib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.shape.AbsoluteCornerSize
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import com.journey.interview.R
import kotlinx.android.synthetic.main.activity_material.*

/**
 * @By Journey 2020/9/14
 * @Description MDC 1.2新功能示例
 */
class MaterialComponentsActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material)
        setImageCorners()
    }

    /**
     * 可以为imageview设置各种各样的边框  如圆形 四方形  不规则等等
     */
    private fun setImageCorners() {
        iv1?.shapeAppearanceModel = ShapeAppearanceModel.builder()
//            .setAllCorners(CornerFamily.ROUNDED,20f)
//            .setTopLeftCorner(CornerFamily.CUT, RelativeCornerSize(0.3f))
//            .setTopRightCorner(CornerFamily.CUT,RelativeCornerSize(0.3f))
//            .setBottomRightCorner(CornerFamily.CUT,RelativeCornerSize(0.3f))
//            .setBottomLeftCorner(CornerFamily.CUT,RelativeCornerSize(0.3f))
            .setAllCornerSizes(ShapeAppearanceModel.PILL)
//            .setTopLeftCornerSize(20f)
//            .setTopRightCornerSize(RelativeCornerSize(0.5f))
//            .setBottomLeftCornerSize(10f)
//            .setBottomRightCornerSize(AbsoluteCornerSize(30f))
            .build()
    }


}