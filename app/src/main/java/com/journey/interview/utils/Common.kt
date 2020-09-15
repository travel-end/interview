package com.journey.interview.utils

import java.lang.reflect.ParameterizedType

/**
 * @By Journey 2020/9/15
 * @Description
 */

fun <T> getClass(t:Any):Class<T> {
    // 通过反射获取父类泛型（T） 对应Class类
    return (t.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
}