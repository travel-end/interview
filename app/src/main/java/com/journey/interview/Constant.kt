package com.journey.interview

import android.Manifest
import java.util.*

/**
 * @By Journey 2020/9/15
 * @Description
 */
object Constant {
    const val BASE_URL = "https://api.caiyunapp.com/"
    const val CAI_YUN_TOKEN = "auv03EiB3Zt9RZcn"
    val mPermission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    const val LOCATION_SUCCESS_CODE = 0

    const val LNG_KEY = "lng"
    const val LAT_KEY = "lat"
    const val PLACE_NAME = "name"
    const val PLACE_PRIMARY_KEY = "key"

    fun getTodayInWeek(data: Date): String {
        val list = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }
    //根据歌手获取歌手图片的baseUrl
    const val SINGER_PIC_BASE_URL = "http://music.163.com/"
    const val SINGER_PIC = "api/search/get/web?csrf_token=&type=100"
    // Fiddler抓包qq音乐网站后的地址
    const val FIDDLER_BASE_QQ_URL = "https://c.y.qq.com/"
    //获取播放地址的baseUrl
    const val FIDDLER_BASE_SONG_URL = "https://u.y.qq.com/"
    // 获取歌手图片需要添加user-agent的表头
    const val HEADER_USER_AGENT = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36"

    const val ALBUM_PIC="http://y.gtimg.cn/music/photo_new/T002R180x180M000"
    const val JPG=".jpg"
    const val SONG_URL_DATA_LEFT="%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%22358840384%22%2C%22songmid%22%3A%5B%22"
    const val SONG_URL_DATA_RIGHT="%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221443481947%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%221443481947%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D"

    val STORAGE_IMG_FILE =
        InterviewApp.instance.getExternalFilesDir("").toString() + "/imusic/img/"

    // 播放顺序
    const val PLAY_ORDER = 0 // 顺序播放
    const val PLAY_SINGER = 1//单曲循环
    const val PLAY_RANDOM = 2// 随机播放
    // 播放状态
    const val SONG_PLAY = 0
    const val SONG_PAUSE = 1
    const val SONG_RESUME = 2
    const val SONG_CHANGE = 3
    // 播放列表
    const val LIST_TYPE_LOCAL=1//本地列表
    const val LIST_TYPE_ONLINE=2//专辑列表
    const val LIST_TYPE_COLLECTED=3//我的收藏列表
    const val LIST_TYPE_HISTORY=4//最近播放列表
    const val LIST_TYPE_DOWNLOAD=5//下载列表
}