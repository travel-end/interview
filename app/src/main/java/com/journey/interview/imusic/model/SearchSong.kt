package com.journey.interview.imusic.model

/**
 * <pre>
 * author : 残渊
 * time   : 2019/08/31
 * desc   :
</pre> *
 */
data class SearchSong (
    /**
     * code : 0
     * data : {"keyword":"邓紫棋","priority":0,"qc":[],"semantic":{"curnum":0,"curpage":1,"list":[],"totalnum":0},"song":{"curnum":2,"curpage":1,"list":[{"albumid":7806097,"albummid":"001F3IM92zEXSX","albumname":"中国新说唱2019 第12期","albumname_hilight":"中国新说唱2019 第12期","alertid":23,"belongCD":0,"cdIdx":6,"chinesesinger":0,"docid":"1263425476867044852","grp":[],"interval":232,"isonly":0,"lyric":"","lyric_hilight":"","media_mid":"003XrSXL43XhhP","msgid":16,"newStatus":1,"nt":3078011703,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":0,"tryend":0,"trysize":960887},"pubtime":1567166403,"pure":0,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋","name_hilight":"G.E.M. *邓紫棋<\/em>"},{"id":3344622,"mid":"003MDz1629ej3X","name":"OBi","name_hilight":"OBi"},{"id":2626134,"mid":"001yBcHD4adsSA","name":"刘炫廷","name_hilight":"刘炫廷"},{"id":2085196,"mid":"001eBoUt4SiKgg","name":"Capper","name_hilight":"Capper"}],"size128":3728026,"size320":9319693,"sizeape":0,"sizeflac":0,"sizeogg":5221536,"songid":237274487,"songmid":"003XrSXL43XhhP","songname":"We Are Young (Live)","songname_hilight":"We Are Young (Live)","strMediaMid":"003XrSXL43XhhP","stream":1,"switch":17413891,"t":1,"tag":10,"type":0,"ver":0,"vid":""},{"albumid":1196826,"albummid":"003c616O2Zlswm","albumname":"新的心跳","albumname_hilight":"新的心跳","alertid":2,"belongCD":0,"cdIdx":4,"chinesesinger":0,"docid":"3872246129178486611","grp":[],"interval":245,"isonly":0,"lyric":"","lyric_hilight":"","media_mid":"001DI2Jj3Jqve9","msgid":16,"newStatus":2,"nt":2292267207,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":0,"tryend":0,"trysize":0},"pubtime":1446739200,"pure":0,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋","name_hilight":"G.E.M. *邓紫棋<\/em>"}],"size128":3929034,"size320":9822259,"sizeape":0,"sizeflac":29903656,"sizeogg":5405648,"songid":104913384,"songmid":"004dFFPd4JNv8q","songname":"来自天堂的魔鬼","songname_hilight":"来自天堂的魔鬼","strMediaMid":"001DI2Jj3Jqve9","stream":1,"switch":636675,"t":1,"tag":11,"type":0,"ver":0,"vid":"v001892io9b"}],"totalnum":416},"tab":0,"taglist":[],"totaltime":0,"zhida":{"chinesesinger":0,"type":0}}
     * message :
     * notice :
     * subcode : 0
     * time : 1567229228
     * tips :
     ** */
    var code:Int = 0,
    var data: DataBean? = null
)


