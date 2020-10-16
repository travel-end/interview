package com.journey.interview.imusic.util

import android.util.Log
import com.journey.interview.InterviewApp

/**
 * @By Journey 2020/10/16
 * @Description
 */
object AssetsUtil {
    fun readAssetsFile(fileName:String):String? {
        val content:String?
        return try {
            val input = InterviewApp.instance.applicationContext.assets.open(fileName)
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()
            content = String(buffer)
            Log.e("JG","读取asset文件内容：$content")
            content
        } catch (e:Exception) {
            e.printStackTrace()
            Log.e("JG","读取assets文件出错!!!--> error:${e.message}")
            null
        }
    }
}