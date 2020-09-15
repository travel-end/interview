package com.journey.interview.weatherapp.model


/**
 * @By Journey 2020/9/15
 * @Description
 */
data class Place (
    val primaryKey:Int,
    val name:String,
    val location:Location,
    val address:String = ""
) {
    constructor():this(0,"",Location("",""),"")
}