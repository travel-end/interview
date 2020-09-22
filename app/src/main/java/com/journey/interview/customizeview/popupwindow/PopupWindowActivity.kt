package com.journey.interview.customizeview.popupwindow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R

/**
 * @By Journey 2020/9/22
 * @Description
 */
class PopupWindowActivity:AppCompatActivity() {
    private lateinit var popupWindow: CommonPopupWindow
    private lateinit var rootView:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popupwindow)
        rootView = findViewById(R.id.root_view)
        initPop()
    }

    private fun initPop() {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_select_pic,null)
        view.findViewById<LinearLayout>(R.id.linear_cancle).setOnClickListener {
            popupWindow.dismiss()
        }
        popupWindow = CommonPopupWindow.Builder(this)
            .setView(view)
            .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            .setOutsideTouchable(true)
            .setAnimationStyle(R.style.pop_animation)
            .setViewOnclickListener(object :CommonPopupWindow.PopItemListener{
                override fun getChildView(view: View) {
                    Log.v("JG","view:$view")
                }
            })
            .create()
    }
    fun bottomPop(view:View) {
        popupWindow.showBottom(rootView,0.5f)
    }
    fun upPop(view:View) {
        popupWindow.showUpView(rootView,0.2f)
    }
    fun leftPop(view:View) {
        popupWindow.showLeftView(rootView,0.5f)
    }
    fun rightPop(view:View) {
        popupWindow.showRightView(rootView,0.7f)
    }
}