data class DataBean (
    /**
     * keyword : 邓紫棋
     * priority : 0
     * qc : []
     * semantic : {"curnum":0,"curpage":1,"list":[],"totalnum":0}
     * song : {"curnum":2,"curpage":1,"list":[{"albumid":7806097,"albummid":"001F3IM92zEXSX","albumname":"中国新说唱2019 第12期","albumname_hilight":"中国新说唱2019 第12期","alertid":23,"belongCD":0,"cdIdx":6,"chinesesinger":0,"docid":"1263425476867044852","grp":[],"interval":232,"isonly":0,"lyric":"","lyric_hilight":"","media_mid":"003XrSXL43XhhP","msgid":16,"newStatus":1,"nt":3078011703,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":0,"tryend":0,"trysize":960887},"pubtime":1567166403,"pure":0,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋","name_hilight":"G.E.M. *邓紫棋<\/em>"},{"id":3344622,"mid":"003MDz1629ej3X","name":"OBi","name_hilight":"OBi"},{"id":2626134,"mid":"001yBcHD4adsSA","name":"刘炫廷","name_hilight":"刘炫廷"},{"id":2085196,"mid":"001eBoUt4SiKgg","name":"Capper","name_hilight":"Capper"}],"size128":3728026,"size320":9319693,"sizeape":0,"sizeflac":0,"sizeogg":5221536,"songid":237274487,"songmid":"003XrSXL43XhhP","songname":"We Are Young (Live)","songname_hilight":"We Are Young (Live)","strMediaMid":"003XrSXL43XhhP","stream":1,"switch":17413891,"t":1,"tag":10,"type":0,"ver":0,"vid":""},{"albumid":1196826,"albummid":"003c616O2Zlswm","albumname":"新的心跳","albumname_hilight":"新的心跳","alertid":2,"belongCD":0,"cdIdx":4,"chinesesinger":0,"docid":"3872246129178486611","grp":[],"interval":245,"isonly":0,"lyric":"","lyric_hilight":"","media_mid":"001DI2Jj3Jqve9","msgid":16,"newStatus":2,"nt":2292267207,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":0,"tryend":0,"trysize":0},"pubtime":1446739200,"pure":0,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋","name_hilight":"G.E.M. *邓紫棋<\/em>"}],"size128":3929034,"size320":9822259,"sizeape":0,"sizeflac":29903656,"sizeogg":5405648,"songid":104913384,"songmid":"004dFFPd4JNv8q","songname":"来自天堂的魔鬼","songname_hilight":"来自天堂的魔鬼","strMediaMid":"001DI2Jj3Jqve9","stream":1,"switch":636675,"t":1,"tag":11,"type":0,"ver":0,"vid":"v001892io9b"}],"totalnum":416}
     * tab : 0
     * taglist : []
     * totaltime : 0
     * zhida : {"chinesesinger":0,"type":0}
     ** */
    var song: SongBean? = null
)

data class SongBean (
    /**
     * curnum : 2
     * curpage : 1
     * list : [{"albumid":7806097,"albummid":"001F3IM92zEXSX","albumname":"中国新说唱2019 第12期","albumname_hilight":"中国新说唱2019 第12期","alertid":23,"belongCD":0,"cdIdx":6,"chinesesinger":0,"docid":"1263425476867044852","grp":[],"interval":232,"isonly":0,"lyric":"","lyric_hilight":"","media_mid":"003XrSXL43XhhP","msgid":16,"newStatus":1,"nt":3078011703,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":0,"tryend":0,"trysize":960887},"pubtime":1567166403,"pure":0,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋","name_hilight":"G.E.M. *邓紫棋<\/em>"},{"id":3344622,"mid":"003MDz1629ej3X","name":"OBi","name_hilight":"OBi"},{"id":2626134,"mid":"001yBcHD4adsSA","name":"刘炫廷","name_hilight":"刘炫廷"},{"id":2085196,"mid":"001eBoUt4SiKgg","name":"Capper","name_hilight":"Capper"}],"size128":3728026,"size320":9319693,"sizeape":0,"sizeflac":0,"sizeogg":5221536,"songid":237274487,"songmid":"003XrSXL43XhhP","songname":"We Are Young (Live)","songname_hilight":"We Are Young (Live)","strMediaMid":"003XrSXL43XhhP","stream":1,"switch":17413891,"t":1,"tag":10,"type":0,"ver":0,"vid":""},{"albumid":1196826,"albummid":"003c616O2Zlswm","albumname":"新的心跳","albumname_hilight":"新的心跳","alertid":2,"belongCD":0,"cdIdx":4,"chinesesinger":0,"docid":"3872246129178486611","grp":[],"interval":245,"isonly":0,"lyric":"","lyric_hilight":"","media_mid":"001DI2Jj3Jqve9","msgid":16,"newStatus":2,"nt":2292267207,"pay":{"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200},"preview":{"trybegin":0,"tryend":0,"trysize":0},"pubtime":1446739200,"pure":0,"singer":[{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋","name_hilight":"G.E.M. *邓紫棋<\/em>"}],"size128":3929034,"size320":9822259,"sizeape":0,"sizeflac":29903656,"sizeogg":5405648,"songid":104913384,"songmid":"004dFFPd4JNv8q","songname":"来自天堂的魔鬼","songname_hilight":"来自天堂的魔鬼","strMediaMid":"001DI2Jj3Jqve9","stream":1,"switch":636675,"t":1,"tag":11,"type":0,"ver":0,"vid":"v001892io9b"}]
     * totalnum : 416
     ** */
    var curnum:Int = 0,
    var curpag:Int = 0,
    var totalnum :Int= 0,
    var list: List<ListBean>? = null
)

