### kotlin
####also函数，返回调用者本身，如：
fun makeDir(path:String) = path.let{File(it)}.also{it.mkdirs()}

#### kotlin语法糖  

#### DSL


#### 实用的库
LayoutManagerDemo:https://github.com/BeauteousJade/LayoutManagerDemo
表格布局（RecyclerView实现）:https://github.com/mCyp/Orient-Ui



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
 
 
 ####todo
 自定义Toast（表情包）
 本地歌曲Fragment使用下拉刷新
 歌词自动滚动
 
 
 ####动画
 xml中属性
 属性	含义
 android:duration	动画执行时间
 android:interpolator	插值器
 android:repeatCount	重复次数
 android:repeatMode	重复模式，reverse：倒着重放动画。restart：正着重放动画
 android:startOffset	延迟开始动画时间
 android:valueFrom	值得开始值，int float color
 android:valueTo	值得目标值 int float color
 android:valueType=	值得类型 intType floatType
 
 
 ####Android MediaPlayer简介
 1）如何获得MediaPlayer实例：
 可以使用直接new的方式：
 MediaPlayer mp = new MediaPlayer();
 也可以使用create的方式，如：
 MediaPlayer mp = MediaPlayer.create(this, R.raw.test);//这时就不用调用setDataSource了
 
 2) 如何设置要播放的文件：
 MediaPlayer要播放的文件主要包括3个来源：
 a. 用户在应用中事先自带的resource资源
 例如：MediaPlayer.create(this, R.raw.test);
 b. 存储在SD卡或其他文件路径下的媒体文件
 例如：mp.setDataSource("/sdcard/test.mp3");
 c. 网络上的媒体文件
 例如：mp.setDataSource("http://www.citynorth.cn/music/confucius.mp3");
 
3）方法介绍
void start()：开始或恢复播放。
void stop()：停止播放。
void pause()：暂停播放。

int getDuration()：获取流媒体的总播放时长，单位是毫秒。
int getCurrentPosition()：获取当前流媒体的播放的位置，单位是毫秒。
void seekTo(int msec)：设置当前MediaPlayer的播放位置，单位是毫秒。
void setLooping(boolean looping)：设置是否循环播放。
boolean isLooping()：判断是否循环播放。
boolean  isPlaying()：判断是否正在播放。
void prepare()：同步的方式装载流媒体文件。
void prepareAsync()：异步的方式装载流媒体文件。
void release ()：回收流媒体资源。 
void setAudioStreamType(int streamtype)：设置播放流媒体类型。
void setWakeMode(Context context, int mode)：设置CPU唤醒的状态。
setNextMediaPlayer(MediaPlayer next)：设置当前流媒体播放完毕，下一个播放的MediaPlayer。
 
 
 除了上面介绍的一些方法外，MediaPlayer还提供了一些事件的回调函数，这里介绍几个常用的：
 setOnCompletionListener(MediaPlayer.OnCompletionListener listener)：当流媒体播放完毕的时候回调。
 setOnErrorListener(MediaPlayer.OnErrorListener listener)：当播放中发生错误的时候回调。
 setOnPreparedListener(MediaPlayer.OnPreparedListener listener)：当装载流媒体完毕的时候回调。
 setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener)：当使用seekTo()设置播放位置的时候回调。
``
                    mediaPlayer = new MediaPlayer();
02.2                 mediaPlayer.setDataSource(path);
03.3                 mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
04.4                
05.5                 // 通过异步的方式装载媒体资源
06.6                 mediaPlayer.prepareAsync();
07.7                 mediaPlayer.setOnPreparedListener(new OnPreparedListener() {                   
08.8                     @Override
09.9                     public void onPrepared(MediaPlayer mp) {
10.10                         // 装载完毕回调
11.11                         mediaPlayer.start();
12.12                     }
13.13                 })
``

