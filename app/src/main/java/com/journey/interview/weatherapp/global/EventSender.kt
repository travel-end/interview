package com.journey.interview.weatherapp.global

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.journey.interview.InterviewApp

/**
 * @By Journey 2020/9/23
 * @Description
 */
object EventSender {
    private val globalViewModel = InterviewApp.instance.getGlobalViewModel(AppEventViewModel::class.java)

    fun sendPlaceChosenEvent() {
        globalViewModel.addChoosePlace.value = true
    }

    fun observePlaceChosen(o:LifecycleOwner,block:()->Unit) {
        globalViewModel.addChoosePlace.observe(o,Observer{
            block.invoke()
        })
    }

    fun sendPlaceAddEvent() {
        globalViewModel.addPlace.value = true
    }

    fun observePlaceAdd(o:LifecycleOwner,block:()->Unit) {
        globalViewModel.addPlace.observe(o,Observer{
            block.invoke()
        })
    }

    fun changeCurrentPlace(position:Int) {
        globalViewModel.currentPlace.value = position
    }

    fun observePlaceChange(o:LifecycleOwner,block:(Int?)->Unit) {
        globalViewModel.currentPlace.observe(o,Observer{
            block.invoke(it)
        })
    }
}