package com.journey.interview.imusic.listener

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * @By Journey 2020/10/22
 * @Description
 */
abstract class AppBarStateChangeListener:AppBarLayout.OnOffsetChangedListener {
    private var mCurrentState = BarState.IDLE
    enum class BarState {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        onOffsetChanged(appBarLayout)
        if (verticalOffset==0) {
            if (mCurrentState != BarState.EXPANDED) {
                onStateChanged(appBarLayout,BarState.EXPANDED)
            }
            mCurrentState = BarState.EXPANDED
        } else if (abs(verticalOffset) >= appBarLayout?.totalScrollRange?:0) {
            if (mCurrentState != BarState.COLLAPSED) {
                onStateChanged(appBarLayout,BarState.COLLAPSED)
            }
            mCurrentState = BarState.COLLAPSED
        } else {
            if (mCurrentState != BarState.IDLE) {
                onStateChanged(appBarLayout,BarState.IDLE)
            }
            mCurrentState = BarState.IDLE
        }
    }

    abstract fun onOffsetChanged(appBarLayout: AppBarLayout?)

    abstract fun onStateChanged(appBarLayout: AppBarLayout?,state:BarState)
}