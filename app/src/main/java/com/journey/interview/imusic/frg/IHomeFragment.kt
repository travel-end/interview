package com.journey.interview.imusic.frg

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.journey.interview.R
import com.journey.interview.customizeview.pager.FocusLayoutManager
import com.journey.interview.imusic.model.*
import com.journey.interview.imusic.vm.IHomeViewModel
import com.journey.interview.recyclerview.core.*
import com.journey.interview.utils.toFloatPx
import com.journey.interview.utils.toIntPx
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.imusic_frg_find.*
import java.util.*

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IHomeFragment : BaseLifeCycleFragment<IHomeViewModel>() {
    private val findData: MutableList<Any> = ArrayList()
    private lateinit var findRv:RecyclerView
    private lateinit var focusLayoutManager: FocusLayoutManager
    companion object {
        fun newInstance(): Fragment {
            return IHomeFragment()
        }
    }

    override fun layoutResId() = R.layout.imusic_frg_find
    override fun initView() {
        super.initView()
        findRv = find_rv
        initDataSource()
        findRv.setup<Any> {
            withLayoutManager {
                val lm = GridLayoutManager(requireContext(), 5)
                lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter?.getItem(position)) {
                            is FindMenu -> 1
//                            is SongList -> 1
                            else -> 5
                        }
                    }
                }
                return@withLayoutManager lm
            }
            // 首页顶部banner
            adapter {
                addItem(R.layout.imusic_find_item_top) {
                    isForViewType { data, _ -> data is FindTop }
                    bindViewHolder { data, _, _ ->
                        val findTop = data as FindTop
                        val datas = findTop.banners
                        val rv = itemView?.findViewById<RecyclerView>(R.id.stack_find_rv_top)
//                        val emptyView = itemView?.findViewById<TextView>(R.id.empty)
                        var index = 0
//                        Log.e("JG","rv:$rv")
                        focusLayoutManager = FocusLayoutManager.Builder()
                            .layerPadding(80f.toFloatPx())
                            .normalViewGap(12f.toFloatPx())
                            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                            .isAutoSelect(true)
                            .maxLayerCount(2)
                            .setOnFocusChangeListener { focusdPosition, lastFocusedPosition ->
                                if (focusdPosition == datas.size - 1
                                    && (focusLayoutManager.focusOrientation == FocusLayoutManager.FOCUS_LEFT)) {
//                                    emptyView?.visibility = View.VISIBLE
                                } else {
//                                    emptyView?.visibility = View.GONE
                                }
                            }
                            .build()
                        rv?.let {
                            it.adapter = object :RecyclerView.Adapter<TopViewHolder>() {
                                override fun onCreateViewHolder(
                                    parent: ViewGroup,
                                    viewType: Int
                                ): TopViewHolder {
                                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card,parent,false)
                                    if (focusLayoutManager.focusOrientation == FocusLayoutManager.FOCUS_LEFT || focusLayoutManager.focusOrientation == FocusLayoutManager.FOCUS_RIGHT) {
                                        val p = view?.layoutParams as ViewGroup.MarginLayoutParams
                                        p.apply {
                                            topMargin = 20f.toIntPx(view.context)
                                            bottomMargin = 20f.toIntPx(view.context)
                                            leftMargin = 0f.toIntPx(view.context)
                                            rightMargin = 0f.toIntPx(view.context)
                                            width = 180f.toIntPx(view.context)
                                            height = 110f.toIntPx(view.context)
                                        }
                                    } else {
                                        val p = view?.layoutParams as ViewGroup.MarginLayoutParams
                                        p.apply {
                                            topMargin = 0f.toIntPx(view.context)
                                            bottomMargin = 0f.toIntPx(view.context)
                                            leftMargin = 20f.toIntPx(view.context)
                                            rightMargin = 20f.toIntPx(view.context)
                                            width = 180f.toIntPx(view.context)
                                            height = 110f.toIntPx(view.context)
                                        }
                                    }
                                    view.tag = ++index
                                    return TopViewHolder(view)
                                }

                                override fun getItemCount()=Integer.MAX_VALUE

                                override fun onBindViewHolder(
                                    holder: TopViewHolder,
                                    position: Int
                                ) {
                                    val realPosition = position % datas.size
                                    val item = datas[realPosition]
                                    holder.iv.setImageResource(item.cover)
                                    holder.tv.text = item.desc
                                }

                            }
                            it.layoutManager = focusLayoutManager
                        }


                    }
                }

