package com.journey.interview.imusic.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.journey.interview.R;
import com.journey.interview.imusic.util.ImUtils;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;


public class IRefreshHeader extends LinearLayout implements RefreshHeader {
    private FrameAnimView frameAnimView;
    private static long startTime;

    public IRefreshHeader(Context context) {
        this(context, null, 0);
    }

    public IRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.imusic_custom_refresh_header, this);
        view.setPadding(0, ImUtils.getStatusBarHeight(context),0,0);
        frameAnimView = view.findViewById(R.id.ptr_anim_view);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int
            maxDragHeight) {
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int
            maxDragHeight) {
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        Log.e("JNRefreshHeader", "---------onFinish----------");
        if (frameAnimView != null) {
            frameAnimView.stop();
//            frameAnimView.setImageDrawable(null);
//            frameAnimView.setVisibility(GONE);
        }
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState
            oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh: //下拉刷新开始。正在下拉还没松手时调用
                //每次重新下拉时，将图片资源重置为第一帧
//                frameAnimView.setVisibility(VISIBLE);
                frameAnimView.setImageResource(R.drawable.ptr_img_1);
                Log.e("JNRefreshHeader", "---------   PullDownToRefresh   ----------");
                break;
            case Refreshing: //正在刷新。只调用一次
                Log.e("JNRefreshHeader", "---------   正在刷新   ----------");
                startTime = System.currentTimeMillis();
                //状态切换为正在刷新状态时，设置图片资源为 动画并开始执行
                if (frameAnimView.getDrawable() == null) {
                    frameAnimView.setImageResource(R.drawable.ptr_animation);
                }
                frameAnimView.start();
                break;
            case ReleaseToRefresh:

                break;
        }
    }

    public static int getDelayTime(){
        int delayTime = 0;
        try {
            int minTime = 2500;//动画最低显示时间
            long endTime = System.currentTimeMillis();
            Log.e("JNRefreshHeader","showTime = "+(endTime - startTime));
            if(endTime - startTime >= minTime){
                delayTime = 0;
            }else{
                delayTime =  (int)(minTime -(endTime - startTime));
            }
            Log.e("JNRefreshHeader","delayTime = "+delayTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return delayTime;
    }

}
