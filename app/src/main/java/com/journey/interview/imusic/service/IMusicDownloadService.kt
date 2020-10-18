package com.journey.interview.imusic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.act.IMainActivity
import com.journey.interview.imusic.download.IMusicDownloadListener
import com.journey.interview.imusic.download.IMusicDownloadTask
import com.journey.interview.imusic.global.Bus
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.DownloadEvent
import com.journey.interview.imusic.model.DownloadSong
import com.journey.interview.imusic.room.IMusicRoomHelper
import kotlinx.coroutines.*
import java.util.*

/**
 * LinkedList：
 *  与ArrayList相比，LinkedList的增删操作效率更高，而查改操作效率较低。
    LinkedList 实现了List 接口，能对它进行列表操作。
    LinkedList 实现了Deque 接口，即能将LinkedList当作双端队列使用。
 增加元素：
public boolean add(E e)，链表末尾添加元素，返回是否成功；
public void add(int index, E element)，向指定位置插入元素；
public void addFirst(E e)，添加到第一个元素；
public void addLast(E e)，添加到最后一个元素；
public boolean offer(E e)，向链表末尾添加元素，返回是否成功；
public boolean offerFirst(E e)，头部插入元素，返回是否成功；
public boolean offerLast(E e)，尾部插入元素，返回是否成功；
 删除元素
public void clear()，清空链表；
public E removeFirst()，删除并返回第一个元素；
public E removeLast()，删除并返回最后一个元素；
public boolean remove(Object o)，删除某一元素，返回是否成功；
public E remove(int index)，删除指定位置的元素；
public E poll()，删除并返回第一个元素；
public E remove()，删除并返回第一个元素；
 查找元素
public boolean contains(Object o)，判断是否含有某一元素；
public E get(int index)，返回指定位置的元素；
public E getFirst(), 返回第一个元素；
public E getLast()，返回最后一个元素；
public int indexOf(Object o)，查找指定元素从前往后第一次出现的索引；
public int lastIndexOf(Object o)，查找指定元素最后一次出现的索引；
public E peek()，返回第一个元素；
public E element()，返回第一个元素；
public E peekFirst()，返回头部元素；
public E peekLast()，返回尾部元素；


 */
