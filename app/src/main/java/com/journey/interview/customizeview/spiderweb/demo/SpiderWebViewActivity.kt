package com.journey.interview.customizeview.spiderweb.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.journey.interview.R
import kotlinx.android.synthetic.main.activity_spider_view.*
import kotlinx.android.synthetic.main.layout_left_drawer.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * @By Journey 2020/8/24
 * @Description 五彩蛛网
 */
class SpiderWebViewActivity:AppCompatActivity(),View.OnTouchListener,SeekBar.OnSeekBarChangeListener {
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spider_view)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerToggle = ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.drawer_open,R.string.drawer_close)
        mDrawerToggle.syncState()
        initListener()
        seekbar1.progress = 49
        seekbar2.progress = 4
        seekbar3.progress = 250
        seekbar4.progress = 150
        seekbar5.progress = 10
        seekbar6.progress = 12
        seekbar7.progress = 1
        seekbar8.progress = 50
    }

    private fun initListener() {
        toolbar.setOnMenuItemClickListener(object :Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if (item != null) {
                    if (item.itemId == R.id.menu_item1) {
                        cob_web_view.restart()
                        return true
                    }
                }
                return false
            }
        })
        seekbar1.setOnTouchListener(this)
        seekbar2.setOnTouchListener(this)
        seekbar3.setOnTouchListener(this)
        seekbar4.setOnTouchListener(this)
        seekbar5.setOnTouchListener(this)
        seekbar6.setOnTouchListener(this)
        seekbar7.setOnTouchListener(this)
        seekbar8.setOnTouchListener(this)
        seekbar1.setOnSeekBarChangeListener(this)
        seekbar2.setOnSeekBarChangeListener(this)
        seekbar3.setOnSeekBarChangeListener(this)
        seekbar4.setOnSeekBarChangeListener(this)
        seekbar5.setOnSeekBarChangeListener(this)
        seekbar6.setOnSeekBarChangeListener(this)
        seekbar7.setOnSeekBarChangeListener(this)
        seekbar8.setOnSeekBarChangeListener(this)
        drawer_layout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {
                cob_web_view.resetTouchPoint()
                mDrawerToggle.onDrawerStateChanged(newState)
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                mDrawerToggle.onDrawerSlide(drawerView,slideOffset)
            }

            override fun onDrawerClosed(drawerView: View) {
                mDrawerToggle.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)
        return true
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p0 is SeekBar) {
            drawer_layout.requestDisallowInterceptTouchEvent(true)
        }
        return false
    }

    override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
        p0?.let {seekBar ->
            when (seekBar.id) {
                R.id.seekbar1 -> {
                    cob_web_view.setPointNum(progress + 1)
                    textview1.text = String.format("小点数量： %d",cob_web_view.getPointNum())
                }
                R.id.seekbar2 -> {
                    cob_web_view.setPointAcceleration(progress + 3)
                    textview2.text = String.format(
                        "加速度： %d",
                        cob_web_view.getPointAcceleration()
                    )
                }
                R.id.seekbar3 -> {
                    cob_web_view.setMaxDistance(progress + 20)
                    textview3.text = String.format(
                        "最大连线距离： %d",
                        cob_web_view.getMaxDistance()
                    )
                }
                R.id.seekbar4 -> {
                    cob_web_view.setLineAlpha(progress)
                    textview4.text = String.format(
                        "连线透明度： %d",
                        cob_web_view.getLineAlpha()
                    )
                }
                R.id.seekbar5 -> {
                    cob_web_view.setLineWidth(progress)
                    textview5.text = String.format(
                        "连线粗细： %d",
                        cob_web_view.getLineWidth()
                    )
                }
                R.id.seekbar6 -> {
                    cob_web_view.setPointRadius(progress)
                    textview6.text = String.format(
                        "小点半径： %d",
                        cob_web_view.getPointRadius()
                    )
                }
                R.id.seekbar7 -> {
                    cob_web_view.setTouchPointRadius(progress)
                    textview7.text = String.format(
                        "触摸点半径： %d",
                        cob_web_view.getTouchPointRadius()
                    )
                }
                R.id.seekbar8 -> {
                    cob_web_view.setGravitationStrength(progress)
                    textview8.text = String.format("引力强度： %d", cob_web_view.getGravitationStrength())
                }
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}