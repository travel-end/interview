package com.journey.interview.customizeview.rounddragtag.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.customizeview.rounddragtag.TagModel
import com.journey.interview.customizeview.rounddragtag.core.RandomDragTagLayout
import com.journey.interview.customizeview.rounddragtag.core.RandomDragTagView
import kotlinx.android.synthetic.main.activity_drag_tag.*
import kotlin.random.Random

/**
 * @By Journey 2020/8/25
 * @Description
 */
class RandomDragTagActivity:AppCompatActivity() {
    private val mTagList:MutableList<TagModel> = mutableListOf()
    private lateinit var mTagLayout:RandomDragTagLayout
    companion object{
        const val mTagText = "仿小红书任意拖拽控件、欢迎关注公众号：控件人生"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_tag)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        mTagLayout = tag_layout
        val random = Random.Default
        tv_add.setOnClickListener {
            val tagText = mTagText.substring(0, random.nextInt(mTagText.length))
            val tranX = random.nextFloat()
            val tranY = random.nextFloat()
            Log.d("JG","tagText=$tagText , tranX=$tranX ,tranY=$tranY")
            mTagLayout.addTagView(
                tagText,
                tranX,
                tranY,
                random.nextBoolean()
            )
        }
        tv_save.setOnClickListener {
            saveTag()
        }
        tv_restore.setOnClickListener {
            restoreTag()
        }
    }
    private fun saveTag() {
        mTagList.clear()
        for (i in 0.. mTagLayout.childCount) {
            val childView=  mTagLayout.getChildAt(i)
            if (childView is RandomDragTagView) {
//                val tagView = childView as RandomDragTagView
                val tagModel = TagModel(
                    childView.getPercentTransX(),
                    childView.getPercentTransY(),
                    childView.getTagText()?:"",
                    childView.isShowLeftView()

                )
                mTagList.add(tagModel)
            }
        }
    }

    private fun restoreTag() {
        if (mTagList.isNotEmpty()) {
            mTagLayout.removeAllViews()
            mTagList.forEach {model->
                mTagLayout.addTagView(model.text,model.x,model.y,model.direction)
            }
        }
    }
}