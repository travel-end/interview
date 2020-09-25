package com.journey.interview.imusic.frg

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.journey.interview.R
import com.journey.interview.imusic.model.*
import com.journey.interview.recyclerview.bean.Image
import com.journey.interview.recyclerview.core.*
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_hot_search.*

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IHotSearchFragment:BaseLifeCycleFragment<IHotSearchViewModel>() {
    private val dataSource = mutableListOf<Any>()
    companion object {
        fun newInstance():Fragment {
            return IHotSearchFragment()
        }
    }
    override fun layoutResId()= R.layout.imusic_hot_search
    override fun initView() {
        super.initView()
        initDataSource()
        rv_hot_search.setup<Any> {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            withLayoutManager {
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItem(position) is HistoryTitles
                            ||adapter?.getItem(position) is FindTitle) {
                            2
                        } else {
                            1
                        }
                    }
                }
                return@withLayoutManager gridLayoutManager
            }
            adapter {
                addItem(R.layout.imusic_item_hot_search_hostory) {
                    isForViewType { data, _ -> data is HistoryTitles }
                    bindViewHolder { data, pos, _ ->
                        val titles = data as HistoryTitles
                        val title = titles.name[0]
                        setText(R.id.hot_tv_history_content,title)
                    }
                }
                addItem(R.layout.imusic_hot_search_title) {
                    isForViewType { data, _ -> data is FindTitle }
                    bindViewHolder { data, pos, _ ->
                    }
                }
                addItem(R.layout.imusic_item_hot_search_top) {
                    isForViewType { data, _ -> data is  HotContent}
                    bindViewHolder { data, position, holder ->
                        val content = data as HotContent
                        setText(R.id.hot_top_number,"${position-1}")
                        setText(R.id.hot_tv_top_search,content.name)
                        if (content.isHot == "1") {
                            setVisible(R.id.hot_top_status,true)
                        }
                    }
                }
                addItem(R.layout.imusic_item_hot_search_bottom) {
                    isForViewType { data, _ -> data is  FindMenu}
                    bindViewHolder { data, position, holder ->
                        val content= data as FindMenu
                        setText(R.id.hot_bottom_name,content.menuName)
                        setImageResource(R.id.hot_bottom_cover,content.iconRes)
                    }
                }
            }


        }
        rv_hot_search.submitList(dataSource)
    }

    private fun initDataSource() {
        val historyName = mutableListOf<String>()
        historyName.add("李建")
        historyName.add("平凡之路")
        dataSource.add(HistoryTitles(historyName))

        dataSource.add(FindTitle("热搜榜","播放全部"))

        dataSource.add(HotContent("他只是经过","1","",""))
        dataSource.add(HotContent("会不会","1","",""))
        dataSource.add(HotContent("吴宣仪","","",""))
        dataSource.add(HotContent("薛之谦","","",""))
        dataSource.add(HotContent("永不失联的爱","","",""))
        dataSource.add(HotContent("爸爸妈妈","","",""))
        dataSource.add(HotContent("哈利波特","","",""))
        dataSource.add(HotContent("后继者","","",""))
        dataSource.add(HotContent("SNH48演唱会","","",""))
        dataSource.add(HotContent("夏天的风","","",""))

        dataSource.add(FindMenu(R.drawable.cover9,"歌手分类"))
        dataSource.add(FindMenu(R.drawable.cover9,"听歌有奖"))
        dataSource.add(FindMenu(R.drawable.cover9,"电音FM"))
        dataSource.add(FindMenu(R.drawable.cover9,"古典专区"))
        dataSource.add(FindMenu(R.drawable.cover9,"ACG专区"))
        dataSource.add(FindMenu(R.drawable.cover9,"亲子专区"))
        dataSource.add(FindMenu(R.drawable.cover9,"爵士FM"))
        dataSource.add(FindMenu(R.drawable.cover9,"声之剧场"))

    }
}