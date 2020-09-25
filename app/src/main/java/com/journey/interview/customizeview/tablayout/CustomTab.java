package com.journey.interview.customizeview.tablayout;

import androidx.annotation.DrawableRes;

/**
 * @By Journey 2020/9/25
 * @Description
 */
public interface CustomTab {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}