//                addItem(R.layout.imusic_find_rv_menu) {
//                    isForViewType { data, _ -> data is FindMenuList }
//                    bindViewHolder { data, _, _ ->
//                        val menuList = data as FindMenuList
//                        itemView?.findViewById<RecyclerView>(R.id.find_rv_menu_list)?.setup<FindMenu> {
//                            dataSource(menuList.list)
//                            adapter {
//                                addItem(R.layout.imusic_find_item_menu) {
//                                    bindViewHolder { data: FindMenu?, position: Int, holder: ViewHolderCreator<FindMenu> ->
//                                        val songList = data as FindMenu
//                                        val roundIv = itemView?.findViewById<ShapeableImageView>(R.id.find_iv_menu_res)
//                                        roundIv?.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
//                                        setImageResource(R.id.find_iv_menu_res,songList.iconRes)
//                                        setText(R.id.find_tv_menu_name,songList.menuName)
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                }
                addItem(R.layout.imusic_find_item_menu) {
                    isForViewType { data, _ -> data is FindMenu }
                    bindViewHolder { data, _, _ ->
                        val menuList = data as FindMenu
                        setText(R.id.find_tv_menu_name, menuList.menuName)
                        val roundIv = itemView?.findViewById<ShapeableImageView>(R.id.find_iv_menu_res)
                        roundIv?.shapeAppearanceModel = ShapeAppearanceModel.Builder()
                            .setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                        roundIv?.setImageResource(menuList.iconRes)
                        itemClicked(View.OnClickListener {
                            Log.e("JG","${menuList.menuName}")
                        })
//                        itemView?.findViewById<RecyclerView>(R.id.find_rv_menu_list)?.setup<FindMenu> {
//                            dataSource(menuList.list)
//                            adapter {
//                                addItem(R.layout.imusic_find_item_menu) {
//                                    bindViewHolder { data: FindMenu?, position: Int, holder: ViewHolderCreator<FindMenu> ->
//                                        val songList = data as FindMenu
//                                        val roundIv = itemView?.findViewById<ShapeableImageView>(R.id.find_iv_menu_res)
//                                        roundIv?.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
//                                        setImageResource(R.id.find_iv_menu_res,songList.iconRes)
//                                        setText(R.id.find_tv_menu_name,songList.menuName)
//                                    }
//                                }
//                            }
//                        }

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

                /* 滚动歌单*/
                addItem(R.layout.imusic_find_rv_song_list) {
                    isForViewType { data, _ -> data is SongListAll }
                    bindViewHolder { data, _, _ ->
                        val songListAll = data as SongListAll
                        val songRv = itemView?.findViewById<RecyclerView>(R.id.find_rv_song_list)
                        // item吸附效果
                        songRv?.onFlingListener=null
                        val snapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(songRv)
                        songRv?.setup<SongList> {
                            dataSource(songListAll.allList)
                            adapter {
                                addItem(R.layout.imusic_find_item_ho_list) {
                                    bindViewHolder { data: SongList?, position: Int, holder: ViewHolderCreator<SongList> ->
                                        val songList = data as SongList
                                        setImageResource(R.id.find_iv_menu_icon,songList.songCover)
                                        setText(R.id.find_tv_volume,songList.volume)
                                        setText(R.id.find_tv_desc,songList.songDesc)
                                        itemClicked(View.OnClickListener {
                                            Log.e("JG","${songList.songDesc}")
                                        })
                                    }
                                }
                            }
                        }

                    }
                }

//                addItem(R.layout.imusic_find_item_ho_list) {
//                    isForViewType { data, _ -> data is SongList }
//                    bindViewHolder { data, _, _ ->
//                        val songList = data as SongList
//                        setImageResource(R.id.find_iv_menu_icon, songList.songCover)
//                        setText(R.id.find_tv_volume, songList.volume)
//                        setText(R.id.find_tv_desc, songList.songDesc)

//                        itemView?.findViewById<RecyclerView>(R.id.find_rv_song_list)?.setup<SongList> {
//                            dataSource(songListAll.allList)
//                            adapter {
//                                addItem(R.layout.imusic_find_item_ho_list) {
//                                    bindViewHolder { data: SongList?, position: Int, holder: ViewHolderCreator<SongList> ->
//                                        val songList = data as SongList
//                                        setImageResource(R.id.find_iv_menu_icon,songList.songCover)
//                                        setText(R.id.find_tv_volume,songList.volume)
//                                        setText(R.id.find_tv_desc,songList.songDesc)
//                                    }
//                                }
//                            }
//                        }

//                    }
//                }

                /* 换一批*/
                addItem(R.layout.imusic_find_item_three_title) {
                    isForViewType { data, _ -> data is ThreeFindTitle }
                    bindViewHolder { data, _, _ ->
                        val title = data as ThreeFindTitle
                        setText(R.id.find_tv_title_three, title.titleMain)
                        setText(R.id.find_change_second_three,title.titleSecond)
                        clicked(R.id.find_change_second_three,View.OnClickListener {
                            Log.e("JG","--->换一批")
                            findRv.updateData(9,HomeReSong(R.drawable.icon6,"dd","dd","dd"),false)
                            findRv.updateData(10,HomeReSong(R.drawable.icon6,"aaaaa","bbb","nnn"),false)
                            findRv.updateData(11,HomeReSong(R.drawable.icon6,"fff","dd","ffff"),false)
                        })
                    }
                }
                /* 推荐歌曲*/
                addItem(R.layout.imusic_find_three_song) {
                    isForViewType { data, position -> data is HomeReSong }
                    bindViewHolder { data, position, holder ->
                        val item = data as HomeReSong
                        setText(R.id.find_three_tv_name, item.songName)
                        setText(R.id.three_find_tv_singer, item.songSinger)
                        setText(R.id.three_tv_desc, item.desc)
                        setImageResource(R.id.three_iv_cover,item.leftCover?:0)
                    }
                }

                /* 听歌读诗*/
                addItem(R.layout.imusic_find_title_poetry) {
                    isForViewType { data, _ -> data is SpTitle }
                    bindViewHolder { data, pos, _ ->
                        val title = data as SpTitle
                        setText(R.id.find_tv_poetry, title.title)
                    }
                }
                /* 推荐诗歌*/
                addItem(R.layout.imusic_find_song_poetry) {
                    isForViewType { data, _ -> data is SongAndPoetry }
                    bindViewHolder { data, position, holder ->
                        val item =data as SongAndPoetry
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
        find_rv.submitList(findData)
    }

    private fun initDataSource() {
        val topItem = mutableListOf<FindTopItem>()
        topItem.add(FindTopItem(R.drawable.cover5, "1zz"))
        topItem.add(FindTopItem(R.drawable.cover9, "2d"))
        topItem.add(FindTopItem(R.drawable.cover5, "3g"))
        topItem.add(FindTopItem(R.drawable.cover9, "4gfd"))
        topItem.add(FindTopItem(R.drawable.cover5, "5jjj"))
//        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "g"))
//        topItem.add(FindTopItem(R.drawable.cover10, "g"))
//        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "gfd"))
//        topItem.add(FindTopItem(R.drawable.cover10, "jjj"))
//        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "g"))
        findData.add(FindTop(banners = topItem))

//        val menuList = mutableListOf<FindMenu>()
//        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"每日推荐"))
//        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"私人FM"))
//        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"歌单"))
//        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"排行榜"))
//        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"直播"))
//        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"电台"))
//        findData.add(FindMenuList(menuList))

        findData.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow, "每日推荐"))
        findData.add(FindMenu(R.drawable.vector_drawable_ic_light_snow, "私人FM"))
        findData.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow, "歌单"))
        findData.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow, "排行榜"))
        findData.add(FindMenu(R.drawable.vector_drawable_ic_light_snow, "小雨"))

        findData.add(FindTitle("你的歌单精选站", "查看更多"))

        val songList = mutableListOf<SongList>()
        songList.add(SongList(R.drawable.icon2,"温柔英文歌-睡觉专用","1988万"))
        songList.add(SongList(R.drawable.icon2,"KTV必点：有没有一首歌","255万"))
        songList.add(SongList(R.drawable.icon1,"2020年Billboard公告牌音乐奖提名","14万"))
        songList.add(SongList(R.drawable.icon2,"写作业专用 清华自习室音乐 集中注意力写作业","1255万"))
        songList.add(SongList(R.drawable.icon2,"民谣不止安河桥","185万"))
        songList.add(SongList(R.drawable.icon1,"民谣不止安河桥","185万"))
        findData.add(SongListAll(songList))

        findData.add(ThreeFindTitle("节奏空 嘻哈说唱", "换一批"))
        findData.add(HomeReSong(R.drawable.icon5, "小雨","作业","星辰大海"))
        findData.add(HomeReSong(R.drawable.icon2, "五行五杀","星晨","星辰大海"))
        findData.add(HomeReSong(R.drawable.icon5, "小雨","作业","星辰大海"))



        findData.add(SpTitle("诗＆歌"))



        findData.add(SongAndPoetry(leftCover = R.drawable.icon1,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        findData.add(SongAndPoetry(leftCover = R.drawable.icon2,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        findData.add(SongAndPoetry(leftCover = R.drawable.icon1,poetryTitle = "江湖夜雨",poetryAuthor = "黄庭坚"))
        findData.add(SongAndPoetry(leftCover = R.drawable.icon2,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        findData.add(SongAndPoetry(leftCover = R.drawable.icon1,poetryTitle = "一蓑烟雨任平生",poetryAuthor = "苏轼"))
        findData.add(SongAndPoetry(leftCover = R.drawable.icon2,poetryTitle = "都说你眼中开倾世桃花却如何一夕桃花雨下",poetryAuthor = "上邪"))
        findData.add(SongAndPoetry(leftCover = R.drawable.icon1,poetryTitle = "谁画中与你天涯",poetryAuthor = "雨露深谷"))



    }


    inner class TopViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tv: TextView
        var iv: ImageView

        init {
            tv = itemView.findViewById(R.id.item_tv)
            iv = itemView.findViewById(R.id.item_iv)
            itemView.setOnClickListener {
                val pos = absoluteAdapterPosition
//                if (MainActivity.mToast == null) {
//                    MainActivity.mToast = Toast.makeText(
//                        this@MainActivity, "" + pos,
//                        Toast.LENGTH_SHORT
//                    )
//                }
//                MainActivity.mToast.setText("" + pos)
//                MainActivity.mToast.show()
                if (pos == this@IHomeFragment.focusLayoutManager.focusdPosition) {
//                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
//                    intent.putExtra("resId", datas.get(pos).background)
//                    startActivity(
//                        intent,
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            this@MainActivity,
//                            itemView,
//                            "img"
//                        ).toBundle()
//                    )
                } else {
                    focusLayoutManager.setFocusdPosition(pos, true)
                }
            }
        }
    }
}