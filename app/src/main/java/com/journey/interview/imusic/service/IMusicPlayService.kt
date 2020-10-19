package com.journey.interview.imusic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.act.IMainActivity
import com.journey.interview.imusic.download.IMusicDownloadUtil
import com.journey.interview.imusic.global.Bus
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.*
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.utils.SongUtil
import com.journey.interview.utils.getString
import kotlinx.coroutines.*
import java.io.IOException

/**
 * @By Journey 2020/9/27
 * @Description
 * @link https://www.jianshu.com/p/8d0cde35eb10
 */
class IMusicPlayService : Service() {
    /* ***音乐列表*/
    private var mDownloadedSongs: MutableList<Downloaded>? = null
    private var mLocalSongs: MutableList<LocalSong>? = null
    private var mLoveSongs: MutableList<LoveSong>? = null
    private var mHistorySongs: MutableList<HistorySong>? = null

    private var playResult:Int = Constant.PLAY_SUCCESS

    companion object {
        const val NOTIFICATION_ID = 98
    }

    private var mIsPlaying: Boolean = false
    private var mIsPause: Boolean = false
    private var mListType: Int? = null// 歌曲列表类别,0表示当前没有列表，即可能在播放网络歌曲
    private var mCurrent: Int? = null

    private val mMediaPlayer by lazy {
        MediaPlayer()
    }

    private val mPlayStatusBinder = PlayStatusBinder()
    var mPlayMode = Constant.PLAY_ORDER // 默认设置为顺序播放

    /**
     * 1、调用时机：手动调用startService方法（本项目在[IMainActivity]onCreate中调用的）
     * 2、说明：
     * 1）即使一个Service被startService启动多次，onCreate方法也只会调用一次（在Service中，
     * 只有onStartCommand方法可以被多次调用，其他方法都只能被调用一次，即onStartCommand调用次数=startService调用次数）
     * 2）
     *
     */
    override fun onCreate() {
        Log.e("JG","--->IMusicPlayService onCreate")
        super.onCreate()
        mListType = SongUtil.getSong()?.listType
        mListType?.let {
            when (it) {
                Constant.LIST_TYPE_DOWNLOAD -> {
                    mDownloadedSongs =
                        IMusicDownloadUtil.getSongFromFile(Constant.STORAGE_SONG_FILE)
                }
                Constant.LIST_TYPE_LOCAL->{
                    mLocalSongs = runBlocking {
                        withContext(Dispatchers.IO) {
                            IMusicRoomHelper.getAllLocalSongs()
                        }
                    }
                    Log.e("JG","onCreate---> localsongs:$mLocalSongs")
                }
                Constant.LIST_TYPE_LOVE->{
                    mLoveSongs = runBlocking {
                        withContext(Dispatchers.IO) {
                            IMusicRoomHelper.getAllMyLoveSong()
                        }
                    }
                    Log.e("JG","onCreate---> lovesongs:$mLocalSongs")
                }
                Constant.LIST_TYPE_HISTORY->{
                    mHistorySongs = runBlocking {
                        withContext(Dispatchers.IO) {
                            IMusicRoomHelper.queryAllHistorySongs()
                        }
                    }
                    // 保证最近播放的歌曲总是列表的第一个
                    val song = SongUtil.getSong()
                    song?.position = 0
                    SongUtil.saveSong(song)
                }
                else-> {

                }
            }
        }
        startForeground(NOTIFICATION_ID, getNotification("爱音乐，开启你的私人音乐之旅o(*￣▽￣*)ブ"))
    }

