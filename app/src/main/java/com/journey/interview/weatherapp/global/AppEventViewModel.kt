package com.journey.interview.weatherapp.global

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @By Journey 2020/9/23
 * @Description
 */
class AppEventViewModel:ViewModel() {
    val addPlace = MutableLiveData<Boolean>()
    val addChoosePlace = MutableLiveData<Boolean>()
    val changeCurrentPlace = MutableLiveData<Boolean>()

    val currentPlace = UnPeekLiveData<Int>()

}