data class SingerBean (
    /**
     * id : 13948
     * mid : 001fNHEf1SFEFN
     * name : G.E.M. 邓紫棋
     * name_hilight : G.E.M. *邓紫棋*
     */
    var id :Int= 0,
    var mid: String? = null,
    var name: String? = null,
    var name_hilight: String? = null

)

data class ListBean (
    /**
     * albumid : 7806097
     * albummid : 001F3IM92zEXSX
     * albumname : 中国新说唱2019 第12期
     * albumname_hilight : 中国新说唱2019 第12期
     * alertid : 23
     * belongCD : 0
     * cdIdx : 6
     * chinesesinger : 0
     * docid : 1263425476867044852
     * grp : []
     * interval : 232
     * isonly : 0
     * lyric :
     * lyric_hilight :
     * media_mid : 003XrSXL43XhhP
     * msgid : 16
     * newStatus : 1
     * nt : 3078011703
     * pay : {"payalbum":0,"payalbumprice":0,"paydownload":1,"payinfo":1,"payplay":0,"paytrackmouth":1,"paytrackprice":200}
     * preview : {"trybegin":0,"tryend":0,"trysize":960887}
     * pubtime : 1567166403
     * pure : 0
     * singer : [{"id":13948,"mid":"001fNHEf1SFEFN","name":"G.E.M. 邓紫棋","name_hilight":"G.E.M. *邓紫棋<\/em>"},{"id":3344622,"mid":"003MDz1629ej3X","name":"OBi","name_hilight":"OBi"},{"id":2626134,"mid":"001yBcHD4adsSA","name":"刘炫廷","name_hilight":"刘炫廷"},{"id":2085196,"mid":"001eBoUt4SiKgg","name":"Capper","name_hilight":"Capper"}]
     * size128 : 3728026
     * size320 : 9319693
     * sizeape : 0
     * sizeflac : 0
     * sizeogg : 5221536
     * songid : 237274487
     * songmid : 003XrSXL43XhhP
     * songname : We Are Young (Live)
     * songname_hilight : We Are Young (Live)
     * strMediaMid : 003XrSXL43XhhP
     * stream : 1
     * switch : 17413891
     * t : 1
     * tag : 10
     * type : 0
     * ver : 0
     * vid :
     * */
    var albumid:Int = 0,
    var albummid: String? = null,
    var albumname: String? = null,
    var albumname_hilight: String? = null,
    var alertid:Int = 0,
    var belongCD:Int = 0,
    var cdIdx:Int = 0,
    var chinesesinger:Int = 0,
    var docid: String? = null,
    var interval:Int = 0,
    var isonly :Int= 0,
    var lyric: String? = null,
    var lyric_hilight: String? = null,
    var media_mid: String? = null,
    var msgid:Int = 0,
    var newStatus:Int = 0,
    var nt: Long = 0,
    var pubtime:Int = 0,
    var pure:Int = 0,
    var size128:Int = 0,
    var size320:Int = 0,
    var sizeape:Int = 0,
    var sizeflac:Int = 0,
    var sizeogg:Int = 0,
    var songid:Int = 0,
    var songmid: String? = null,
    var songname: String? = null,
    var songname_hilight: String? = null,
    var strMediaMid: String? = null,
    var stream:Int = 0,
    var switchX:Int = 0,
    var t :Int= 0,
    var tag:Int = 0,
    var type:Int = 0,
    var ver:Int = 0,
    var singer: List<SingerBean>? = null
)