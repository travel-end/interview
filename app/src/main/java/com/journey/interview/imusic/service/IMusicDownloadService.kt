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
import com.journey.interview.imusic.IMainActivity
import com.journey.interview.imusic.download.IMusicDownloadListener
import com.journey.interview.imusic.download.IMusicDownloadTask
import com.journey.interview.imusic.global.IMusicBus
import com.journey.interview.imusic.model.DownloadEvent
import com.journey.interview.imusic.model.DownloadSong
import com.journey.interview.imusic.room.IMusicRoomHelper
import kotlinx.coroutines.*
import java.util.*

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
                            downloadQueue.offer(downloadSong)// 将歌曲放到等待队列中
                            // 发送全局消息 通知有一个新的下载任务（在正在下载页面会展示当前任务）
                            IMusicBus.sendDownloadSongStatusChange(DownloadEvent(downloadStatus = Constant.TYPE_DOWNLOAD_ADD))
                        }
                    }

                    if (downloadTask != null) {
//                        Toast.makeText(this@IMusicDownloadService, "已经加入下载队列", Toast.LENGTH_SHORT).show()
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
                                        IMusicBus.sendDownloadSongStatusChange(
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
                                            getNotification("正在下载: ${downloadingSong?.songName ?: ""}", 0)
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
            IMusicBus.sendDownloadSongStatusChange(
                DownloadEvent(
                    downloadStatus = Constant.DOWNLOAD_ING,
                    downloadSong = downloadSong
                )
            )
            if (downloadSong.progress != 100) {
                notificationManager.notify(
                    1,
                    getNotification("正在下载: ${downloadSong.songName}", downloadSong.progress ?: 0)
                )
            } else {
                if (downloadQueue.isEmpty()) {
                    Log.e("JG","download--->onProgress success")
                    notificationManager.notify(1, getNotification("下载成功~", -1))
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
            IMusicBus.sendSongListNumChange(Constant.LIST_TYPE_DOWNLOAD)
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
                .setSmallIcon(R.drawable.icon1)
                .setContentIntent(pi)
                .setContentTitle(title)
            if (progress > 0) {
                builder.setContentText("$progress%")
                builder.setProgress(100, progress, false)
            }
            return builder.build()
        } else {
            val builder = NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.icon1)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon1))
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