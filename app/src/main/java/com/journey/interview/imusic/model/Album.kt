package com.journey.interview.imusic.model

/**
 * Created by 残渊 on 2018/11/25.
 */
data class Album(
    /**
     * code : 200
     * msg : OK
     * timestamp : 1558855295121
     * data : {"curnum":1,"curpage":1,"totalnum":113,"list":[{"albumName":"周杰伦的床边故事","singerMID":"0025NhlN2yWrP4","singerName_hilight":"*周杰伦<\/em>","docid":"3609733955036397641","singer_list":[{"name":"周杰伦","name_hilight":"*周杰伦<\/em>","mid":"0025NhlN2yWrP4","id":4558}],"albumMID":"003RMaRI1iFoYd","albumID":1458791,"albumPic":"http://y.gtimg.cn/music/photo_new/T002R180x180M000003RMaRI1iFoYd.jpg","type":0,"singerName":"周杰伦","albumName_hilight":"*周杰伦<\/em>的床边故事","publicTime":"2016-06-24","singerID":4558,"song_count":10,"catch_song":""}]}
     *** */
    var code: Int = 0,
    var msg: String? = null,
    var data: AlDataBean? = null
)


data class AlDataBean(
    val album: AlbumBean? = null
)

data class AlbumBean(
    /**
     * curnum : 1
     * curpage : 1
     * totalnum : 113
     * list : [{"albumName":"周杰伦的床边故事","singerMID":"0025NhlN2yWrP4","singerName_hilight":"*周杰伦<\/em>","docid":"3609733955036397641","singer_list":[{"name":"周杰伦","name_hilight":"*周杰伦<\/em>","mid":"0025NhlN2yWrP4","id":4558}],"albumMID":"003RMaRI1iFoYd","albumID":1458791,"albumPic":"http://y.gtimg.cn/music/photo_new/T002R180x180M000003RMaRI1iFoYd.jpg","type":0,"singerName":"周杰伦","albumName_hilight":"*周杰伦<\/em>的床边故事","publicTime":"2016-06-24","singerID":4558,"song_count":10,"catch_song":""}]
     *** */
    var curnum: Int = 0,
    var curpage: Int = 0,
    var totalnum: Int = 0,
    var list: List<AlListBean>? = null
)

data class AlListBean(
    /**
     * albumName : 周杰伦的床边故事
     * singerMID : 0025NhlN2yWrP4
     * singerName_hilight : *周杰伦*
     * docid : 3609733955036397641
     * singer_list : [{"name":"周杰伦","name_hilight":"*周杰伦<\/em>","mid":"0025NhlN2yWrP4","id":4558}]
     * albumMID : 003RMaRI1iFoYd
     * albumID : 1458791
     * albumPic : http://y.gtimg.cn/music/photo_new/T002R180x180M000003RMaRI1iFoYd.jpg
     * type : 0
     * singerName : 周杰伦
     * albumName_hilight : *周杰伦*的床边故事
     * publicTime : 2016-06-24
     * singerID : 4558
     * song_count : 10
     * catch_song :
     * */
    var albumName: String? = null,
    var singerMID: String? = null,
    var singerName_hilight: String? = null,
    var docid: String? = null,
    var albumMID: String? = null,
    var albumID: Int = 0,
    var albumPic: String? = null,
    var type: Int = 0,
    var singerName: String? = null,
    var albumName_hilight: String? = null,
    var publicTime: String? = null,
    var singerID: Int = 0,
    var song_count: Int = 0,
    var catch_song: String? = null,
    var singer_list: List<AlSingerListBean>? = null
)


data class AlSingerListBean(
    /**
     * name : 周杰伦
     * name_hilight : *周杰伦*
     * mid : 0025NhlN2yWrP4
     * id : 4558
     */
    var name: String? = null,
    var name_hilight: String? = null,
    var mid: String? = null,
    var id: Int = 0
)