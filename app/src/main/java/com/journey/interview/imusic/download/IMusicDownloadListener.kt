package com.journey.interview.imusic.download

import com.journey.interview.imusic.model.DownloadSong

interface IMusicDownloadListener {
    // 進度
    fun onProgress(downloadSong: DownloadSong)

    //成功
    fun onSuccess()

    // 已经下载过的歌曲
    fun hasDownloaded()

    // 失败
    fun onFailed()

    // 暂停
    fun onPaused()

    // 取消
    fun onCancel()

    fun onStart()
}