    /**
     * 调用bindService时调用，如果在之前已经调用过bindService，则不会调用该方法
     * 如[IMainActivity]中已经调用过bindService，在[IPlayActivity]中的bindService 方法
     * 就不会再调用该方法
     */
    override fun onBind(intent: Intent?): IBinder? {
        Log.e("JG","--->IMusicPlayService onBind")
        // 音乐播放完毕的时候调用
        mMediaPlayer.setOnCompletionListener { mp ->
//            IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_COMPLETE)
            Log.e("JG","--->歌曲播放完毕")
            Bus.post(Constant.SONG_STATUS_CHANGE,Constant.SONG_PAUSE)
            mCurrent = SongUtil.getSong()?.position // 当前歌曲在列表中的位置  如第一首 为：0
            if (mListType != null) {
                when (mListType) {
                    Constant.LIST_TYPE_DOWNLOAD -> {
                        mCurrent = getNextSongPosition(mCurrent?:0,mPlayMode,mDownloadedSongs?.size?:1)
                        saveDownloadInfo(mCurrent ?: 0)
                    }
                    Constant.LIST_TYPE_LOCAL->{
                        mCurrent = getNextSongPosition(mCurrent?:0,mPlayMode,mLocalSongs?.size?:1)
                        saveLocalSong(mCurrent?:0)
                    }
                    Constant.LIST_TYPE_LOVE->{
                        mCurrent = getNextSongPosition(mCurrent?:0,mPlayMode,mLoveSongs?.size?:1)
                        saveLoveSong(mCurrent?:0)
                    }
                    Constant.LIST_TYPE_HISTORY->{
                        mCurrent = getNextSongPosition(mCurrent?:0,mPlayMode,mHistorySongs?.size?:1)
                        saveHistorySong(mCurrent?:0)
                    }
                }
            }

            if (mListType != 0 && mListType != null) {
                mPlayStatusBinder.play(mListType!!)
            } else {
                // todo 如果播放的是网络歌曲继续播放搜索的下一首
                mPlayStatusBinder.stop()
            }
        }
        /**
         * MediaPlayer切歌进入setOnCompletionListener的问题
         * 因为直接切歌会发生错误，所以增加错误监听器。返回true。就不会回调onCompletion方法了。
         * todo // 处理播放出错的逻辑
         */
        mMediaPlayer.setOnErrorListener { _, what, extra ->
            Log.e("JG","--->setOnErrorListener:$what, $extra")
            playResult = Constant.PLAY_FAILED
            Toast.makeText(this@IMusicPlayService,R.string.play_error.getString(),Toast.LENGTH_SHORT).show()
            true
        }
        return mPlayStatusBinder

    }

