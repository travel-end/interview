package com.journey.interview.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.journey.interview.Constant
import com.journey.interview.InterviewApp
import com.journey.interview.R
import com.journey.interview.imusic.model.Song
import java.io.*
import kotlin.concurrent.thread

/**
 * @By Journey 2020/9/27
 * @Description
 */
object SongUtil {
    /**
     * 将一个对象写入流中 对象必须实现Serializable接口
     */
    fun saveSong(song: Song?) {
        try {
            if (song != null) {
                val file =
                    File(InterviewApp.instance.getExternalFilesDir("imusic")?.absolutePath?:"")
                if (!file.exists()) {
                    file.mkdirs()
                }
                //写对象流的对象
                val songFile = File(file, "song.txt")
                val oos =
                    ObjectOutputStream(FileOutputStream(songFile))
                oos.writeObject(song) //将Person对象p写入到oos中
                oos.close() //关闭文件流
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("JG","写入对象error!")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("JG","写入对象error!")
        }
    }

    fun getSong(): Song? {
        try {
            val ois = ObjectInputStream(
                FileInputStream(
                    InterviewApp.instance.getExternalFilesDir("imusic").toString() + "/song.txt"
                )
            )
            return ois.readObject() as Song //返回对象
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("JG","读取对象error!")
            return null
        } catch (e: IOException) {
            Log.e("JG","读取对象error!")
            e.printStackTrace()
            return null
        } catch (e: ClassNotFoundException) {
            Log.e("JG","读取对象error!")
            e.printStackTrace()
            return null
        }
    }

    fun saveImgToNative(bitmap: Bitmap?,singer:String):Boolean {
        if (bitmap != null) {
            val file = File(Constant.STORAGE_IMG_FILE)
            if (!file.exists()) {
                file.mkdirs()
            }
            val singerImgFile = File(file,"$singer.jpg")
            var fos :FileOutputStream?=null
            try {
                fos = FileOutputStream(singerImgFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
                fos.flush()
                return true
            } catch (e:FileNotFoundException) {
                e.printStackTrace()
//                Log.e("JG","保存至本地:fileNotFound")
                return false
            } catch (e:IOException) {
                e.printStackTrace()
                return false
//                Log.e("JG","saveImgToNative:fileNotFound")
            } finally {
                try {
                    fos?.close()
                } catch (e:IOException) {
                    e.printStackTrace()
                }
            }
        }
        return false
    }

    fun setLocalCoverImg(context: Context,singer: String,v:ImageView) {
        val mS:String
        mS = if (singer.contains("/")) {
            val s = singer.split("/")
            s[0].trim()
        } else {
            singer.trim()
        }
        val imgUrl = "${Constant.STORAGE_IMG_FILE}$mS.jpg"
        Glide.with(context)
            .load(imgUrl)
            .apply(RequestOptions.placeholderOf(R.drawable.icon1))
            .apply(RequestOptions.errorOf(R.drawable.icon1))
            .into(v)
    }

    /**
     * 保存歌词到本地
     */
    fun saveLrcToNative(lrc:String,songName:String) {
        thread {
            val file = File(Constant.STORAGE_LRC_FILE)
            if (!file.exists()) {
                file.mkdirs()
            }
            val name = if (songName.isBlank()) {
                "imusic"
            } else {
                songName
            }
            val lrcFile = File(file,"$name.lrc")
            try {
                val fileWriter = FileWriter(lrcFile)
                fileWriter.write(lrc)
                fileWriter.close()
            } catch (e:IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getLrcFromNative(songName:String) :String? {
        return try {
            val fileReader = FileReader("${Constant.STORAGE_LRC_FILE}$songName.lrc")
            val bufferReader = BufferedReader(fileReader)
            val lrc = StringBuilder()
            while(true) {
                val s = bufferReader.readLine() ?: break
                lrc.append(s).append("\n")
            }
            fileReader.close()
            Log.e("JG","读取本地歌词：$lrc")
            lrc.toString()
        } catch (e:IOException) {
            e.printStackTrace()
            null
        }
    }
}