package com.journey.interview.test

/**
 * @By Journey 2020/9/18
 * @Description
 */
data class IPerson(
    var name :String,
    var gender: Int = 1,
    var age: Int= 0,
    var height: Int = 0,
    var weight: Int = 0
) {
    // 在构造函数 被调用的时候执行参数合法检查
    init {
        require(name.isNotEmpty()){"name can`t be empty"}
    }
}