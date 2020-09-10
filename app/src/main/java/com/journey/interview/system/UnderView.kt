package com.journey.interview.system

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * @By Journey 2020/9/10
 * @Description
 */
class UnderView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : View(context, attributeSet, defAttrStyle) {
    // 记录滑动操作的起始x坐标
    private var mStartX:Float = 0f
    private var mMoveView:View?=null
    private var mainHandler:Handler?= null
    private var mWidth:Int = 0
    fun setMoveView(view: View) {
        this.mMoveView = view
    }
    fun setMainHandler(handler: Handler) {
        this.mainHandler = handler
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mWidth =w
        Log.i("JG","onSizeChanged---->w=$w")
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {evt->
            val nx = evt.x
            when(evt.action) {
                MotionEvent.ACTION_DOWN->{
                    Log.i("JG","onTouchEvent---->ACTION_DOWN")
                    mStartX = nx
                    Log.i("JG","onTouchEvent---->mStartX=$mStartX")
                    onAnimationEnd()
                }
                MotionEvent.ACTION_MOVE->{
                    Log.i("JG","onTouchEvent---->ACTION_MOVE")
                    Log.i("JG","ACTION_MOVE ---->nx=$nx")
                    handleMoveView(nx)
                }
                MotionEvent.ACTION_UP->{
                    Log.i("JG","onTouchEvent---->ACTION_UP")
                    doTriggerEvent(nx)
                }
                MotionEvent.ACTION_CANCEL->{
                    Log.i("JG","onTouchEvent---->ACTION_CANCEL")
                    Log.i("JG","ACTION_CANCEL ---->nx=$nx")
//                    doTriggerEvent(nx)
                }
                else->{}
            }
        }
        return true
    }

    /**
     * 控制moveView随手指的移动
     */
    private fun handleMoveView(x:Float) {
        var moveX = x - mStartX
        if (moveX < 0f) {
            moveX = 0f
        }
        mMoveView?.translationX = moveX
        val mWidthFloat = mWidth.toFloat()
        if (background != null) {
            val mAlpha = (
                    (mWidthFloat - mMoveView?.translationX!!) / mWidthFloat *200
                    ).toInt()
            Log.i("JG","handleMoveView---->mAlpha=$mAlpha")
            background.alpha = mAlpha
        }
    }

    private fun doTriggerEvent(x:Float) {
        val moveX = x - mStartX
        if (moveX > (mWidth * 0.4)) {
            // 自动移动到屏幕右边界之外，并finish掉
            moveMoveView(mWidth.toFloat() - mMoveView?.left!!,true)
        } else {
            // 自动移动回初始位置，重新覆盖
            val leftX = -mMoveView?.left?.toFloat()!!
            Log.i("JG","doTriggerEvent---->leftX=$leftX")
            moveMoveView(leftX,false)
        }
    }

    private fun moveMoveView(to :Float, exit:Boolean) {
        val animator = ObjectAnimator.ofFloat(mMoveView!!,"translationX",to)
        animator?.let {
            it.addUpdateListener {anim->
                if (background != null) {
                    background.alpha = (
                            (mWidth.toFloat() - mMoveView?.translationX!!) / mWidth.toFloat() *200
                            ).toInt()
                }

            }
            it.duration = 250
            it.start()

            if (exit) {
                it.addListener(object :AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        mainHandler?.obtainMessage(LockScreenActivity.MSG_LAUNCH_HOME)?.sendToTarget()
                        super.onAnimationEnd(animation)
                    }
                })
            }
        }
    }
}