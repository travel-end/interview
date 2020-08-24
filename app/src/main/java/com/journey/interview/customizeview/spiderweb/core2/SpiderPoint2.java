package com.journey.interview.customizeview.spiderweb.core2;

import android.graphics.Point;

/**
 * Created by wenshi on 2019/3/26.
 * Description https://github.com/HpWens/MeiWidgetView
 */
public class SpiderPoint2 extends Point {

    // x 方向加速度
    public int aX;

    // y 方向加速度
    public int aY;

    // 小球颜色
    public int color;

    // 小球半径
    public int r;

    // x 轴方向速度
    public float vX;

    // y 轴方向速度
    public float vY;

    // 点
    // public float x;
    // public float y;


    public SpiderPoint2(int x, int y) {
        super(x, y);
    }

    public SpiderPoint2() {
    }
}
