package com.journey.interview.utils

import com.journey.interview.InterviewApp
import com.journey.interview.imusic.model.Song
import java.io.*

/**
 * @By Journey 2020/9/27
 * @Description
 */
object FileUtil {

    fun saveSong(song: Song?) {
        try {
            val file =
                File(InterviewApp.instance.getExternalFilesDir("i_music")?.absolutePath?:"")
            if (!file.exists()) {
                file.mkdirs()
            }
            //写对象流的对象
            val userFile = File(file, "song.txt")
            val oos =
                ObjectOutputStream(FileOutputStream(userFile))
            oos.writeObject(song) //将Person对象p写入到oos中
            oos.close() //关闭文件流
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getSong(): Song? {
        try {
            val ois = ObjectInputStream(
                FileInputStream(
                    InterviewApp.instance.getExternalFilesDir("").toString() + "/i_music/song.txt"
                )
            )
            return ois.readObject() as Song //返回对象
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}