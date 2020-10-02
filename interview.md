### kotlin
####also函数，返回调用者本身，如：
fun makeDir(path:String) = path.let{File(it)}.also{it.mkdirs()}

#### kotlin语法糖  

#### DSL


###关于kotlin中的内部类和嵌套类
在Kotlin中的内部类也是指在一个类的内部声明另一个类，但是和Java中的规则有些差异。
在Kotlin中的内部类和Java中的内部类相似，都会持有一个外部类的引用，但是在Kotlin中
内部类的声明方式变化了，必须要使用 `inner`修饰符。
在Kotlin中，没有静态内部类一说，Java中的静态内部类在Kotlin中称为`嵌套类`。而且默认的
就是嵌套类，也就是内部类不写任何修饰符就是`嵌套类`。
同样的，`内部类`会持有一个外部类的引用，`嵌套类`不持有外部类的引用。
下面通过代码来说明：
```
class OutClass {
    var count = 0
    fun add() {
        count++
    }
    // 内部类
    inner class InnerClass {
        fun doSomething():Int {
               // 默认持有外部类的引用，直接访问外部类的方法属性
            add()
            this@OutClass.add()
            return this@OutClass.count
        }
    }
    // 嵌套类
    class NestedClass {
        fun getValue() :Int {
            // 嵌套类不持有外部类的引用，必须通过外部类的对象访问
            val outClass = OutClass()
            outClass.add()
            return outClass.count
        }
    }
}
            
```
下面通过表格来对比一下Java和Kotlin的内部类
    类A在另一个类B中声明             在Java中              在Kotlin中
    嵌套类（不储存外部类的引用）      static class A          class A
    内部类（存储外部类的引用）        class A                 inner class A
    
 
 ####简单天气app   
 
 
 #### Service
 
 启动方式                   存在方式
 startService             独立于Activity运行，自行结束或者被叫停
 bindService              绑定于Activity运行，Activity结束时，会被叫停
 
 涉及方法
 onCreate()
 onDestroy()
 onStartCommand()         用于计数，服务被调用的次数
 onBind()                 与Activity组件绑定
 onUnBind()               与Activity组件解绑
 
 使用方法（步骤）
 ### 第一步：在AndroidManifest.xml中进行注册
 `<service android:name=".LocalService" />`
 ### 第二步：启动
 `startService(Intent)`
 `bindService(Intent, ServiceConnetion,Int)`
 ### 第三步：解绑
 `unBindService(ServiceConnetion)`
 ### 第四步：暂停
 `stopService(Intent)`
 
 
 #### lrcView API
 属性	                描述
 lrcTextSize	        当前歌词文本字体大小
 lrcNormalTextSize	    普通歌词文本字体大小
 lrcNormalTextColor	    非当前行歌词字体颜色
 lrcCurrentTextColor	当前行歌词字体颜色
 lrcTimelineTextColor	拖动歌词时选中歌词的字体颜色
 lrcTextGravity	        歌词对齐方向，center：居中对齐，left：靠左对齐，right：靠右对齐，默认为 center
 lrcDividerHeight	    歌词间距
 lrcAnimationDuration	歌词滚动动画时长
 lrcLabel	            没有歌词时屏幕中央显示的文字，如“暂无歌词”
 lrcPadding	            歌词文字的左右边距
 lrcTimelineColor	    拖动歌词时时间线的颜色
 lrcTimelineHeight	    拖动歌词时时间线的高度
 lrcPlayDrawable	    拖动歌词时左侧播放按钮图片
 lrcTimeTextColor	    拖动歌词时右侧时间字体颜色
 lrcTimeTextSize	    拖动歌词时右侧时间字体大小
 
 
 方法	描述
 loadLrc(File lrcFile)	                            加载歌词文件
 loadLrc(File mainLrcFile, File secondLrcFile)	    加载双语歌词文件，两种语言的歌词时间戳需要一致
 loadLrc(String lrcText)	                        加载歌词文本
 loadLrc(String mainLrcText, String secondLrcText)	加载双语歌词文本，两种语言的歌词时间戳需要一致
 loadLrcByUrl(String lrcUrl)	                    加载在线歌词文本，默认使用 utf-8 编码
 loadLrcByUrl(String lrcUrl, String charset)	    加载在线歌词文本
 hasLrc()	                                        歌词是否有效
 setLabel(String label)	                            设置歌词为空时视图中央显示的文字，如“暂无歌词”
 updateTime(long time)	                            刷新歌词
 onDrag(long time)	                                将歌词滚动到指定时间。已弃用，请使用 updateTime(long) 代替
 setOnPlayClickListener(OnPlayClickListener onPlayClickListener)	        设置拖动歌词时，播放按钮点击监听器。如果为非 null ，则激活歌词拖动功能，否则将将禁用歌词拖动功能。已弃用，请使用 setDraggable 代替
 setDraggable(Boolean draggable, OnPlayClickListener onPlayClickListener)	设置歌词是否允许拖动。如果允许拖动，则 OnPlayClickListener 不能为 null
 setOnTapListener(LrcView view, float x, float y)	                        设置歌词控件点击监听器
 setNormalColor	                                    设置非当前行歌词字体颜色
 setCurrentColor	                                设置当前行歌词字体颜色
 setTimelineTextColor	                            设置拖动歌词时选中歌词的字体颜色
 setTimelineColor	                                设置拖动歌词时时间线的颜色
 setTimeTextColor	                                设置拖动歌词时右侧时间字体颜色
 setCurrentTextSize	                                当前歌词文本字体大小
 setNormalTextSize	                                普通歌词文本字体大小
 
