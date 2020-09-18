package com.journey.interview.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.utils.ofMap
import com.journey.interview.utils.print

/**
 * @By Journey 2020/9/18
 * @Description
 */
class FastKotlinActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast_kotlin)
        // 对于打印列表，使用for each打印， 内容散落，容易被其他log打断
        // 使用kotlin可以打印一份便捷好看的列表

        // 打印list的扩展函数
        val persons = listOf(
            IPerson(name = "玲玲",age = 10),
            IPerson(name = "风穗",age = 20),
            IPerson(name = "犬泽",age = 17),
            IPerson(name = "邪皇",age = 21)
        )
        persons.print {
            "name=${it.name}, age=${it.age}"
        }.let { Log.v("Kotlin",it) }


        // 同样 打印map的扩展函数
        val morePersons = mapOf(
            "东京" to IPerson(name = "玲玲",age = 10),
            "大阪" to IPerson(name = "风穗",age = 20),
            "奈良" to IPerson(name = "犬泽",age = 17),
            "熊本" to IPerson(name = "邪皇",age = 21)
        )
        morePersons.print {
            "name=${it?.name},age=${it?.age}"
        }.let { Log.v("Kotlin",it) }


        //打印嵌套data（适用于类的字段较多，这种打印看起来十分清晰）
        Person("林丸",18,Location("Japan","Tokyo",Coordinate(10,20))).ofMap()?.print().let {
            Log.v("Kotlin","$it")
        }
        // 打印单一类型data
        IPerson(name = "可氤",age = 27,gender = 0).ofMap()?.print().let { Log.v("Kotlin","$it") }

    }

}