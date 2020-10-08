package com.journey.interview.imusic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class IMusicDownloadService:Service() {
    // 等待隊列
    private val downloadQueue = LinkedList<DownloadSong>()
    private var mPosition:Int=0// 下載歌曲在下載歌曲列表中的位置
    private var downloadTask:IMusicDownloadTask?=null
    private var downloadUrl:String?=null
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    inner class DownloadBinder:Binder() {
        fun startDownload(song: DownloadSong) {
            try {
                postDownloadEvent(song)
            } catch (e:Exception) {
                e.printStackTrace()
            }
            if (downloadTask != null) {
                Toast.makeText(this@IMusicDownloadService,"已经加入下载队列",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@IMusicDownloadService,"开始下载",Toast.LENGTH_SHORT).show()
                start()
            }
        }

    }

    private fun start() {
        if (downloadTask == null && !downloadQueue.isEmpty()) {
            val downloadSong = downloadQueue.peek()
            GlobalScope.launch {
                val songList = IMusicRoomHelper.findDownloadSongBySongId(downloadSong?.songId?:"")
                songList?.let {
                    if (it.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            val currentDownloadInfo = it[0]
                            currentDownloadInfo.status = Constant.DOWNLOAD_READY
                            IMusicBus.sendDownloadSongStatusChange(DownloadEvent(downloadStatus = Constant.TYPE_DOWNLOADING,downloadSong = currentDownloadInfo))
                            downloadUrl = currentDownloadInfo.url
                            downloadTask = IMusicDownloadTask(listener)
                            downloadTask!!.execute()
                            notificationManager.notify(1,getNotification("正在下载: ${downloadSong?.songName?:""}",0))

                        }
                    }

                }
            }
        }
    }
    private val notificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val listener = object :IMusicDownloadListener {
        override fun onProgress(downloadSong: DownloadSong) {
            TODO("Not yet implemented")
        }

        override fun onSuccess() {
            TODO("Not yet implemented")
        }

        override fun hasDownloaded() {
            TODO("Not yet implemented")
        }

        override fun onFailed() {
            TODO("Not yet implemented")
        }

        override fun onPaused() {
            TODO("Not yet implemented")
        }

        override fun onCancel() {
            TODO("Not yet implemented")
        }

    }

    /**
     * 通知正在下載界面
     */
    private fun postDownloadEvent(downloadSong: DownloadSong) {
        // 如果需要下載的表中有該條歌曲，則添加到下載隊列後跳過
        GlobalScope.launch {
            val result = withContext(Dispatchers.IO) {
                IMusicRoomHelper.findDownloadSongBySongId(downloadSong.songId?:"")
            }
            if (result != null && result.size != 0) {
                val historyDownloadSong = result[0]
                historyDownloadSong.status = Constant.DOWNLOAD_WAIT
                IMusicRoomHelper.saveToDownloadSong(historyDownloadSong)
                withContext(Dispatchers.Main) {
                    IMusicBus.sendDownloadSongStatusChange(DownloadEvent(downloadStatus = Constant.DOWNLOAD_PAUSED,downloadSong = historyDownloadSong))
                    downloadQueue.offer(historyDownloadSong)
                }
            } else {
                mPosition = IMusicRoomHelper.queryAllDownloadSongs()?.size?:0
                downloadSong.position = mPosition
                downloadSong.status = Constant.DOWNLOAD_WAIT
                IMusicRoomHelper.saveToDownloadSong(downloadSong)
                downloadQueue.offer(downloadSong)
                withContext(Dispatchers.Main) {
                    IMusicBus.sendDownloadSongStatusChange(DownloadEvent(downloadStatus = Constant.TYPE_DOWNLOAD_ADD))
                }
            }
        }
    }

    private fun getNotification(title:String,progress:Int) :Notification {
        val intent = Intent(this,IMainActivity::class.java)
        val pi = PendingIntent.getActivity(this,0,intent,0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "channel_01"
            val name = "下载通知"
            val channel = NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW)
            val builder = Notification.Builder(this,id)
                .setSmallIcon(R.drawable.icon1)
                .setContentIntent(pi)
                .setContentTitle(title)
            if (progress > 0) {
                builder.setContentText("$progress%")
                builder.setProgress(100,progress,false)
            }
            return builder.build()
        } else {
            val builder = NotificationCompat.Builder(this,"default")
                .setSmallIcon(R.drawable.icon1)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.icon1))
                .setContentIntent(pi)
                .setContentTitle(title)
            if (progress>0) {
                builder.setContentText("$progress%")
                builder.setProgress(100,progress,false)
            }
            return builder.build()
        }
    }
}