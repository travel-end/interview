package com.journey.interview.imusic.model

data class DownloadEvent(
    var downloadStatus:Int?=null,
    var downloadSong: DownloadSong?=null,
    var position:Int?=null
)