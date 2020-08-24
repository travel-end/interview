package com.journey.interview.customizeview.spiderweb.core

/**
 * Created by wenshi on 2019/3/26.
 * Description https://github.com/HpWens/MeiWidgetView
 */
class SpiderConfig {
    // 小点半径
    var pointRadius =
        DEFAULT_POINT_RADIUS

    // 小点之间连线的粗细(宽度)
    var lineWidth =
        DEFAULT_LINE_WIDTH

    // 小点之间连线的透明度
    var lineAlpha =
        DEFAULT_LINE_ALPHA

    // 小点数量
    var pointNum =
        DEFAULT_POINT_NUMBER

    // 小点加速度
    var pointAcceleration =
        DEFAULT_POINT_ACCELERATION

    // 小点之间最长直线距离
    var maxDistance =
        DEFAULT_MAX_DISTANCE

    // 触摸点半径
    var touchPointRadius =
        DEFAULT_TOUCH_POINT_RADIUS

    // 引力大小
    var gravitationStrength =
        DEFAULT_GRAVITATION_STRENGTH

    companion object {
        const val  DEFAULT_POINT_RADIUS = 1
        const val DEFAULT_LINE_WIDTH = 2
        const val DEFAULT_LINE_ALPHA = 150

        // ~ 160
        const val DEFAULT_POINT_NUMBER = 50
        const val DEFAULT_POINT_ACCELERATION = 7
        const val DEFAULT_MAX_DISTANCE = 280
        const val DEFAULT_TOUCH_POINT_RADIUS = 1
        const val DEFAULT_GRAVITATION_STRENGTH = 50
    }
}