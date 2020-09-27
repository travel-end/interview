package com.journey.interview.imusic.model

/**
 * <pre>
 * author : 残渊
 * time   : 2019/09/02
 * desc   : 根据歌曲mid得到播放地址
</pre> *
 */
data class SongUrl(
    /**
     * req_0 : {"data":{"expiration":80400,"login_key":"","midurlinfo":[{"common_downfromtag":0,"errtype":"","filename":"C400000RJgjd1kEeBn.m4a","flowfromtag":"","flowurl":"","hisbuy":0,"hisdown":0,"isbuy":0,"isonly":0,"onecan":0,"opi128kurl":"","opi192koggurl":"","opi192kurl":"","opi30surl":"","opi48kurl":"","opi96kurl":"","opiflackurl":"","p2pfromtag":0,"pdl":0,"pneed":0,"pneedbuy":0,"premain":0,"purl":"C400000RJgjd1kEeBn.m4a?guid=358840384&vkey=639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3&uin=0&fromtag=66","qmdlfromtag":0,"result":0,"songmid":"003wFozn3V3Ra0","tips":"","uiAlert":0,"vip_downfromtag":0,"vkey":"639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3","wififromtag":"","wifiurl":""}],"msg":"183.63.119.7","retcode":0,"servercheck":"0502f67d8cf451662c4b46417d571295","sip":["http://ws.stream.qqmusic.qq.com/","http://isure.stream.qqmusic.qq.com/"],"testfile2g":"C400003mAan70zUy5O.m4a?guid=358840384&vkey=6D7A29027104BC9653C74A397CE6C6D7DB6D9E67D48C90FF74B4BBA6987684DBC2332C5FB6FC278FE170F44FF62139EF0A699D9DF09E7F8F&uin=&fromtag=3","testfilewifi":"C400003mAan70zUy5O.m4a?guid=358840384&vkey=6D7A29027104BC9653C74A397CE6C6D7DB6D9E67D48C90FF74B4BBA6987684DBC2332C5FB6FC278FE170F44FF62139EF0A699D9DF09E7F8F&uin=&fromtag=3","thirdip":["",""],"uin":"","verify_type":0},"code":0}
     * code : 0
     * ts : 1567401529098
     */
    var code: Int = 0,
    var req_0: Req0Bean? = null
)

data class Req0Bean(
    /**
     * data : {"expiration":80400,"login_key":"","midurlinfo":[{"common_downfromtag":0,"errtype":"","filename":"C400000RJgjd1kEeBn.m4a","flowfromtag":"","flowurl":"","hisbuy":0,"hisdown":0,"isbuy":0,"isonly":0,"onecan":0,"opi128kurl":"","opi192koggurl":"","opi192kurl":"","opi30surl":"","opi48kurl":"","opi96kurl":"","opiflackurl":"","p2pfromtag":0,"pdl":0,"pneed":0,"pneedbuy":0,"premain":0,"purl":"C400000RJgjd1kEeBn.m4a?guid=358840384&vkey=639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3&uin=0&fromtag=66","qmdlfromtag":0,"result":0,"songmid":"003wFozn3V3Ra0","tips":"","uiAlert":0,"vip_downfromtag":0,"vkey":"639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3","wififromtag":"","wifiurl":""}],"msg":"183.63.119.7","retcode":0,"servercheck":"0502f67d8cf451662c4b46417d571295","sip":["http://ws.stream.qqmusic.qq.com/","http://isure.stream.qqmusic.qq.com/"],"testfile2g":"C400003mAan70zUy5O.m4a?guid=358840384&vkey=6D7A29027104BC9653C74A397CE6C6D7DB6D9E67D48C90FF74B4BBA6987684DBC2332C5FB6FC278FE170F44FF62139EF0A699D9DF09E7F8F&uin=&fromtag=3","testfilewifi":"C400003mAan70zUy5O.m4a?guid=358840384&vkey=6D7A29027104BC9653C74A397CE6C6D7DB6D9E67D48C90FF74B4BBA6987684DBC2332C5FB6FC278FE170F44FF62139EF0A699D9DF09E7F8F&uin=&fromtag=3","thirdip":["",""],"uin":"","verify_type":0}
     * code : 0
     */
    var data: UrlDataBean? = null,
    var code: Int = 0


)

