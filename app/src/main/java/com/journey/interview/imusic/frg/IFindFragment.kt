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
import com.journey.interview.customizeview.pager.StackLayoutManager
import com.journey.interview.imusic.model.*
import com.journey.interview.imusic.vm.IFindViewModel
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
class IFindFragment : BaseLifeCycleFragment<IFindViewModel>() {
    private val findData: MutableList<Any> = ArrayList()
    private lateinit var focusLayoutManager: FocusLayoutManager
    companion object {
        fun newInstance(): Fragment {
            return IFindFragment()
        }
    }

    override fun layoutResId() = R.layout.imusic_frg_find
    override fun initView() {
        super.initView()
        initDataSource()
        find_rv.setup<Any> {
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
            // 首页的5个选项
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
                            .layerPadding(50f.toFloatPx())
                            .normalViewGap(14f.toFloatPx())
                            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                            .isAutoSelect(true)
                            .maxLayerCount(3)
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
                                            topMargin = 25f.toIntPx(view.context)
                                            bottomMargin = 25f.toIntPx(view.context)
                                            leftMargin = 0f.toIntPx(view.context)
                                            rightMargin = 0f.toIntPx(view.context)
                                            width = 100f.toIntPx(view.context)
                                            height = 150f.toIntPx(view.context)
                                        }
                                    } else {
                                        val p = view?.layoutParams as ViewGroup.MarginLayoutParams
                                        p.apply {
                                            topMargin = 0f.toIntPx(view.context)
                                            bottomMargin = 0f.toIntPx(view.context)
                                            leftMargin = 25f.toIntPx(view.context)
                                            rightMargin = 25f.toIntPx(view.context)
                                            width = 150f.toIntPx(view.context)
                                            height = 100f.toIntPx(view.context)
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
                    }
                }

                //首页歌单
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
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is FindTitle }
                    bindViewHolder { data, pos, _ ->
                        val title = data as FindTitle
                        setText(R.id.find_tv_title_main, title.titleMain)
                        setText(R.id.find_mb_second, title.titleSecond)
                    }
                }
            }
        }
        find_rv.submitList(findData)
    }

    private fun initDataSource() {
        val topItem = mutableListOf<FindTopItem>()
        topItem.add(FindTopItem(R.drawable.cover10, "zz"))
        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "dd"))
        topItem.add(FindTopItem(R.drawable.cover10, "g"))
        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "gfd"))
        topItem.add(FindTopItem(R.drawable.cover10, "jjj"))
        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "g"))
        topItem.add(FindTopItem(R.drawable.cover10, "g"))
        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "gfd"))
        topItem.add(FindTopItem(R.drawable.cover10, "jjj"))
        topItem.add(FindTopItem(R.drawable.ic_wel_bg, "g"))
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

//        findData.add(SongList(R.drawable.icon1, "温柔英文歌-睡觉专用", "1988万"))
//        findData.add(SongList(R.drawable.icon1, "2020年Billboard公告牌音乐奖提名", "14万"))
//        findData.add(SongList(R.drawable.icon1, "写作业专用 清华自习室音乐 集中注意力写作业", "1255万"))
//        findData.add(SongList(R.drawable.icon1, "民谣不止安河桥", "185万"))


        findData.add(FindTitle("刮来一阵热情嘻哈风", "播放全部", true))

//        val recommendSongList = mutableListOf<RecommendSong>()
//        recommendSongList.add(RecommendSong())
    }

//    private fun initBannerPager(bannerPager:ScrollBannerPager?) {
//        for (i in 0..4) {
//            val cover = BannerPagerCover(requireContext())
//            cover.setBannerCover(R.drawable.pic11)
//            cover.setText("遇见你 山河已秋")
//            bannerPager?.addView(cover)
//            val lp = cover.layoutParams as ScrollBannerPager.ScrollBannerLayoutParams
//            lp.from = i
//            lp.to = i
//            lp.index = i
////            bannerList.add(cover)
//        }

//    }

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
                if (pos == this@IFindFragment.focusLayoutManager.focusdPosition) {
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


    private fun initLayoutManager() {

    }
}