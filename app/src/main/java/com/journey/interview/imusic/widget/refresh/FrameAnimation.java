package com.journey.interview.imusic.widget.refresh;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;


import androidx.annotation.NonNull;

import java.lang.ref.SoftReference;

import static android.R.attr.height;
import static android.R.attr.width;

/**
 * 帧动画动画类
 *
 * @author haohao on 2017/6/27 14:28
 * @version v1.0
 */
public class FrameAnimation {


    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (onImageLoadListener != null) {
                onImageLoadListener.onImageLoad(bitmapDrawable);
            }
            mHandler.sendEmptyMessage(0);
        }
    };

    public static final float DEFAULT_DURATION = 100f;

    private int[] resIds;//动画列表
    private volatile float durations[];//动画间隔时间数组
    private boolean loop = false;//是否循环
    private boolean isRunning = false;
    private boolean needStop = false;


    private int index = 0;//当前显示图片
    private ImageCache imageCache;
    private volatile BitmapDrawable bitmapDrawable;
    private OnImageLoadListener onImageLoadListener;
    private Resources resources;
    private HandlerThread mHandlerThread;
    private Handler mHandler;


    FrameAnimation(Resources resources, int[] resIds, boolean loop, float[] duration, boolean
            isLowMemory) {
        imageCache = new ImageCache(isLowMemory);
        this.resources = resources;
        this.resIds = resIds;
        this.loop = loop;
        this.durations = duration;
    }

    public void setResIds(int[] resIds) {
        this.resIds = resIds;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setDuration(float[] duration) {
        this.durations = duration;
    }

    public void start() {
        if (isRunning) {
            return;
        }
        if (mHandlerThread != null) {
            mHandlerThread.quitSafely();
        }
        mHandlerThread = new HandlerThread("handlerThread");
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                loadInThreadFromRes();
            }
        };

        mHandler.sendEmptyMessage(0);
    }

    public void pause() {
        if (!isRunning) {
            return;
        }
        needStop = true;
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        index = 0;
        needStop = true;
        imageCache.destory();
    }

    private void loadInThreadFromRes() {
        if (resIds == null || resIds.length == 0) {
            isRunning = false;
            return;
        }
        if (needStop) {
            isRunning = false;
            needStop = false;
            if (mainHandler != null) {
                mHandlerThread.quitSafely();
            }
            if (mainHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
            }
            if (mainHandler != null) {
                mainHandler.removeCallbacksAndMessages(null);
            }
            return;
        }
        isRunning = true;
        if (index < resIds.length) {
            int resId = resIds[index];
            if (bitmapDrawable != null) {
                imageCache.mReusableBitmaps.add(new SoftReference<>(bitmapDrawable.getBitmap()));
            }
            long start = System.currentTimeMillis();
            Log.e("durations", "start =  " + start);
            bitmapDrawable = BitmapLoadUtil.decodeBitmapFromResLruCache(resources, resId, width,
                    height, imageCache);
            long end = System.currentTimeMillis();
            Log.e("durations", "end =  " + end);
            float duration = DEFAULT_DURATION;
            if (durations != null && durations.length >= index - 1) {
                duration = durations[index];
            }
            float updateTime = (duration - (end - start)) > 0 ? (duration - (end - start)) : 0;
            Log.e("durations", "durations =  " + durations + "  updateTime = " + updateTime);
            Message message = Message.obtain();
            message.obj = resIds;
            mainHandler.sendMessageDelayed(message, index == 0 ? 0 : (long) updateTime);
            index++;
        } else {
            if (loop) {
                index = 0;
                loadInThreadFromRes();
            } else {
                index++;
                bitmapDrawable = null;
                durations = null;
                if (onImageLoadListener != null) {
                    onImageLoadListener.onFinish();
                }
                isRunning = false;
                onImageLoadListener = null;
            }
        }

    }

    private final Runnable mTicker = new Runnable() {
        public void run() {
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);
            mainHandler.postAtTime(mTicker, next);
            Log.e("mTicker", now + "");
        }
    };

    public void setOnImageLoadListener(OnImageLoadListener onImageLoadListener) {
        this.onImageLoadListener = onImageLoadListener;
    }

    public static class FrameAnimationBuilder {

        private Resources resources;
        private float duration[];//动画间隔时间
        private boolean loop = false;//是否循环
        private int[] resIds;//动画列表
        private boolean isLowMemory;//低内存、少量图片时设置，防止图片不显示

        public FrameAnimationBuilder(@NonNull Resources resources) {
            this.resources = resources;
        }

        public FrameAnimationBuilder setResIds(int[] resIds) {
            this.resIds = resIds;
            return this;
        }

        public FrameAnimationBuilder setLoop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public FrameAnimationBuilder setDuration(float[] duration) {
            this.duration = duration;
            return this;
        }

        public FrameAnimationBuilder isLowMemory(boolean isLowMemory) {
            this.isLowMemory = isLowMemory;
            return this;
        }

        public FrameAnimation build() {
            return new FrameAnimation(resources, resIds, loop, duration, isLowMemory);
        }


    }
}
