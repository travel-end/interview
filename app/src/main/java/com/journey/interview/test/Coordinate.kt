package com.journey.interview.test

/**
 * @By Journey 2020/9/18
 * @Description
 */
//'坐标类，嵌套在Location类中'
data class Coordinate(var x: Int, var y: Int)
//'位置类，嵌套在Person类中'
data class Location(var country: String, var city: String, var coordinate: Coordinate)
data class Person(var name: String, var age: Int, var locaton: Location? = null)