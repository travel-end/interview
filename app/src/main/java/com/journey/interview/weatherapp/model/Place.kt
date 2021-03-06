package com.journey.interview.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/**
 * @By Journey 2020/9/15
 * @Description
 * SerializedName将json中的字段名匹配到自己定义的字段中
 */
@Entity
data class Place (
    @PrimaryKey(autoGenerate = true)
    var primaryKey:Int,
    var name:String,
    val location:Location,
    @SerializedName("formatted_address")val address:String = ""
) {
    constructor():this(0,"",Location("",""),"")
}