    inner class PlayStatusBinder : Binder() {
        fun setPlayMode(mode: Int) {
            this@IMusicPlayService.mPlayMode = mode
        }

        // 播放音乐
        fun play(listType: Int?,restartTime:Int?=null) :Int{
            try {
                this@IMusicPlayService.mListType = listType
                if (mListType != null) {
                    when (mListType) {
                        Constant.LIST_TYPE_DOWNLOAD -> {
                            mDownloadedSongs =
                                orderDownloadList(IMusicDownloadUtil.getSongFromFile(Constant.STORAGE_SONG_FILE))
                            Log.e("JG","play---> downloadSongs:$mDownloadedSongs")
                        }
                        Constant.LIST_TYPE_LOCAL->{
                            runBlocking {
                                mLocalSongs = withContext(Dispatchers.IO) {
                                    IMusicRoomHelper.getAllLocalSongs()
                                }
                            }
                            Log.e("JG","play---> localsongs:$mLocalSongs")
                        }
                        Constant.LIST_TYPE_LOVE->{
                            runBlocking {
                                mLoveSongs = withContext(Dispatchers.IO) {
                                    IMusicRoomHelper.getAllMyLoveSong()
                                }
                            }
                            mLoveSongs = orderLoveList(mLoveSongs)
                            Log.e("JG","play---> lovesongs:$mLoveSongs")
                        }
                        Constant.LIST_TYPE_HISTORY->{

                        }
                    }
                }
                mCurrent = SongUtil.getSong()?.position// 当前播放歌曲在列表中的位置
                Log.e("JG","mCurrent:$mCurrent")
                // 把各项参数恢复到初始状态
                mMediaPlayer.reset()
                when (mListType) {
                    Constant.LIST_TYPE_DOWNLOAD -> {
                        mDownloadedSongs?.let {
                            val url = it[mCurrent?:0].url
                            if (!url.isNullOrEmpty()) {
                                mMediaPlayer.setDataSource(url)
                                startPlay(restartTime)
                            }
                        }
                    }
                    Constant.LIST_TYPE_LOCAL->{
                        mLocalSongs?.let {
                            val url = it[mCurrent?:0].url
                            if (!url.isNullOrEmpty()) {
//                                Log.e("JG","播放url:$url")
                                mMediaPlayer.setDataSource(url)
                                startPlay(restartTime)
                            }
                        }
                    }
                    Constant.LIST_TYPE_LOVE->{
                        mLoveSongs?.let {
                            val url = it[mCurrent?:0].url
                            if (!url.isNullOrEmpty()) {
                                mMediaPlayer.setDataSource(url)
                                startPlay(restartTime)
                            }
                        }
                    }
                    Constant.LIST_TYPE_HISTORY->{
                        mHistorySongs?.let {
                            val url = it[mCurrent?:0].url
                            if (!url.isNullOrEmpty()) {
                                mMediaPlayer.setDataSource(url)
                                startPlay(restartTime)
                            }
                        }
                    }
                    else->{
                    }
                }
            } catch (e: Exception) {
                Log.e("JG","play error:${e.message}")
                playResult = Constant.PLAY_FAILED
                Toast.makeText(this@IMusicPlayService,R.string.play_error.getString(),Toast.LENGTH_SHORT).show()
            }
            return playResult
        }

        // 播放搜索歌曲
        fun playOnline(restartTime:Int?=null) {
            try {
                val song = SongUtil.getSong()
                mMediaPlayer.run {
                    reset()
                    setDataSource(song?.url ?: "")
//                    prepare()
                    //通过异步的方式装载媒体流
                    prepareAsync()
                    //装载完毕
                    setOnPreparedListener {
                        this@IMusicPlayService.mIsPlaying = true
                        saveToHistorySong()
                        if (restartTime != null && restartTime !=0) {
                            it?.seekTo(restartTime)
                        }
                        it.start()
                        // todo 发送网络歌曲改编事件

                        // 播放歌曲改变
//                      // 在[IMainActivity中监听]
                        Bus.post(Constant.SONG_STATUS_CHANGE,Constant.SONG_CHANGE)
                        // 改变通知栏歌曲
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            getNotification("${song?.songName} - ${song?.singer}")
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("JG","播放网络歌曲出错！！--->${e.message}")
            }
        }

        fun pause() {
            if (mMediaPlayer.isPlaying) {
                mIsPlaying = false
                mMediaPlayer.pause()
                mIsPause = true
                Bus.post(Constant.SONG_STATUS_CHANGE,Constant.SONG_PAUSE)
            }
        }

        fun resume() {
            if (mIsPause) {
                mMediaPlayer.start()
                mIsPlaying = true
                mIsPause = false
                Bus.post(Constant.SONG_STATUS_CHANGE,Constant.SONG_RESUME)
            }
        }

        fun next() {
            IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_RESUME)

            // 如果是网络歌曲 todo 播放网络 歌曲的另一首

            if (mListType != 0 && mListType != null) {
                mPlayStatusBinder.play(mListType!!)
            }
        }

        fun last() {
            IMusicBus.sendPlayStatusChangeEvent(Constant.SONG_RESUME)
            if (mListType != 0 && mListType != null) {
                mPlayStatusBinder.play(mListType!!)
            }
        }

        fun stop() {
            mIsPlaying = false
            mMediaPlayer.stop()
            try {
                //  在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
//                mMediaPlayer.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val isPlaying get() = mIsPlaying

        val isPausing get() = mIsPause

        val mediaPlayer get() = mMediaPlayer

        val playService get() = this@IMusicPlayService

        // 播放实时进度
        val currentTime get() = mMediaPlayer.currentPosition.toLong() / 1000

    }

    @Throws(IOException::class)
    private fun startPlay(restartTime:Int?=null) {
        mMediaPlayer.prepareAsync()
        mMediaPlayer.setOnPreparedListener {
            this@IMusicPlayService.mIsPlaying = true
            if (restartTime != null && restartTime !=0) {
                it?.seekTo(restartTime)
            }
            it.start()
            playResult = Constant.PLAY_SUCCESS
            saveToHistorySong()
            Bus.post(Constant.SONG_STATUS_CHANGE,Constant.SONG_CHANGE)
        }

        // todo 这里后面再优化
//        val song = SongUtil.getSong()

//        notificationManager.notify(NOTIFICATION_ID,
//        getNotification("${song?.songName}-${song?.singer}"))
    }

    private var job: Job? = null
    private fun saveToHistorySong() {
        val song = SongUtil.getSong()
        song?.let {
            job = GlobalScope.launch {
                val historySong = HistorySong().apply {
                    songId = it.songId
                    qqId = it.qqId
                    name = it.songName
                    singer = it.singer
                    url = it.url
                    pic = it.imgUrl
                    isOnline = it.isOnline
                    isDownload = it.isDownload
                    duration = it.duration
                    mediaId = it.mediaId
                }
                val saveResult = withContext(Dispatchers.IO) {
                    IMusicRoomHelper.saveToHistorySong(historySong)
                }
                if (saveResult != null) {
                    // 更新界面
                    withContext(Dispatchers.Main) {
                        IMusicBus.sendSongListNumChange(Constant.LIST_TYPE_HISTORY)
                        Log.e("JG", "保存最近播放曲目成功:  $saveResult")
                        // todo 如果最近播放的曲目>100 将第一个删除
                    }
                }
            }
        }
    }

    private fun saveDownloadInfo(current: Int) {
        val downloadSong = mDownloadedSongs?.get(current)
        downloadSong?.let {
            val song = Song().apply {
                position = current
                songId = it.songId
                songName = it.name
                singer = it.singer
                url = it.url
                imgUrl = it.pic
                listType = Constant.LIST_TYPE_DOWNLOAD
                isOnline = false
                duration = it.duration?.toInt() ?: 0
                mediaId = it.mediaId
                isDownload = true
                albumName = it.albumName
            }
            SongUtil.saveSong(song)
        }
    }

    private fun saveLocalSong(current: Int) {
        mLocalSongs = runBlocking {
            withContext(Dispatchers.IO) {
                IMusicRoomHelper.getAllLocalSongs()
            }
        }
        Log.e("JG","saveLocalSong---> localsongs:$mLocalSongs")
        val localSong = mLocalSongs?.let { it[current] }
        localSong?.let {
            val song = Song().apply {
                position = current
                songId = it.songId
                songName = it.name
                singer = it.singer
                url = it.url
                listType = Constant.LIST_TYPE_DOWNLOAD
                isOnline = false
                duration = it.duration?.toInt() ?: 0
                qqId = it.qqId
                listType = Constant.LIST_TYPE_LOCAL
            }
            SongUtil.saveSong(song)
        }
    }

    private fun saveLoveSong(current: Int) {
        mLoveSongs = runBlocking {
            withContext(Dispatchers.IO) {
                IMusicRoomHelper.getAllMyLoveSong()
            }
        }
        mLoveSongs = orderLoveList(mLoveSongs)
        Log.e("JG","saveLoveSong---> lovesongs:$mLocalSongs")
        val loveSong = mLoveSongs?.let { it[current] }
        loveSong?.let {
            val song = Song().apply {
                position = current
                songId = it.songId
                songName = it.name
                qqId = it.qqId
                singer = it.singer
                url = it.url
                imgUrl = it.pic
                listType = Constant.LIST_TYPE_LOVE
                mediaId = it.mediaId
                isOnline = it.isOnline?:false
                isDownload = it.isDownload?:false
                duration = it.duration?: 0
            }
            SongUtil.saveSong(song)
        }
    }
    private fun saveHistorySong(current: Int) {
        val history = mHistorySongs?.get(current)
        history?.let {
            val song = Song().apply {
                position = current
                songId = it.songId
                qqId = it.qqId
                songName = it.name
                singer = it.singer
                url = it.url
                imgUrl = it.pic
                listType = Constant.LIST_TYPE_HISTORY
                isOnline = it.isOnline
                duration = it.duration?:0
                mediaId = it.mediaId
                isDownload = it.isDownload
            }
            SongUtil.saveSong(song)
        }

    }


    private fun getNextSongPosition(current: Int,playMode:Int,len:Int):Int {
        return when (playMode) {
            Constant.PLAY_ORDER -> {
                (current + 1) % len
            }
            Constant.PLAY_RANDOM -> {
                (current + (Math.random() * len).toInt()) % len
            }
            else -> {
                current
            }
        }
    }

    private val notificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun getNotification(title: String): Notification {
        val pi = PendingIntent.getActivity(this, 0, Intent(this, IMainActivity::class.java), 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "play"
            val name = "播放歌曲"
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
            return Notification.Builder(this, id)
                .setSmallIcon(R.drawable.ic_nht)
                .setContentIntent(pi)
                .setContentTitle(title)
                .build()
        } else {
            return NotificationCompat.Builder(this, "play")
                .setSmallIcon(R.drawable.ic_nht)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_nht))
                .setContentIntent(pi)
                .setContentTitle(title)
                .build()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("JG", "IMusicPlayService onUnbind--->")
        return true
    }

    private fun orderDownloadList(tempList: MutableList<Downloaded>?): MutableList<Downloaded>? {
        val downloadSongList = mutableListOf<Downloaded>()
        downloadSongList.clear()
        return if (tempList != null) {
            for (i in tempList.indices.reversed()) {
                downloadSongList.add(tempList[i])
            }
            downloadSongList
        } else {
            null
        }
    }

    private fun orderLoveList(tempList: MutableList<LoveSong>?): MutableList<LoveSong>? {
        val loveSongList = mutableListOf<LoveSong>()
        loveSongList.clear()
        return if (tempList != null) {
            for (i in tempList.indices.reversed()) {
                loveSongList.add(tempList[i])
            }
            loveSongList
        } else {
            null
        }
    }

    override fun onDestroy() {
        Log.d("JG", "--->IMusicPlayService onDestroy")
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
            mMediaPlayer.release()
        }
        stopForeground(true)
        if (job?.isCancelled == false) {
            job?.cancel()
        }
    }

    /**
     *
     */
    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }
}