package com.journey.interview.imusic.util

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.journey.interview.Constant
import com.journey.interview.imusic.download.IMusicDownloadListener
import com.journey.interview.imusic.download.IMusicDownloadUtil.getSaveSongFile
import com.journey.interview.imusic.model.DownloadSong
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

class DownloadTask(private val mDownListener: IMusicDownloadListener) : AsyncTask<DownloadSong?, DownloadSong?, Int>() {
    private var isCanceled = false
    private var isPaused = false
    private var lastProgress: Long = 0

    override fun doInBackground(vararg params: DownloadSong?): Int {
        var `is`: InputStream? = null
        var saveFile: RandomAccessFile? = null
        var file: File? = null
        val downloadInfo = params[0]
        try {
            var downloadedLength: Long = 0 //记录已下载的文件长度
            val downloadUrl = downloadInfo?.url
            val url = Environment.getExternalStorageDirectory().toString() + "/IMusic/download/"
            Log.e("JG","文件下载路径：$url")
            val downloadFile = File(url)
            if (!downloadFile.exists()) {
                downloadFile.mkdirs()
            }
            //传过来的下载地址
            // http://ws.stream.qqmusic.qq.com/C400001DI2Jj3Jqve9.m4a?guid=358840384&vkey=2B9BF114492F203C3943D8AE38C83DD8FEEA5E628B18F7F4455CA9B5059040266D74EBD43E09627AA4419D379B6A9E1FC1E5D2104AC7BB50&uin=0&fromtag=66
            val contentLength = getContentLength(downloadUrl) //实际文件长度
            val fileName = getSaveSongFile(
                downloadInfo?.singer!!,
                downloadInfo.songName!!,
                downloadInfo.duration!!.toLong(),
                downloadInfo.songId!!,
                contentLength
            )
            file = File(downloadFile, fileName)
            if (file.exists()) {
                downloadedLength = file.length()
            }
            if (contentLength == 0L) {
                return Constant.TYPE_DOWNLOAD_FAILED
            } else if (contentLength == downloadedLength) { //已下载
                return Constant.TYPE_DOWNLOADED
            }
            val client = OkHttpClient()
            val request = Request.Builder() //断点下载，指定从哪个字节开始下载
                .addHeader("RANGE", "bytes=$downloadedLength-")
                .url(downloadUrl!!)
                .build()
            val response = client.newCall(request).execute()
            if (response != null) {
                `is` = response.body!!.byteStream()
                saveFile = RandomAccessFile(file, "rw")
                saveFile.seek(downloadedLength) //跳过已下载的字节
                val b = ByteArray(1024)
                var total = 0
                var len: Int
                while (`is`.read(b).also { len = it } != -1) {
                    if (isCanceled) {
                        return Constant.TYPE_DOWNLOAD_CANCELED
                    } else if (isPaused) {
                        return Constant.TYPE_DOWNLOAD_PAUSED
                    } else {
                        total += len
                        saveFile.write(b, 0, len)
                        val progress =
                            ((total + downloadedLength) * 100 / contentLength).toInt()
                        downloadInfo.progress = progress
                        downloadInfo.totalSize = contentLength
                        downloadInfo.currentSize = total + downloadedLength
                        publishProgress(downloadInfo)
                    }
                }
                response.body!!.close()
                return Constant.TYPE_DOWNLOAD_SUCCESS
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`?.close()
                saveFile?.close()
                if (isCanceled && file != null) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return Constant.TYPE_DOWNLOAD_FAILED
    }


    override fun onProgressUpdate(vararg values: DownloadSong?) {
        val downloadInfo = values[0]
        val progress = downloadInfo?.progress!!
        if (progress > lastProgress) {
            mDownListener.onProgress(downloadInfo)
            lastProgress = progress.toLong()
        }
    }

    override fun onPostExecute(status: Int) {
        when (status) {
            Constant.TYPE_DOWNLOAD_SUCCESS -> mDownListener.onSuccess()
            Constant.TYPE_DOWNLOAD_FAILED -> mDownListener.onFailed()
            Constant.TYPE_DOWNLOAD_PAUSED -> mDownListener.onPaused()
            Constant.TYPE_DOWNLOAD_CANCELED -> mDownListener.onCancel()
            Constant.TYPE_DOWNLOADED -> mDownListener.hasDownloaded()
            else -> {
            }
        }
    }

    fun pauseDownload() {
        isPaused = true
    }

    fun cancelDownload() {
        isCanceled = true
    }

    @Throws(IOException::class)
    private fun getContentLength(downloadUrl: String?): Long {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(downloadUrl!!)
            .build()
        val response = client.newCall(request).execute()
        if (response != null && response.isSuccessful) {
            val contentLength = response.body!!.contentLength()
            response.body!!.close()
            return contentLength
        }
        return 0
    }

}