package com.journey.interview.imusic.model

/**
 * @By Journey 2020/10/12
 * @Description
 */
data class SingerImg(
    /**
     * result : {"artistCount":1,"artists":[{"id":840134,"name":"刘瑞琦","picUrl":"http://p1.music.126.net/qTDkcmWPMK3U54RNC0IgMw==/109951163288035254.jpg","alias":[],"albumSize":20,"picId":109951163288035254,"img1v1Url":"http://p1.music.126.net/a13xmSNqxMY5M_R1OFvPvA==/109951163288038157.jpg","accountId":3788031,"img1v1":109951163288038157,"mvSize":16,"followed":false,"trans":null}]}
     * code : 200
     */
    val result:ResultBean?,
    val code:Int
)

data class ResultBean(
    /**
     * artistCount : 1
     * artists : [{"id":840134,"name":"刘瑞琦","picUrl":"http://p1.music.126.net/qTDkcmWPMK3U54RNC0IgMw==/109951163288035254.jpg","alias":[],"albumSize":20,"picId":109951163288035254,"img1v1Url":"http://p1.music.126.net/a13xmSNqxMY5M_R1OFvPvA==/109951163288038157.jpg","accountId":3788031,"img1v1":109951163288038157,"mvSize":16,"followed":false,"trans":null}]
     */
    val artists:List<ArtistsBean>?
)

data class ArtistsBean(
    /**
     * /**
     * id : 840134
     * name : 刘瑞琦
     * picUrl : http://p1.music.126.net/qTDkcmWPMK3U54RNC0IgMw==/109951163288035254.jpg
     * alias : []
     * albumSize : 20
     * picId : 109951163288035254
     * img1v1Url : http://p1.music.126.net/a13xmSNqxMY5M_R1OFvPvA==/109951163288038157.jpg
     * accountId : 3788031
     * img1v1 : 109951163288038157
     * mvSize : 16
     * followed : false
     * trans : null
    */

     */
    val id:Int?,
    val name:String?,
    val picUrl:String?,
    val img1v1Url:String?
)