data class UrlDataBean(
    /**
     * expiration : 80400
     * login_key :
     * midurlinfo : [{"common_downfromtag":0,"errtype":"","filename":"C400000RJgjd1kEeBn.m4a","flowfromtag":"","flowurl":"","hisbuy":0,"hisdown":0,"isbuy":0,"isonly":0,"onecan":0,"opi128kurl":"","opi192koggurl":"","opi192kurl":"","opi30surl":"","opi48kurl":"","opi96kurl":"","opiflackurl":"","p2pfromtag":0,"pdl":0,"pneed":0,"pneedbuy":0,"premain":0,"purl":"C400000RJgjd1kEeBn.m4a?guid=358840384&vkey=639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3&uin=0&fromtag=66","qmdlfromtag":0,"result":0,"songmid":"003wFozn3V3Ra0","tips":"","uiAlert":0,"vip_downfromtag":0,"vkey":"639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3","wififromtag":"","wifiurl":""}]
     * msg : 183.63.119.7
     * retcode : 0
     * servercheck : 0502f67d8cf451662c4b46417d571295
     * sip : ["http://ws.stream.qqmusic.qq.com/","http://isure.stream.qqmusic.qq.com/"]
     * testfile2g : C400003mAan70zUy5O.m4a?guid=358840384&vkey=6D7A29027104BC9653C74A397CE6C6D7DB6D9E67D48C90FF74B4BBA6987684DBC2332C5FB6FC278FE170F44FF62139EF0A699D9DF09E7F8F&uin=&fromtag=3
     * testfilewifi : C400003mAan70zUy5O.m4a?guid=358840384&vkey=6D7A29027104BC9653C74A397CE6C6D7DB6D9E67D48C90FF74B4BBA6987684DBC2332C5FB6FC278FE170F44FF62139EF0A699D9DF09E7F8F&uin=&fromtag=3
     * thirdip : ["",""]
     * uin :
     * verify_type : 0
     */
    var expiration: Int = 0,
    var login_key: String? = null,
    var msg: String? = null,
    var retcode: Int = 0,
    var servercheck: String? = null,
    var testfile2g: String? = null,
    var testfilewifi: String? = null,
    var uin: String? = null,
    var verify_type: Int = 0,
    var midurlinfo: List<MidurlinfoBean>? = null,
    var sip: List<String>? = null,
    var thirdip: List<String>? = null


)


data class MidurlinfoBean(
    /**
     * common_downfromtag : 0
     * errtype :
     * filename : C400000RJgjd1kEeBn.m4a
     * flowfromtag :
     * flowurl :
     * hisbuy : 0
     * hisdown : 0
     * isbuy : 0
     * isonly : 0
     * onecan : 0
     * opi128kurl :
     * opi192koggurl :
     * opi192kurl :
     * opi30surl :
     * opi48kurl :
     * opi96kurl :
     * opiflackurl :
     * p2pfromtag : 0
     * pdl : 0
     * pneed : 0
     * pneedbuy : 0
     * premain : 0
     * purl : C400000RJgjd1kEeBn.m4a?guid=358840384&vkey=639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3&uin=0&fromtag=66
     * qmdlfromtag : 0
     * result : 0
     * songmid : 003wFozn3V3Ra0
     * tips :
     * uiAlert : 0
     * vip_downfromtag : 0
     * vkey : 639B78E998817CF9E252EEBC7A929B06B6FB939E7A1BF08593917940385E28D953A6A9F8191EAB3450A353E83F7A3420C8EE47664C1612A3
     * wififromtag :
     * wifiurl :
     */
    var common_downfromtag: Int = 0,
    var errtype: String? = null,
    var filename: String? = null,
    var flowfromtag: String? = null,
    var flowurl: String? = null,
    var hisbuy: Int = 0,
    var hisdown: Int = 0,
    var isbuy: Int = 0,
    var isonly: Int = 0,
    var onecan: Int = 0,
    var opi128kurl: String? = null,
    var opi192koggurl: String? = null,
    var opi192kurl: String? = null,
    var opi30surl: String? = null,
    var opi48kurl: String? = null,
    var opi96kurl: String? = null,
    var opiflackurl: String? = null,
    var p2pfromtag: Int = 0,
    var pdl: Int = 0,
    var pneed: Int = 0,
    var pneedbuy: Int = 0,
    var premain: Int = 0,
    var purl: String? = null,
    var qmdlfromtag: Int = 0,
    var result: Int = 0,
    var songmid: String? = null,
    var tips: String? = null,
    var uiAlert: Int = 0,
    var vip_downfromtag: Int = 0,
    var vkey: String? = null,
    var wififromtag: String? = null,
    var wifiurl: String? = null
)