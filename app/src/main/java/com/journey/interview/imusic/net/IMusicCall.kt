package com.journey.interview.imusic.net

import com.journey.interview.weatherapp.state.ApiException

/**
 * @By Journey 2020/9/29
 * @Description
 */
data class IMusicCall<T>(
    private var code:Int=-1,
    private var msg:String?,
    private var data:T
) {
    fun call():T {
        if (code==0) {
            return data
        }else {
            throw ApiException(code,msg!!)
        }
    }
}