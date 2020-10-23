package com.journey.interview.imusic.frg

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.journey.interview.R
import com.journey.interview.customizeview.indicator.IndicatorView
import com.journey.interview.customizeview.indicator.enums.IndicatorSlideMode
import com.journey.interview.customizeview.indicator.enums.IndicatorStyle
import com.journey.interview.imusic.model.*
import com.journey.interview.imusic.vm.ICloudVillageViewModel
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.f_dp
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_frg_find_poetry.*
import java.util.ArrayList

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IFindPoetryFragment:BaseLifeCycleFragment<ICloudVillageViewModel>() {
    private lateinit var rvPoetry:RecyclerView
    private val findData: MutableList<Any> = ArrayList()
    private var scrollPosition:Int =0
    companion object {
        fun newInstance() :Fragment {
            return IFindPoetryFragment()
        }
    }

    override fun layoutResId()= R.layout.imusic_frg_find_poetry

    override fun initView() {
        super.initView()
        initDataSource()
        rvPoetry = poetry_rv
        rvPoetry.setup<Any> {
            dataSource(findData)
            withLayoutManager {
                val layoutManager = GridLayoutManager(requireContext(),3)
                layoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when(adapter?.getItem(position)) {
                            is Poetry->1
                            else->3
                        }
                    }
                }
                return@withLayoutManager layoutManager
            }
            adapter {
                addItem(R.layout.imusic_item_poetry_f_a) {
                    isForViewType { data, position -> data is FindTop }
                    bindViewHolder { data, position, holder ->
                        data?.let {
                            val item = it as FindTop
                            val datas = item.banners
                            itemView?.let {view->
                                val rvf1:RecyclerView = view.findViewById(R.id.poetry_fa_rv)
                                val indicatorView :IndicatorView = view.findViewById(R.id.poetry_fa_indicatorview)
                                indicatorView
                                    .setSliderColor(
                                        ContextCompat.getColor(requireContext(),R.color.grey_10),
                                        ContextCompat.getColor(requireContext(),R.color.material_blue))
                                    .setSliderWidth(6.f_dp)
                                    .setSliderHeight(6.f_dp)
                                    .setSlideMode(IndicatorSlideMode.NORMAL)
                                    .setIndicatorStyle(IndicatorStyle.CIRCLE)
                                    .setupWithPosition(datas.size)
                                val f1LayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                                rvf1.layoutManager = f1LayoutManager
                                rvf1.onFlingListener=null
                                val pagerSnapHelper = PagerSnapHelper()
                                pagerSnapHelper.attachToRecyclerView(rvf1)
                                rvf1.adapter = object :RecyclerView.Adapter<PoetryF1ViewHolder>() {
                                    override fun onCreateViewHolder(
                                        parent: ViewGroup,
                                        viewType: Int
                                    ): PoetryF1ViewHolder {
                                        return PoetryF1ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.imusic_item_poetry_f_a_item,parent,false))
                                    }
                                    override fun getItemCount()=Integer.MAX_VALUE
                                    override fun onBindViewHolder(
                                        holder: PoetryF1ViewHolder,
                                        position: Int
                                    ) {
                                        val pos = position % datas.size
                                        val res = datas[pos]
                                        holder.cover.setImageResource(res.cover)
                                        holder.desc.text = res.desc
                                    }
                                }
//                                rvf1.scrollToPosition(2)
//                                indicatorView.initSliderPosition(0,0f)
                                var index = 0
                                rvf1.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                                    override fun onScrolled(
                                        recyclerView: RecyclerView,
                                        dx: Int,
                                        dy: Int
                                    ) {
                                        super.onScrolled(recyclerView, dx, dy)
//                                        val visiblePos = f1LayoutManager.findFirstVisibleItemPosition()
//                                        indicatorView.setOnSelectedListener(scrollPosition)
//                                        index++
//                                        scrollPosition = index%5
                                        Log.e("JG","index=$index")
                                        Log.e("JG","scrollPosition=$scrollPosition")
//                                        if (initPosition != visiblePos) {
//                                            initPosition= f1LayoutManager.findFirstVisibleItemPosition()
//                                            Log.e("JG","initPosition=$initPosition")
//                                            Log.e("JG","dx=$dx")
//                                            indicatorView.setOnSelectedListener(initPosition)
//                                        }
                                    }

                                    override fun onScrollStateChanged(
                                        recyclerView: RecyclerView,
                                        newState: Int
                                    ) {
                                        super.onScrollStateChanged(recyclerView, newState)

                                    }
                                })
                            }
                        }

                    }
                }
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is FindTitle }
                    bindViewHolder { data, pos, _ ->
                        val title = data as FindTitle
                        setText(R.id.find_tv_title_main, title.titleMain)
                        setText(R.id.find_mb_second, title.titleSecond)
                        clicked(R.id.find_mb_second,View.OnClickListener {
                            Log.e("JG","--->查看更多")
                        })
                    }
                }

            }

        }
    }

    private fun initDataSource() {
        // 1楼
        val topItem = mutableListOf<FindTopItem>()
        topItem.add(FindTopItem(R.drawable.cover5, "1ST"))
        topItem.add(FindTopItem(R.drawable.cover9, "2ND"))
        topItem.add(FindTopItem(R.drawable.cover5, "3RD"))
        topItem.add(FindTopItem(R.drawable.cover9, "4TH"))
        topItem.add(FindTopItem(R.drawable.cover5, "5FI"))
        findData.add(FindTop(banners = topItem))
        //2楼
        findData.add(FindTitle("二楼", "查看更多"))
        // 3楼
//        findData.add(Poetry(R.drawable.icon2,"唐诗","zuoye"))
//        // 4楼
//        findData.add(Poetry(R.drawable.icon2,"宋词","方法"))
//        // 5楼
//        findData.add(Poetry(R.drawable.icon4,"其他","刚刚"))
    }

    inner class PoetryF1ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val cover:ImageView = itemView.findViewById(R.id.poetry_item_f_a_iv)
        val desc: TextView = itemView.findViewById(R.id.poetry_item_f_a_tv)
    }
}