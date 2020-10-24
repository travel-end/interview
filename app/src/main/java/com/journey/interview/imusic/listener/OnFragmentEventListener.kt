package com.journey.interview.imusic.listener

import android.view.View

/**
 * @By Journey 2020/10/24
 * @Description
 */
interface OnFragmentEventListener {
    fun sendEvent(view: View?, eventType:Int)
}