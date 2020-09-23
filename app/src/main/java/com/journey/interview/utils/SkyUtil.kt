package com.journey.interview.utils

import com.journey.interview.R

class Sky(val info: String, val icon: Int)

private val sky = mapOf(
    "CLEAR_DAY" to Sky(
        "晴",
        R.drawable.vector_drawable_ic_clear_day
    ),
    "CLEAR_NIGHT" to Sky(
        "晴",
        R.drawable.vector_drawable_ic_clear_night
    ),
    "PARTLY_CLOUDY_DAY" to Sky(
        "多云",
        R.drawable.vector_drawable_ic_partly_cloud_day
    ),
    "PARTLY_CLOUDY_NIGHT" to Sky(
        "多云",
        R.drawable.vector_drawable_ic_partly_cloud_night
    ),
    "CLOUDY" to Sky(
        "阴",
        R.drawable.vector_drawable_ic_cloudy
    ),
    "WIND" to Sky(
        "大风",
        R.drawable.vector_drawable_ic_windy
    ),
    "LIGHT_RAIN" to Sky(
        "小雨",
        R.drawable.vector_drawable_ic_light_rain
    ),
    "MODERATE_RAIN" to Sky(
        "中雨",
        R.drawable.vector_drawable_ic_moderate_rain
    ),
    "HEAVY_RAIN" to Sky(
        "大雨",
        R.drawable.vector_drawable_ic_heavy_rain
    ),
    "STORM_RAIN" to Sky(
        "暴雨",
        R.drawable.vector_drawable_ic_storm_rain
    ),
    "THUNDER_SHOWER" to Sky(
        "雷阵雨",
        R.drawable.vector_drawable_ic_thunder_shower
    ),
    "SLEET" to Sky(
        "雨夹雪",
        R.drawable.vector_drawable_ic_sleet
    ),
    "LIGHT_SNOW" to Sky(
        "小雪",
        R.drawable.vector_drawable_ic_light_snow
    ),
    "MODERATE_SNOW" to Sky(
        "中雪",
        R.drawable.vector_drawable_ic_moderate_snow
    ),
    "HEAVY_SNOW" to Sky(
        "大雪",
        R.drawable.vector_drawable_ic_heavy_snow
    ),
    "STORM_SNOW" to Sky(
        "暴雪",
        R.drawable.vector_drawable_ic_heavy_snow
    ),
    "HAIL" to Sky(
        "冰雹",
        R.drawable.vector_drawable_ic_hail
    ),
    "LIGHT_HAZE" to Sky(
        "轻度雾霾",
        R.drawable.vector_drawable_ic_light_haze
    ),
    "MODERATE_HAZE" to Sky(
        "中度雾霾",
        R.drawable.vector_drawable_ic_moderate_haze
    ),
    "HEAVY_HAZE" to Sky(
        "重度雾霾",
        R.drawable.vector_drawable_ic_heavy_haze
    ),
    "FOG" to Sky(
        "雾",
        R.drawable.vector_drawable_ic_fog
    ),
    "DUST" to Sky(
        "浮尘",
        R.drawable.vector_drawable_ic_fog
    )
)

fun getSky(skyIcon: String): Sky {
    return sky[skyIcon] ?: (sky["CLEAR_DAY"] ?: error(""))
}




class WindOri(val ori: String)
class WindSpeed(val speed: String)


fun getWindOri(windOri: Double): WindOri {
    if (windOri in 11.26..56.25) {
        return WindOri("东北风")
    }
    if (windOri in 56.26..78.75) {
        return WindOri("东风")
    }
    if (windOri in 78.76..146.25) {
        return WindOri("东南风")
    }
    if (windOri in 146.26..168.75) {
        return WindOri("南风")
    }
    if (windOri in 168.76..236.25) {
        return WindOri("西南风")
    }
    if (windOri in 236.26..258.75) {
        return WindOri("西风")
    }
    if (windOri in 281.26..348.75) {
        return WindOri("西北风")
    }
    return WindOri("北风")
}

fun getWindSpeed(windSpeed: Double): WindSpeed {
    if (windSpeed in 1.0..5.0) {
        return WindSpeed("1级")
    }
    if (windSpeed in 6.0..11.0) {
        return WindSpeed("2级")
    }
    if (windSpeed in 12.0..19.0) {
        return WindSpeed("3级")
    }
    if (windSpeed in 20.0..28.0) {
        return WindSpeed("4级")
    }
    if (windSpeed in 29.0..38.0) {
        return WindSpeed("5级")
    }
    if (windSpeed in 39.0..49.0) {
        return WindSpeed("6级")
    }
    if (windSpeed in 50.0..61.0) {
        return WindSpeed("7级")
    }
    if (windSpeed in 62.0..74.0) {
        return WindSpeed("8级")
    }
    if (windSpeed in 75.0..88.0) {
        return WindSpeed("9级")
    }
    if (windSpeed in 89.0..102.0) {
        return WindSpeed("10级")
    }
    if (windSpeed in 103.0..117.0) {
        return WindSpeed("11级")
    }
    if (windSpeed in 118.0..133.0) {
        return WindSpeed("12级")
    }

    if (windSpeed in 134.0..149.0) {
        return WindSpeed("13级")
    }
    if (windSpeed in 150.0..166.0) {
        return WindSpeed("14级")
    }
    if (windSpeed in 167.0..183.0) {
        return WindSpeed("15级")
    }
    if (windSpeed in 184.0..201.0) {
        return WindSpeed("16级")
    }
    if (windSpeed in 202.0..220.0) {
        return WindSpeed("17级")
    }
    return WindSpeed("0级")
}


class AirLevel(val airLevel: String)

fun getAirLevel(airLevel: Double): AirLevel {
    if (airLevel in 0.0..50.0) {
        return AirLevel("优")
    }
    if (airLevel in 50.0..100.0) {
        return AirLevel("良")
    }
    if (airLevel in 100.0..150.0) {
        return AirLevel("轻度污染")
    }
    if (airLevel in 150.0..200.0) {
        return AirLevel("中度污染")
    }
    return AirLevel("重度污染")
}