4)使用技巧
1、在使用start()播放流媒体之前，需要装载流媒体资源。这里最好使用prepareAsync()用异步的方式装载流媒体资源。
因为流媒体资源的装载是会消耗系统资源的，在一些硬件不理想的设备上，如果使用prepare()同步的方式装载资源，
可能会造成UI界面的卡顿，这是非常影响用于体验的。因为推荐使用异步装载的方式，为了避免还没有装载完成就调用start()而报错的问题，
需要绑定MediaPlayer.setOnPreparedListener()事件，它将在异步装载完成之后回调。异步装载还有一个好处就是避免装载超
时引发ANR（(Application Not Responding）错误。

2、使用完MediaPlayer需要回收资源。MediaPlayer是很消耗系统资源的，所以在使用完MediaPlayer，不要等待系统自动回收，最好是主动回收资源。
``
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
2.2             mediaPlayer.stop();
3.3             mediaPlayer.release();
4.4             mediaPlayer = null;
5.5         }
``

3、使用MediaPlayer最好使用一个Service来使用，并且在Service的onDestory()方法中回收MediaPlayer资源，实际上，
就算是直接使用Activity承载MediaPlayer，也最好在销毁的时候判断一下MediaPlayer是否被回收，如果未被回收，回收其资源，
因为底层调用的native方法，如果不销毁还是会在底层继续播放，而承载的组件已经被销毁了，这个时候就无法获取到这个MediaPlayer
进而控制它。
``
           @Override
02.2     protected void onDestroy() {
03.3         if (mediaPlayer != null && mediaPlayer.isPlaying()) {
04.4             mediaPlayer.stop();
05.5             mediaPlayer.release();
06.6             mediaPlayer = null;
07.7         }
08.8         super.onDestroy();
09.9     }
``

4、对于单曲循环之类的操作，除了可以使用setLooping()方法进行设置之外，还可以为MediaPlayer注册回调函数，
MediaPlayer.setOnCompletionListener()，它会在MediaPlayer播放完毕被回调。
``
01.1                 // 设置循环播放
02.2 //                mediaPlayer.setLooping(true);
03.3                 mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
04.4                    
05.5                     @Override
06.6                     public void onCompletion(MediaPlayer mp) {
07.7                         // 在播放完毕被回调
08.8                         play();                       
09.9                     }
10.10                 });
``
5、因为MediaPlayer一直操作的是一个流媒体，所以无可避免的可能一段流媒体资源，前半段可以正常播放，
而中间一段因为解析或者源文件错误等问题，造成中间一段无法播放问题，需要我们处理这个错误，否则会影响Ux（用户体验）。
可以为MediaPlayer注册回调函数setOnErrorListener()来设置出错之后的解决办法，一般重新播放或者播放下一个流媒体即可
``
1.1                 mediaPlayer.setOnErrorListener(new OnErrorListener() {
2.2                    
3.3                     @Override
4.4                     public boolean onError(MediaPlayer mp, int what, int extra) {
5.5                         play();
6.6                         return false;
7.7                     }
8.8                 })
``
@link https://blog.csdn.net/world_kun/article/details/79788250

####需求池
1、添加tab诗词部分添加图片诗词（带评论） 参考开眼视频 发现tab
2、首页下面四个菜单 分别为唐诗 宋词 诗经 其他朝代的诗词
3、最下面的四个菜单 提供四个歌单入口  每日推荐  精品歌单等（提供一个歌单页面  歌单的内容从asset获取）
4、最下面滑动为
5、歌单名称为诗句   app  小i音乐（最好能个诗词相关联）
6、在asset目录下储存大量音乐路径，分类形成本地歌单
7、每个诗词详情页推荐一首歌（最好与之关联）


####优化的点
1、todo 如果播放的是网络歌曲继续播放搜索的下一首 playService 144行
2、// 处理播放出错的逻辑
3、是否需要通知栏 以及通知栏的样式
4、IMainActivity退出程序后，如何保持是退出之前的同一个mediaPlayer对象？ IMainActivity 135行
这样子的话重新播放音乐就不会重新装载了（装载需要时间   或者把这个时间放在activity的onResume方法中麻痹用户？？）
5、处理初次进入页面没有歌曲的情况（在Application中查询本地歌曲 如果有 用第一条歌曲   如果一条都没有，自己推荐一条）
6、播放界面顶部的songName改为访网易云音乐那样的滚动
7、资源不够 UI来凑  UI 一定要好看！！！！！   添加自定义字体！