class IMusicDownloadService : Service() {
    // 等待隊列
    private val downloadQueue = LinkedList<DownloadSong>()
    private var mPosition: Int = 0// 下載歌曲在下載歌曲列表中的位置
    private var downloadTask: IMusicDownloadTask? = null
//    private var downloadTask: DownloadTask? = null
    private var downloadUrl: String? = null
    private val downloadBinder = DownloadBinder()
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return downloadBinder
    }

    inner class DownloadBinder : Binder() {
        fun startDownload(downloadSong: DownloadSong) {
            try {
//                postDownloadEvent(song)
                // 如果需要下載的表中有該條歌曲，則添加到下載隊列後跳過
                GlobalScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        IMusicRoomHelper.findDownloadSongBySongId(downloadSong.songId ?: "")
                    }
                    Log.e("JG", "下载队列：$result")
                    if (result != null && result.size != 0) {
//                        Toast.makeText(this@IMusicDownloadService, "已经加入下载队列", Toast.LENGTH_SHORT).show()
//                        val historyDownloadSong = result[0]
//                        historyDownloadSong.status = Constant.DOWNLOAD_WAIT
//                        withContext(Dispatchers.IO) {
//                            IMusicRoomHelper.saveToDownloadSong(historyDownloadSong)
//                        }
//                        withContext(Dispatchers.Main) {
//                            Bus.post(
//                                Constant.DOWNLOAD_EVENT,
//                                DownloadEvent(
//                                    downloadStatus = Constant.DOWNLOAD_PAUSED,
//                                    downloadSong = historyDownloadSong
//                                ))
//
//                            downloadQueue.offer(historyDownloadSong)
//                        }
                    } else {
                        mPosition = withContext(Dispatchers.IO) {
                            IMusicRoomHelper.queryAllDownloadSongs()?.size ?: 0
                        }
                        withContext(Dispatchers.IO) {
                            downloadSong.position = mPosition
                            downloadSong.status = Constant.DOWNLOAD_WAIT
                            IMusicRoomHelper.saveToDownloadSong(downloadSong)
                        }// 插入一条下载记录
                        withContext(Dispatchers.Main) {
                            downloadQueue.offer(downloadSong)// 将歌曲放到等待队列中
                            // 发送全局消息 通知有一个新的下载任务（在正在下载页面会展示当前任务）
//                            IMusicBus.sendDownloadSongStatusChange(DownloadEvent(downloadStatus = Constant.TYPE_DOWNLOAD_ADD))
                            Bus.post(Constant.DOWNLOAD_EVENT, DownloadEvent(downloadStatus = Constant.TYPE_DOWNLOAD_ADD))
                        }
                    }

                    if (downloadTask != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@IMusicDownloadService, "已经加入下载队列", Toast.LENGTH_SHORT).show()
                        }
                    } else {
//                        start()
                        if (downloadTask == null && !downloadQueue.isEmpty()) {
                            val downloadingSong = downloadQueue.peek()
                            Log.e("JG", "当前下载的歌曲详情：$downloadingSong")
                            val songList = withContext(Dispatchers.IO) {
                                IMusicRoomHelper.findDownloadSongBySongId(downloadingSong?.songId ?: "")
                            }
                            songList?.let {
                                if (it.isNotEmpty()) {
                                    withContext(Dispatchers.Main) {
                                        val currentDownloadInfo = it[0]
                                        currentDownloadInfo.status = Constant.DOWNLOAD_READY
                                        Bus.post(
                                            Constant.DOWNLOAD_EVENT,
                                            DownloadEvent(
                                                downloadStatus = Constant.TYPE_DOWNLOADING,
                                                downloadSong = currentDownloadInfo
                                            )
                                        )
                                        downloadUrl = currentDownloadInfo.url
//                                        Log.e("JG", "下载路径：$downloadUrl")
                                        downloadTask = IMusicDownloadTask(listener)
                                        Toast.makeText(this@IMusicDownloadService, "开始下载", Toast.LENGTH_SHORT).show()
                                        downloadTask!!.execute(currentDownloadInfo)
                                        notificationManager.notify(
                                            1,
                                            getNotification("正在下载: ${downloadingSong?.songName ?: "imusic"}", 0)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("JG","error:${e.message}")
                e.printStackTrace()
            }

        }

        fun pauseDownload(songId: String) {
            // 暂停的歌曲是否为当前下载的歌曲
            if (downloadTask != null && downloadQueue.peek().songId == songId) {
                downloadTask?.pauseDownload()
            } else {// 暂停的歌曲是下载队列的歌曲
                // 将该歌曲从下载队列中移除
                for (i in 0 until downloadQueue.size) {
                    val downloadSong = downloadQueue[i]
                    if (downloadSong.songId == songId) {
                        downloadQueue.removeAt(i)
                        updateDbOfPause(downloadSong.songId)
                        downloadSong.status = Constant.DOWNLOAD_PAUSED
                        IMusicBus.sendDownloadSongStatusChange(
                            DownloadEvent(
                                downloadStatus = Constant.DOWNLOAD_PAUSED,
                                downloadSong = downloadSong
                            )
                        )
                    }
                }
            }
        }

        fun cancelDownload(song: DownloadSong) {
            val songId = song.songId
            if (songId != null) {
                // 如果该歌曲正在下载，则需要将downloadTask置为null
                if (downloadTask != null && downloadQueue.peek().songId == songId) {
                    downloadTask?.cancelDownload()
                }
                // 将该歌曲从下载队列中移除
                for (i in 0 until downloadQueue.size) {
                    val downloadSong = downloadQueue[i]
                    if (downloadSong.songId == songId) {
                        downloadQueue.removeAt(i)
                    }
                }

            }
        }

    }

    /**
     * 暂停时更新列表歌曲状态
     */
    private fun updateDbOfPause(songId: String?) {
        GlobalScope.launch {
            val statusList = IMusicRoomHelper.findDownloadSongBySongId(songId ?: "")
            if (statusList != null) {
                val downloadSong = statusList[0]
                downloadSong.status = Constant.DOWNLOAD_PAUSED
                IMusicRoomHelper.saveToDownloadSong(downloadSong)
            }
        }
    }

    private fun start() {
        Log.e("JG", "start")
        if (downloadTask == null && !downloadQueue.isEmpty()) {
            val downloadSong = downloadQueue.peek()
            Log.e("JG", "当前下载的歌曲详情：$downloadSong")
            job = GlobalScope.launch {
                val songList = withContext(Dispatchers.IO) {
                    IMusicRoomHelper.findDownloadSongBySongId(downloadSong?.songId ?: "")
                }
                songList?.let {
                    if (it.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            val currentDownloadInfo = it[0]
                            currentDownloadInfo.status = Constant.DOWNLOAD_READY
                            IMusicBus.sendDownloadSongStatusChange(
                                DownloadEvent(
                                    downloadStatus = Constant.TYPE_DOWNLOADING,
                                    downloadSong = currentDownloadInfo
                                )
                            )
                            downloadUrl = currentDownloadInfo.url
                            Log.e("JG", "下载路径：$downloadUrl")
                            downloadTask = IMusicDownloadTask(listener)
                            downloadTask!!.execute(currentDownloadInfo)
                            notificationManager.notify(
                                1,
                                getNotification("正在下载: ${downloadSong?.songName ?: ""}", 0)
                            )
                        }
                    }
                }
            }
        }
    }

    private val notificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val listener = object : IMusicDownloadListener {
        override fun onProgress(downloadSong: DownloadSong) {
            downloadSong.status = Constant.DOWNLOAD_ING
            Bus.post(
                Constant.DOWNLOAD_EVENT,
                DownloadEvent(
                    downloadStatus = Constant.DOWNLOAD_ING,
                    downloadSong = downloadSong
                )
            )
            Log.e("JG","下载进度：${downloadSong.progress}")
            if (downloadSong.progress != 100) {
                notificationManager.notify(
                    1,
                    getNotification("正在下载: ${downloadSong.songName}", downloadSong.progress ?: 0)
                )
            } else {
                if (downloadQueue.isEmpty()) {
                    Log.e("JG","download--->onProgress success")
                    notificationManager.notify(1, getNotification("全部歌曲下载已完成", -1))
                }
            }
        }

        override fun onSuccess() {
            Log.e("JG","download--->onSuccess")
            downloadTask = null
            val downloadSong = downloadQueue.poll()
            stopForeground(true)
            if (downloadQueue.isEmpty()) {
                notificationManager.notify(1, getNotification("下载成功啦~", -1))
            }
//            Bus.post(Constant.EVENT_LIST_TYPE,Constant.LIST_TYPE_DOWNLOAD)
            Bus.post(Constant.DOWNLOAD_RESULT,Constant.TYPE_DOWNLOAD_SUCCESS)
        }

        override fun hasDownloaded() {
        }

        override fun onFailed() {
            downloadTask = null
            stopForeground(true)
            notificationManager.notify(1, getNotification("下载失败", -1))
        }
        override fun onPaused() {
        }

        override fun onCancel() {
        }
    }
    private var postJob:Job?=null

    /**
     * 通知正在下載界面
     * runBlocking: 会阻塞当前线程
     */
    private fun postDownloadEvent(downloadSong: DownloadSong) {
        GlobalScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.findDownloadSongBySongId(downloadSong.songId ?: "")
            }
            Log.e("JG", "已下载歌曲查询结果：$result")
            if (result != null && result.size != 0) {
                val historyDownloadSong = result[0]
                historyDownloadSong.status = Constant.DOWNLOAD_WAIT
                withContext(Dispatchers.IO) {
                    IMusicRoomHelper.saveToDownloadSong(historyDownloadSong)
                }
                withContext(Dispatchers.Main) {
                    IMusicBus.sendDownloadSongStatusChange(
                        DownloadEvent(
                            downloadStatus = Constant.DOWNLOAD_PAUSED,
                            downloadSong = historyDownloadSong
                        )
                    )
                    downloadQueue.offer(historyDownloadSong)
                }
            } else {
                mPosition = withContext(Dispatchers.IO) {
                    IMusicRoomHelper.queryAllDownloadSongs()?.size ?: 0
                }
                withContext(Dispatchers.IO) {
                    downloadSong.position = mPosition
                    downloadSong.status = Constant.DOWNLOAD_WAIT
                    IMusicRoomHelper.saveToDownloadSong(downloadSong)
                }
                withContext(Dispatchers.Main) {
                    Log.e("JG", "添加一个下载任务")
                    downloadQueue.offer(downloadSong)// 将歌曲放到等待队列中
                    // 发送全局消息 通知有一个新的下载任务（在正在下载页面会展示当前任务）
                    IMusicBus.sendDownloadSongStatusChange(DownloadEvent(downloadStatus = Constant.TYPE_DOWNLOAD_ADD))
                }
            }
        }
    }

    private fun getNotification(title: String, progress: Int): Notification {
        val intent = Intent(this, IMainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "channel_01"
            val name = "下载通知"
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
            val builder = Notification.Builder(this, id)
                .setSmallIcon(R.drawable.ic_nht)
                .setContentIntent(pi)
                .setContentTitle(title)
            if (progress > 0) {
                builder.setContentText("$progress%")
                builder.setProgress(100, progress, false)
            }
            return builder.build()
        } else {
            val builder = NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_nht)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_nht))
                .setContentIntent(pi)
                .setContentTitle(title)
            if (progress > 0) {
                builder.setContentText("$progress%")
                builder.setProgress(100, progress, false)
            }
            return builder.build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (job?.isCancelled == false) {
            job?.cancel()
        }
        if (postJob?.isCancelled == false) {
            postJob?.cancel()
        }
    }
}