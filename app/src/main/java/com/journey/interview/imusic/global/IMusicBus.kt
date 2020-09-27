package com.journey.interview.imusic.global

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.journey.interview.InterviewApp

/**
 * @By Journey 2020/9/27
 * @Description
 */
object IMusicBus {

    private val musicEvent = InterviewApp.instance.getGlobalViewModel(IMusicEventViewModel::class.java)

    fun sendPlayStatusChangeEvent(status:Int) {
        musicEvent.songStatus.value = status
    }

    fun observePlayStatusChange(o: LifecycleOwner, block:(Int)->Unit) {
        musicEvent.songStatus.observe(o, Observer{
            block.invoke(it)
        })
    }

}