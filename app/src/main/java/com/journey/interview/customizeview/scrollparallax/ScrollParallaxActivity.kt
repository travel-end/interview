package com.journey.interview.customizeview.scrollparallax

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.recyclerview.core.addItem
import com.journey.interview.recyclerview.core.setup
import kotlinx.android.synthetic.main.activity_scroll_parallax.*

/**
 * @By Journey 2020/8/27
 * @Description
 */
class ScrollParallaxActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_parallax)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val mData :MutableList<String> = mutableListOf()
        for (i in 0..65) {
            mData.add("")
        }
        recycler_view.setup<String> {
            dataSource(mData)
            adapter {
                addItem(R.layout.item_scroll_parallax) {

                }
            }
        }

    }
}