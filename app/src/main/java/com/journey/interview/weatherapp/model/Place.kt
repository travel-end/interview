package com.journey.interview.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/**
 * @By Journey 2020/9/15
 * @Description
 */
@Entity
data class Place (
    @PrimaryKey(autoGenerate = true)
    val primaryKey:Int,
    val name:String,
    val location:Location,
    @SerializedName("formatted_address")val address:String = ""
) {
    constructor():this(0,"",Location("",""),"")
}