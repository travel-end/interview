package com.journey.interview.imusic.download

import android.os.AsyncTask
import android.util.Log
import com.journey.interview.Constant
import com.journey.interview.imusic.model.DownloadSong
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

class IMusicDownloadTask(private val listener:IMusicDownloadListener) :AsyncTask<DownloadSong,DownloadSong,Int>(){
    private var mIsCancel :Boolean = false
    private var mIsPause:Boolean = false
    private var lastProgress:Long=0L


    override fun doInBackground(vararg params: DownloadSong?): Int {
        var inputStream:InputStream?=null
        var raf : RandomAccessFile?=null
        var file :File?=null
        val downloadSong = params[0]
        downloadSong?.let {song->
            try {
                // 记录已下载的文件长度
                var downloadedLength =0L
                val downloadUrl = song.url
                val downloadFile = File(Constant.STORAGE_SONG_FILE)
//                Log.e("JG","文件下载储存路径：${Constant.STORAGE_SONG_FILE}")
                if (!downloadFile.exists()) {
                    downloadFile.mkdirs()
                }
                //传过来的下载地址
                // http://ws.stream.qqmusic.qq.com/C400001DI2Jj3Jqve9.m4a?guid=358840384&vkey=2B9BF114492F203C3943D8AE38C83DD8FEEA5E628B18F7F4455CA9B5059040266D74EBD43E09627AA4419D379B6A9E1FC1E5D2104AC7BB50&uin=0&fromtag=66

                // 实际文件长度
                val contentLength = getContentLength(downloadUrl?:"")
                val fileName = IMusicDownloadUtil.getSaveSongFile(
                    downloadSong.singer?:"",
                    downloadSong.songName?:"",
                    downloadSong.duration?.toLong()?:0L,
                    downloadSong.songId?:"",
                    contentLength
                )
                Log.e("JG","下载详情：$fileName")
                file = File(downloadFile,fileName)
                if (file?.exists()==true) {
                    downloadedLength = file?.length()?:0
                }
                if (contentLength == 0L) {
                    return Constant.TYPE_DOWNLOAD_FAILED
                } else if (contentLength == downloadedLength) {
                    return Constant.TYPE_DOWNLOADED
                }
                listener.onStart()
                val client = OkHttpClient()
                val request = Request.Builder()
                //断点下载  指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=$downloadedLength-")
                    .url(downloadUrl?:"")
                    .build()
                val response = client.newCall(request).execute()
                inputStream = response.body?.byteStream()
                raf = RandomAccessFile(file,"rw")
                raf?.seek(downloadedLength)// 跳过已经下载的字节
                val b= ByteArray(1024)
                var total = 0
                var len:Int

                while (inputStream!!.read(b).also { len = it } != -1) {
                    if (mIsCancel) {
                        return Constant.TYPE_DOWNLOAD_CANCELED
                    } else if (mIsPause) {
                        return Constant.TYPE_DOWNLOAD_PAUSED
                    } else {
                        total += len
                        raf?.write(b,0,len)
                        val progress = ((total + downloadedLength) * 100 / contentLength).toInt()
                        downloadSong.progress = progress
                        downloadSong.totalSize = contentLength
                        downloadSong.currentSize = total+ downloadedLength
                        publishProgress(downloadSong)
                    }
                }
                response.body?.close()
                return Constant.TYPE_DOWNLOAD_SUCCESS
            } catch (e:Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream?.close()
                    }
                    if (raf != null) {
                        raf?.close()
                    }
                    if (mIsCancel && file != null) {
                        file?.delete()
                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                }
            }
        }
        return Constant.TYPE_DOWNLOAD_FAILED
    }

    override fun onProgressUpdate(vararg values: DownloadSong?) {
        val downloadSong = values[0]
        if (downloadSong != null) {
            val progress = downloadSong.progress
            if (progress != null && progress > lastProgress) {
                listener.onProgress(downloadSong)
                lastProgress = progress.toLong()
            }
        }

    }

    override fun onPostExecute(result: Int?) {
        result?.let {
            when(it) {
                Constant.TYPE_DOWNLOAD_SUCCESS->listener.onSuccess()
                Constant.TYPE_DOWNLOAD_FAILED->listener.onFailed()
                Constant.TYPE_DOWNLOAD_PAUSED->listener.onPaused()
                Constant.TYPE_DOWNLOAD_CANCELED->listener.onCancel()
                Constant.TYPE_DOWNLOADED->listener.hasDownloaded()
            }
        }
    }
    fun pauseDownload() {
        mIsPause = true
    }
    fun cancelDownload() {
        mIsCancel= true
    }

    @Throws(IOException::class)
    private fun getContentLength(downloadUrl:String) :Long  {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(downloadUrl)
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val contentLength = response.body?.contentLength()
            response.body?.close()
            return contentLength?:0
        }
        return 0
    }
}