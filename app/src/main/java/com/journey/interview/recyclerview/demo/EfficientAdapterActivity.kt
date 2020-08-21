package com.journey.interview.recyclerview.demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.recyclerview.bean.NumberInfo
import com.journey.interview.recyclerview.core.*
import kotlinx.android.synthetic.main.activity_efficient_adapter.*

/**
 * @By Journey 2020/8/21
 * @Description RecyclerView Adapter的简单封装演示
 */
class EfficientAdapterActivity:AppCompatActivity() {
    private var index = 0
    private var mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_efficient_adapter)
        recycle_view.setup<NumberInfo> {
            dataSource(initData())
            adapter {
                addItem(R.layout.item_number_info) {
                    bindViewHolder{data, pos, _ ->
                        setText(R.id.number,data?.number.toString())
                        itemClicked(View.OnClickListener {
                            val item = getItem(pos)
                            Log.i("JG","${item?.number}")
                        })
                    }
                }

            }

        }
        // 添加数据
        add.setOnClickListener {
            val info = NumberInfo()
            info.number = 100
            recycle_view?.insertedData(3,info)
        }
        //删除数据演示
        remove.setOnClickListener {
            recycle_view?.removedData(3)
        }
        //更新数据演示
        update.setOnClickListener {
            index = 0
            mHandler.post(mRunnable)
        }
        //停止更新数据
        stop.setOnClickListener { mHandler.removeCallbacksAndMessages(null) }
        //重新刷新数据演示
        reset.setOnClickListener { recycle_view?.submitList(initData()) }
        //界面转跳
        viewtype.setOnClickListener {
            startActivity(Intent(this@EfficientAdapterActivity, ListActivity::class.java))
        }

    }
    private var mRunnable: Runnable = object : Runnable {
        override fun run() {
            index++
            val info = NumberInfo()
            info.number = index
            val info2 = NumberInfo()
            info2.number = index * 2
            recycle_view?.updateData(1, info, false) //普通更新，界面会闪烁
            recycle_view?.updateData(3, info2)                //高效率刷新,界面不会闪烁
            mHandler.postDelayed(this, 1000)
        }
    }


    private fun initData():MutableList<NumberInfo> {
        val list :MutableList<NumberInfo> = mutableListOf()
        for (i in 0..9) {
            val info = NumberInfo()
            info.number = i
            list.add(info)
        }
        return list
    }
}