package com.journey.interview.weatherapp.model

/**
 * @By Journey 2020/9/15
 * @Description
 */
data class SearchPlace(
    val status:String,
    val places:MutableList<Place>
)