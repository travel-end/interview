package com.journey.interview.system

/**
 * @By Journey 2020/9/9
 * @Description
 */
interface ScreenStatusListener {
    fun onScreenOn()
    fun onScreenOff()
    fun userPresent()
}