package com.journey.interview.customizeview.swipecaptcha.core

import android.graphics.Path
import android.graphics.PointF
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @By Journey 2020/8/21
 * @Description
 */
object DrawHelperUtil {
    /**
     * 传入起点、终点 坐标、凹凸和Path。
     * 会自动绘制凹凸的半圆弧
     *
     * @param start 起点坐标
     * @param end   终点坐标
     * @param path  半圆会绘制在这个path上
     * @param outer 是否凸半圆
     */
    fun drawPartCircle(
        start: PointF,
        end: PointF,
        path: Path,
        outer: Boolean
    ) {
        val c = 0.551915024494f
        //中点
        val middle =
            PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2)
        //半径
        val r1 = sqrt(
            (middle.x - start.x).toDouble().pow(2.0) + (middle.y - start.y).toDouble().pow(2.0)
        ).toFloat()
        //gap值
        val gap1 = r1 * c
        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            val topToBottom = end.y - start.y > 0
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            val flag: Int //旋转系数
            flag = if (topToBottom) {
                1
            } else {
                -1
            }
            if (outer) {
                //凸的 两个半圆
                path.cubicTo(
                    start.x + gap1 * flag, start.y,
                    middle.x + r1 * flag, middle.y - gap1 * flag,
                    middle.x + r1 * flag, middle.y
                )
                path.cubicTo(
                    middle.x + r1 * flag, middle.y + gap1 * flag,
                    end.x + gap1 * flag, end.y,
                    end.x, end.y
                )
            } else {
                //凹的 两个半圆
                path.cubicTo(
                    start.x - gap1 * flag, start.y,
                    middle.x - r1 * flag, middle.y - gap1 * flag,
                    middle.x - r1 * flag, middle.y
                )
                path.cubicTo(
                    middle.x - r1 * flag, middle.y + gap1 * flag,
                    end.x - gap1 * flag, end.y,
                    end.x, end.y
                )
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            val leftToRight = end.x - start.x > 0
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            val flag: Int //旋转系数
            flag = if (leftToRight) {
                1
            } else {
                -1
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(
                    start.x, start.y - gap1 * flag,
                    middle.x - gap1 * flag, middle.y - r1 * flag,
                    middle.x, middle.y - r1 * flag
                )
                path.cubicTo(
                    middle.x + gap1 * flag, middle.y - r1 * flag,
                    end.x, end.y - gap1 * flag,
                    end.x, end.y
                )
            } else {
                //凹 两个半圆
                path.cubicTo(
                    start.x, start.y + gap1 * flag,
                    middle.x - gap1 * flag, middle.y + r1 * flag,
                    middle.x, middle.y + r1 * flag
                )
                path.cubicTo(
                    middle.x + gap1 * flag, middle.y + r1 * flag,
                    end.x, end.y + gap1 * flag,
                    end.x, end.y
                )
            }


/*
            没推导之前的公式在这里
            if (start.x < end.x) {
                if (outer) {
                    //上左半圆 顺时针
                    path.cubicTo(start.x, start.y - gap1,
                            middle.x - gap1, middle.y - r1,
                            middle.x, middle.y - r1);

                    //上右半圆:顺时针
                    path.cubicTo(middle.x + gap1, middle.y - r1,
                            end.x, end.y - gap1,
                            end.x, end.y);
                } else {
                    //下左半圆 逆时针
                    path.cubicTo(start.x, start.y + gap1,
                            middle.x - gap1, middle.y + r1,
                            middle.x, middle.y + r1);

                    //下右半圆 逆时针
                    path.cubicTo(middle.x + gap1, middle.y + r1,
                            end.x, end.y + gap1,
                            end.x, end.y);
                }
            } else {
                if (outer) {
                    //下右半圆 顺时针
                    path.cubicTo(start.x, start.y + gap1,
                            middle.x + gap1, middle.y + r1,
                            middle.x, middle.y + r1);
                    //下左半圆 顺时针
                    path.cubicTo(middle.x - gap1, middle.y + r1,
                            end.x, end.y + gap1,
                            end.x, end.y);
                }
            }*/
        }
    }
}