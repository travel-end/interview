package com.journey.interview.weatherapp.state

import androidx.annotation.StringRes
import com.journey.interview.R
import com.journey.interview.utils.getString

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/6/3 23:01
 */
data class State(var code: StateType, var message: String = "", @StringRes var tip: Int = 0)

data class EmptyState(var resource:Int= R.drawable.ic_xigua, var message:String = R.string.empty_common.getString())