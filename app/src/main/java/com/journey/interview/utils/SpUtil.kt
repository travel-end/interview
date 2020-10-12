package com.journey.interview.utils

import android.content.Context
import android.content.SharedPreferences
import com.journey.interview.InterviewApp

/**
 * @Description
 * android数据的存储和查询
 */
object SpUtil {


    private const val name = "sp_app_config"

    private val prefs: SharedPreferences by lazy {
        InterviewApp.instance.applicationContext
            .getSharedPreferences(name, Context.MODE_PRIVATE)
    }


    fun saveValue(key: String, value: Any) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw IllegalArgumentException("SharedPreferences Wrong Type")
        }.apply()
    }


    @Suppress("UNCHECKED_CAST")
    fun getValue(key: String, default: Any): Any? = with(prefs) {
        return when (default) {
            is Int -> getInt(key, default)
            is String -> this.getString(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            is Boolean -> getBoolean(key, default)
            else -> throw IllegalArgumentException("SharedPreferences 类型错误")
        }
    }

    fun getString(key: String, default: String = ""): String {
        return getValue(key, default) as String
    }


    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getValue(key, default) as Boolean
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return getValue(key, default) as Float
    }

    fun getInt(key: String, default: Int = 0): Int {
        return getValue(key, default) as Int
    }

    fun getLong(key: String, default: Long = 0): Long {
        return getValue(key, default) as Long
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}