package com.journey.interview.customizeview.cropimageview.core2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Created by wenshi on 2019/3/6.
 * Description
 */
public class SquareLinearLayout2 extends LinearLayout {
    public SquareLinearLayout2(Context context) {
        super(context);
    }

    public SquareLinearLayout2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinearLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
