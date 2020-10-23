package com.journey.interview.imusic.frg

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.journey.interview.R
import com.journey.interview.imusic.model.peotry.*
import com.journey.interview.imusic.vm.ICloudVillageViewModel
import com.journey.interview.recyclerview.core.*
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_frg_find_poetry.*
import java.util.ArrayList

/**
 * @By Journey 2020/9/25
 * @Description imusic_frg_find_poetry
 * 1 五个tab 推荐
 */
class IFindPoetryFragment:BaseLifeCycleFragment<ICloudVillageViewModel>() {
    private val homeData: MutableList<Any> = ArrayList()
    private lateinit var poetryFindRv:RecyclerView
    companion object {
        fun newInstance(): Fragment {
            return IFindPoetryFragment()
        }
    }
    override fun layoutResId() = R.layout.imusic_frg_find_poetry
    override fun initView() {
        super.initView()
        poetryFindRv = poetry_rv
        initDataSource()
        poetryFindRv.setup<Any> {
            withLayoutManager {
                val lm = GridLayoutManager(requireContext(), 3)
                lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter?.getItem(position)) {
                            is PoetryF2Content -> 1
                            is PoetryF3Content -> 1
                            else -> 3
                        }
                    }
                }
                return@withLayoutManager lm
            }
            adapter {
                /* title ---> 全部诗人*/
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is PoetryF1Title }
                    bindViewHolder { data, pos, _ ->
                        val title = data as PoetryF1Title
                        setText(R.id.find_tv_title_main, title.title)
                        setText(R.id.find_mb_second, title.more)
                        clicked(R.id.find_mb_second,View.OnClickListener {
                            Log.e("JG","--->查看更多诗人")
                        })
                    }
                }
                /*content 全部诗人（）*/
                addItem(R.layout.imusic_find_rv_song_list) {
                    isForViewType { data, _ -> data is PoetryF1ContentList }
                    bindViewHolder { data, _, _ ->
                        val dataAll = data as PoetryF1ContentList
                        val dataRv = itemView?.findViewById<RecyclerView>(R.id.find_rv_song_list)
                        dataRv?.onFlingListener=null
                        val snapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(dataRv)
                        dataRv?.setup<PoetryF1Content> {
                            dataSource(dataAll.contentList)
                            adapter {
                                addItem(R.layout.imusic_find_item_ho_list) {
                                    bindViewHolder { data: PoetryF1Content?, position: Int, holder: ViewHolderCreator<PoetryF1Content> ->
                                        val item = data as PoetryF1Content
                                        setImageResource(R.id.find_iv_menu_icon,item.cover?:0)
                                        setText(R.id.find_tv_volume,item.contentDesc)
                                        setText(R.id.find_tv_desc,item.contentDesc)
                                        itemClicked(View.OnClickListener {
                                            Log.e("JG","${item.contentDesc}")
                                        })
                                    }
                                }
                            }
                        }
                    }
                }

                /* title 朝代*/
                addItem(R.layout.imusic_find_item_three_title) {
                    isForViewType { data, _ -> data is PoetryF2Title }
                    bindViewHolder { data, _, _ ->
                        val title = data as PoetryF2Title
                        setText(R.id.find_tv_title_three, title.title)
                    }
                }
                /* content 朝代*/
                addItem(R.layout.imusic_find_item_ho_list) {
                    isForViewType { data, _ -> data is PoetryF2Content }
                    bindViewHolder { data, _, _ ->
                        val item = data as PoetryF2Content
                        setImageResource(R.id.find_iv_menu_icon,item.cover?:0)
                        setText(R.id.find_tv_volume,item.contentDesc)
                        setText(R.id.find_tv_desc,item.contentDesc)
                    }
                }
                /* title 经典名句*/
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is PoetryF3title }
                    bindViewHolder { data, pos, _ ->
                        val title = data as PoetryF3title
                        setText(R.id.find_tv_title_main, title.title)
                    }
                }
                /* content 经典名句*/
                addItem(R.layout.imusic_find_item_ho_list) {
                    isForViewType { data, _ -> data is PoetryF3Content }
                    bindViewHolder { data, _, _ ->
                        val item = data as PoetryF3Content
                        setImageResource(R.id.find_iv_menu_icon,item.cover?:0)
                        setText(R.id.find_tv_volume,item.contentDesc)
                        setText(R.id.find_tv_desc,item.contentDesc)
                    }
                }

                /* title 名篇*/
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is PoetryF4title }
                    bindViewHolder { data, pos, _ ->
                        val title = data as PoetryF4title
                        setText(R.id.find_tv_title_main, title.title)
                        setText(R.id.find_mb_second, title.more)
                        clicked(R.id.find_mb_second,View.OnClickListener {
                            Log.e("JG","--->查看更多名篇")
                        })
                    }
                }
                /* content 名篇*/
                addItem(R.layout.imusic_find_rv_song_list) {
                    isForViewType { data, _ -> data is PoetryF4ContentList }
                    bindViewHolder { data, _, _ ->
                        val dataAll = data as PoetryF4ContentList
                        val dataRv = itemView?.findViewById<RecyclerView>(R.id.find_rv_song_list)
                        dataRv?.onFlingListener=null
                        val snapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(dataRv)
                        dataRv?.setup<PoetryF4Content> {
                            dataSource(dataAll.contentList)
                            adapter {
                                addItem(R.layout.imusic_find_item_ho_list) {
                                    bindViewHolder { data: PoetryF4Content?, position: Int, holder: ViewHolderCreator<PoetryF4Content> ->
                                        val item = data as PoetryF4Content
                                        setImageResource(R.id.find_iv_menu_icon,item.cover?:0)
                                        setText(R.id.find_tv_volume,item.contentDesc)
                                        setText(R.id.find_tv_desc,item.contentDesc)
                                        itemClicked(View.OnClickListener {
                                            Log.e("JG","${item.contentDesc}")
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
                /* title 每日推荐*/
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is PoetryF5title }
                    bindViewHolder { data, pos, _ ->
                        val title = data as PoetryF5title
                        setText(R.id.find_tv_title_main, title.title)
                    }
                }
                /* content 每日推荐*/
                addItem(R.layout.imusic_item_poetry_f_a) {
                    isForViewType { data, position -> data is PoetryF5ContentList }
                    bindViewHolder { data, position, holder ->
                        data?.let {
                            val item = it as PoetryF5ContentList
                            val datas = item.contentList
                            itemView?.let {view->
                                val rvf1:RecyclerView = view.findViewById(R.id.poetry_fa_rv)
                                val f1LayoutManager = LinearLayoutManager(requireContext(),
                                    LinearLayoutManager.HORIZONTAL,false)
                                rvf1.layoutManager = f1LayoutManager
                                rvf1.onFlingListener=null
                                val pagerSnapHelper = PagerSnapHelper()
                                pagerSnapHelper.attachToRecyclerView(rvf1)
                                rvf1.adapter = object :RecyclerView.Adapter<IFindPoetryFragment.PoetryF1ViewHolder>() {
                                    override fun onCreateViewHolder(
                                        parent: ViewGroup,
                                        viewType: Int
                                    ): IFindPoetryFragment.PoetryF1ViewHolder {
                                        return PoetryF1ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.imusic_item_poetry_f_a_item,parent,false))
                                    }
                                    override fun getItemCount()=Integer.MAX_VALUE
                                    override fun onBindViewHolder(
                                        holder: IFindPoetryFragment.PoetryF1ViewHolder,
                                        position: Int
                                    ) {
                                        val pos = position % datas.size
                                        val res = datas[pos]
                                        holder.cover.setImageResource(res.cover?:0)
                                        holder.desc.text = res.contentDesc
                                    }
                                }
                            }
                        }
                    }
                }

                /*title 趣闻轶事*/
                addItem(R.layout.imusic_find_item_three_title) {
                    isForViewType { data, _ -> data is PoetryF6title }
                    bindViewHolder { data, pos, _ ->
                        val title = data as PoetryF6title
                        setText(R.id.find_tv_title_three, title.title)
                        setText(R.id.find_change_second_three, title.more)
                        clicked(R.id.find_change_second_three,View.OnClickListener {
                        })
                    }
                }

                /*content 趣闻轶事*/
                addItem(R.layout.imusic_find_song_poetry) {
                    isForViewType { data, _ -> data is PoetryF6Content }
                    bindViewHolder { data, position, holder ->
                        val item =data as PoetryF6Content
                        val leftIv = itemView?.findViewById<ShapeableImageView>(R.id.find_sp_iv_icon)
                        leftIv?.shapeAppearanceModel = ShapeAppearanceModel.Builder()
                            .setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                        setImageResource(R.id.find_sp_iv_icon,item.leftCover?:0)
                        setText(R.id.find_sp_tv_title,item.poetryTitle)
                        setText(R.id.find_tv_sp_author,item.poetryAuthor)
                    }
                }
            }
        }
        poetryFindRv.submitList(homeData)
    }
    private fun initDataSource() {
        homeData.add(PoetryF1Title("全部诗人","查看全部"))
        val contentList = mutableListOf<PoetryF1Content>()
        contentList.add(PoetryF1Content(R.drawable.icon2,"对对对","李白"))
        contentList.add(PoetryF1Content(R.drawable.icon2,"擦擦擦","杜甫"))
        contentList.add(PoetryF1Content(R.drawable.icon1,"啧啧啧","王维"))
        contentList.add(PoetryF1Content(R.drawable.icon2,"滚滚滚","白居易"))
        contentList.add(PoetryF1Content(R.drawable.icon2,"急急急","李商隐"))
        contentList.add(PoetryF1Content(R.drawable.icon1,"哦哦哦","杜牧"))
        homeData.add(PoetryF1ContentList(contentList))

        homeData.add(PoetryF2Title(title = "朝代"))
        homeData.add(PoetryF2Content(R.drawable.pic8, "担惊受恐","唐诗"))
        homeData.add(PoetryF2Content(R.drawable.pic11, "担惊受恐","宋词"))
        homeData.add(PoetryF2Content(R.drawable.pic8, "担惊受恐","其他朝代"))

        homeData.add(PoetryF3title(title = "经典名句"))
        homeData.add(PoetryF3Content(R.drawable.pic8, "担惊受恐","不限"))
        homeData.add(PoetryF3Content(R.drawable.pic11, "担惊受恐","抒情"))
        homeData.add(PoetryF3Content(R.drawable.pic8, "担惊受恐","山水"))

        homeData.add(PoetryF4title("名篇","查看全部"))
        val mpList = mutableListOf<PoetryF4Content>()
        mpList.add(PoetryF4Content(R.drawable.icon2,"对对对","岳阳楼记"))
        mpList.add(PoetryF4Content(R.drawable.icon2,"擦擦擦","滕王阁序"))
        mpList.add(PoetryF4Content(R.drawable.icon1,"啧啧啧","蜀道难"))
        mpList.add(PoetryF4Content(R.drawable.icon2,"滚滚滚","小石潭记"))
        mpList.add(PoetryF4Content(R.drawable.icon2,"急急急","桃花源记"))
        homeData.add(PoetryF4ContentList(mpList))

        homeData.add(PoetryF5title("每日推荐"))
        val recommendList = mutableListOf<PoetryF5Content>()
        recommendList.add(PoetryF5Content(R.drawable.icon2,"对对对","推荐1"))
        recommendList.add(PoetryF5Content(R.drawable.icon2,"擦擦擦","推荐2"))
        recommendList.add(PoetryF5Content(R.drawable.icon2,"擦擦擦","推荐3"))
        homeData.add(PoetryF5ContentList(recommendList))

        homeData.add(PoetryF6title("每日拾得","换一批"))
        homeData.add(PoetryF6Content(leftCover = R.drawable.icon1,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        homeData.add(PoetryF6Content(leftCover = R.drawable.icon2,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        homeData.add(PoetryF6Content(leftCover = R.drawable.icon1,poetryTitle = "江湖夜雨",poetryAuthor = "黄庭坚"))
        homeData.add(PoetryF6Content(leftCover = R.drawable.icon2,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        homeData.add(PoetryF6Content(leftCover = R.drawable.icon1,poetryTitle = "一蓑烟雨任平生",poetryAuthor = "苏轼"))
        homeData.add(PoetryF6Content(leftCover = R.drawable.icon2,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        homeData.add(PoetryF6Content(leftCover = R.drawable.icon1,poetryTitle = "谁画中与你天涯",poetryAuthor = "雨露深谷"))
    }
    inner class PoetryF1ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val cover:ImageView = itemView.findViewById(R.id.poetry_item_f_a_iv)
        val desc: TextView = itemView.findViewById(R.id.poetry_item_f_a_tv)
    }
}