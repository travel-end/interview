package com.journey.interview.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KProperty

/**
 * @By Journey 2020/9/15
 * @Description  关于kotlin的语法糖定义的一些扩展函数
 */

fun <T> getClass(t: Any): Class<T> {
    // 通过反射获取父类泛型（T） 对应Class类
    return (t.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
}

/**
 * 扩展函数：获取DecorView
 * 当Activity生命周期没有结束的时候，获取它的Window，再从Window中获取DecorView。强转为FrameLayout
 */
val Activity.decorView: FrameLayout?
    get() = (takeIf { !isFinishing && !isDestroyed }?.window?.decorView) as FrameLayout

/**
 * 获取Activity的Content View
 */
val Activity.contentView: FrameLayout?
    get() = takeIf { !isFinishing && !isDestroyed }?.window?.decorView?.findViewById<FrameLayout>(
        android.R.id.content
    )


/**
 * 在非xml环境下构建布局，需要将px转换为dp来进行多屏幕适配，Java的做法是
 * 在Util类中新增一个静态函数。利用kotlin的扩展函数可以更简洁的实现：
 * 使用：40.dp   40f.dp
 */
val Int.dp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

val Int.f_dp: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

val Float.dp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        ).toInt()
    }

/**
 * 打印list:
 * 为集合的基类Collection新增一个扩展函数，它是一个高阶函数，因为它的参数是另一个
 * 函数，该函数用lambda表示。再把集合元素抽象成泛型。通过StringBuilder将所有集合
 * 内容拼接成一个自动换行的字符串
 */
fun <T> Collection<T>.print(map: (T) -> String) =
    StringBuilder("\n[").also { sb ->
        // 遍历集合元素，通过map表达式将元素转换成感兴趣的字串，并独占一行
        this.forEach { e -> sb.append("\n\t${map(e)},") }
        sb.append("\n]")
    }.toString()


/**
 * 打印map的扩展函数
 */
fun <K, V> Map<K, V?>.print(map: (V?) -> String): String =
    StringBuilder("\n{").also { sb ->
        this.iterator().forEach { entry ->
            sb.append("\n\t[${entry.key}], ${map(entry.value)}")
        }
        sb.append("\n}")
    }.toString()


/**
 * 打印 Map，生成结构化键值对子串
 * @param space 行缩进量
 */
fun <K, V> Map<K, V?>.print(space: Int = 0): String {
    //'生成当前层次的行缩进，用space个空格表示，当前层次每一行内容都需要带上缩进'
    val indent = StringBuilder().apply {
        repeat(space) { append(" ") }
    }.toString()
    return StringBuilder("\n${indent}{").also { sb ->
        this.iterator().forEach { entry ->
            //'如果值是 Map 类型，则递归调用print()生成其结构化键值对子串，否则返回值本身'
            val value = entry.value.let { v ->
                (v as? Map<*, *>)?.print("${indent}${entry.key} = ".length) ?: v.toString()
            }
            sb.append("\n\t${indent}[${entry.key}] = $value,")
        }
        sb.append("\n${indent}}")
    }.toString()
}


/**
 * 将data类转换成map：
 * 有些数据类字段比较多，调试时，想把它们通通打印出来，在Java中，我们借助toString()方法
 * 以下扩展函数，可以读取一个类中所有的字段信息，这样我们就可以把它们组织成想要的形状
 */
fun Any.ofMap():Map<String,Any?>?=
    this::class.takeIf { it.isData }
            // 遍历类的所有成员，过滤掉成员方法，只考虑成员属性
        ?.members?.filterIsInstance<KProperty<Any>>()
            // 将成员属性名和值储存在Pair中
        ?.map {member->
            val value = member.call(this)?.let { v->
                //'若成员变量是data class，则递归调用ofMap()，将其转化成键值对，否则直接返回值'
                if (v::class.isData)v.ofMap()
                else v
            }

            member.name to value
        }
            // 将Pair转换成Map
        ?.toMap()

/**
 * 协程和视图生命周期相绑定：
 * 若协程用于异步加载一张图片，这张图片显示在imageView上。当imageView的所在界面
 * 已经被销毁时，得及时取消协程的加载任务，以释放资源
 *
 * 为Job扩展方式，传入View类型的参数，表示该Job和该View的声明周期绑定，当View生命周期
 * 结束时，自动取消协程
 */
fun Job.autoDispose(view: View) {
    val isAttached = Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.KITKAT && view.isAttachedToWindow
            || view.windowToken != null
    if (!isAttached) {
        cancel()
    }
    // 监听视图生命周期
    val listener = object :View.OnAttachStateChangeListener{
        // 在视图生命周期结束时取消协程
        override fun onViewDetachedFromWindow(v: View?) {
            cancel()
            v?.removeOnAttachStateChangeListener(this)
        }

        override fun onViewAttachedToWindow(v: View?) =Unit
    }
    view.addOnAttachStateChangeListener(listener)
    invokeOnCompletion {
        view.removeOnAttachStateChangeListener(listener)
    }
}

/**
 * 为View增加一个扩展方法，返回布尔值，在其中获取视图和屏幕矩形区域，然后判断
 * 是否有交集，若有则表示视图出现在屏幕上。
 */
val View.isOnScreen:Boolean
get() {
    // 获取屏幕宽度
    val screenWidth = context?.resources?.displayMetrics?.widthPixels?:0
    // 获取屏幕高度
    val screenHeight = context?.resources?.displayMetrics?.heightPixels?:0
    // 构建屏幕矩形
    val screenRect = Rect(0,0,screenWidth,screenHeight)
    val array = IntArray(2)
    // 获取矩形视图
    getLocationOnScreen(array)
    val viewRect = Rect(array[0],array[1],array[0] + width,array[1] + height)
    // 判断屏幕和视图矩形是否有交集
    return screenRect.intersect(viewRect)
}

/**
 * RecyclerView子控件点击事件监听器:
 * 为RecyclerView扩展表项点击监听器
 */
fun RecyclerView.setOnRvItemClickListener(listener:(View,Int)->Unit) {
    // 为RecyclerView子控件设置触摸监听器
    addOnItemTouchListener(object :RecyclerView.OnItemTouchListener{
        // 构造手势探测器，用于解析单机事件
        val gestureDetector = GestureDetector(context,object :GestureDetector.OnGestureListener{
            override fun onShowPress(e: MotionEvent?) {
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                // 当单机事件发生时，寻找单机坐标下的子控件，并回调监听器
                e?.let {
                    findChildViewUnder(it.x,it.y)?.let { child->
                        listener(child,getChildAdapterPosition(child))
                    }
                }
                return false
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return false
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return false
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                return false
            }

            override fun onLongPress(e: MotionEvent?) {
            }

        })

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        }
        // 在拦截触摸事件时，解析触发事件
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            gestureDetector.onTouchEvent(e)
            return false
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        }

    })
}

fun EditText?.showKeyBoard(context:Context) {
    this?.let {et->
        // 设置可获得焦点
        et.isFocusable=true
        et.isFocusableInTouchMode=true
        // 获取焦点
        et.requestFocus()
        // 调用系统输入法
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}

fun Activity.hideKeyboards() {
    // 当前焦点的 View
    val imm =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun Activity.isServiceRunning(serviceName:String) :Boolean {
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val infos = am.getRunningServices(100)
    for (info in infos) {
        val name = info?.service?.className
        if (name == serviceName) {
            return true
        }
    }
    return false
}





