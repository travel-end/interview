package com.journey.interview.customizeview.cropimageview.demo

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journey.interview.R
import com.journey.interview.customizeview.cropimageview.core2.CoordinatorLinearLayout2
import com.journey.interview.customizeview.cropimageview.core2.CoordinatorRecyclerView2
import com.journey.interview.recyclerview.core.addItem
import com.journey.interview.recyclerview.core.itemClicked
import com.journey.interview.recyclerview.core.setImageResource
import com.journey.interview.recyclerview.core.setup
import kotlinx.android.synthetic.main.activity_crop2.*

/**
 * @By Journey 2020/8/26
 * @Description
 */
class CropImageViewActivity2:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop2)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val mRecyclerView = recycler
        mRecyclerView.layoutManager = GridLayoutManager(this,4)
        val l = mRecyclerView.setup<Int> {
            dataSource(initData())
            adapter {
                addItem(R.layout.item_coordinator_layout) {
                    bindViewHolder{data,pos,_->
                        val resId = if (data == null || data == 0) R.mipmap.ic_gril2 else R.mipmap.ic_gril
                        setImageResource(R.id.iv,resId)
                        itemClicked(View.OnClickListener {
                            val resId2 = if (data == null || data == 0) R.mipmap.ic_gril2 else R.mipmap.ic_gril
                            crop_view.setImageRes(resId2)
                            Toast.makeText(it.context,"position:${getItem(pos)}",Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            }
        }

        mRecyclerView.setOnCoordinatorListener(object :CoordinatorRecyclerView2.OnCoordinatorListener{
            override fun handlerInvalidClick(rawX: Int, rawY: Int) {
                handlerRecyclerInvalidClick(mRecyclerView,rawX,rawY)
            }

            override fun onScroll(y: Float, deltaY: Float, maxParentScrollRange: Int) {
                coordinator_layout.onScroll(y,deltaY,maxParentScrollRange)
            }

            override fun onFiling(velocityY: Int) {
                coordinator_layout.onFiling(velocityY)
            }

        })

        coordinator_layout.setOnScrollListener(object :CoordinatorLinearLayout2.OnScrollListener{
            override fun onScroll(scrollY: Int) {
                mRecyclerView.setCurrentParenScrollY(scrollY)
            }

            override fun isExpand(isExpand: Boolean) {
                mRecyclerView.setExpand(isExpand)
            }

            override fun completeExpand() {
                mRecyclerView.resetRecyclerHeight()
            }

        })

    }

    private fun handlerRecyclerInvalidClick(recyclerView: RecyclerView?,touchX:Int,touchY:Int) {
        if (recyclerView != null && recyclerView.childCount > 0) {
            for (i in 0 until recyclerView.childCount) {
                val childView = recyclerView.getChildAt(i)
                if (childView != null) {
                    if (isTouchView(touchX, touchY, childView)) {
                        childView.performClick()
                        return
                    }
                }
            }
        }
    }

    // 触摸点是否view区域内 parent.isPointInChildBounds(child, x, y)
    private fun isTouchView(
        touchX: Int,
        touchY: Int,
        view: View
    ): Boolean {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        return rect.contains(touchX, touchY)
    }

    private fun initData():MutableList<Int>{
        val list :MutableList<Int> = mutableListOf()
        for (i in 0..99) {
            list.add(i)
        }
        return list
    }
}