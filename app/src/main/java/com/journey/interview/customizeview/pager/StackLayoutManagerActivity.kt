package com.journey.interview.customizeview.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.journey.interview.R
import com.journey.interview.imusic.model.FindTopItem
import com.journey.interview.utils.toFloatPx

/**
 * @By Journey 2020/10/13
 * @Description
 */
class StackLayoutManagerActivity : AppCompatActivity() {
    private val topItem = mutableListOf<FindTopItem>()

    private lateinit var focusLayoutManager: FocusLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imusic_find_item_top)
        initData()
        val layoutManager = StackLayoutManager(this)
        val rv = findViewById<RecyclerView>(R.id.stack_find_rv_top)
//        rv.layoutManager = layoutManager

        focusLayoutManager = FocusLayoutManager.Builder()
            .layerPadding(14f.toFloatPx())
            .normalViewGap(14f.toFloatPx())
            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
            .isAutoSelect(true)
            .maxLayerCount(3)
            .setOnFocusChangeListener { focusdPosition, lastFocusedPosition ->

            }
            .build()
        rv.layoutManager = focusLayoutManager



        val adapter = object :RecyclerView.Adapter<BaseViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.imusic_item_top_banner,parent,false)
                return BaseViewHolder(view)
            }
            override fun getItemCount()=topItem.size
            override fun getItemViewType(position: Int)=1
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                val item = topItem[position]
                val iv = holder.itemView.findViewById<ImageView>(R.id.iv_find_banner_top)
                iv?.setImageResource(item.cover)
            }

        }
        rv.adapter = adapter




//        rv.setup<FindTopItem> {
//            withLayoutManager {
//                return@withLayoutManager layoutManager
//            }
//            dataSource(topItem)
//            adapter {
//                addItem(R.layout.imusic_item_top_banner) {
//                    bindViewHolder { data: FindTopItem?, position: Int, holder: ViewHolderCreator<FindTopItem> ->
//                        setImageResource(R.id.iv_find_banner_top, data?.cover ?: 0)
//                        itemClicked(View.OnClickListener {
//                            layoutManager.smoothScrollToPosition(position, null)
//                        })
//                    }
//                }
//            }
//        }
    }

    private fun initData() {
        topItem.add(FindTopItem(R.drawable.pic11, ""))
        topItem.add(FindTopItem(R.drawable.pic11, ""))
        topItem.add(FindTopItem(R.drawable.pic11, ""))
        topItem.add(FindTopItem(R.drawable.pic11, ""))
        topItem.add(FindTopItem(R.drawable.pic11, ""))
        topItem.add(FindTopItem(R.drawable.pic11, ""))
    }

    class BaseViewHolder(view:View):RecyclerView.ViewHolder(view) {

    }
}