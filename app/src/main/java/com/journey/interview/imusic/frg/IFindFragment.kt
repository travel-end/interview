package com.journey.interview.imusic.frg

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.journey.interview.R
import com.journey.interview.imusic.model.*
import com.journey.interview.imusic.vm.IFindViewModel
import com.journey.interview.recyclerview.core.*
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.imusic_find_rv_menu.*
import kotlinx.android.synthetic.main.imusic_find_rv_song_list.*
import kotlinx.android.synthetic.main.imusic_frg_find.*
import java.util.*

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IFindFragment : BaseLifeCycleFragment<IFindViewModel>() {
    private val findData: MutableList<Any> = ArrayList()
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
            adapter {
                addItem(R.layout.imusic_find_item_top) {
                    isForViewType { data, _ -> data is FindTop }
                    bindViewHolder { data, _, _ ->
                        val findTop = data as FindTop
                        setImageResource(R.id.find_iv_topview, findTop.icon)
                    }
                }

                addItem(R.layout.imusic_find_rv_menu) {
                    isForViewType { data, _ -> data is FindMenuList }
                    bindViewHolder { data, _, _ ->
                        val menuList = data as FindMenuList
                        itemView?.findViewById<RecyclerView>(R.id.find_rv_menu_list)?.setup<FindMenu> {
                            dataSource(menuList.list)
                            adapter {
                                addItem(R.layout.imusic_find_item_menu) {
                                    bindViewHolder { data: FindMenu?, position: Int, holder: ViewHolderCreator<FindMenu> ->
                                        val songList = data as FindMenu
                                        val roundIv = itemView?.findViewById<ShapeableImageView>(R.id.find_iv_menu_res)
                                        roundIv?.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                                        setImageResource(R.id.find_iv_menu_res,songList.iconRes)
                                        setText(R.id.find_tv_menu_name,songList.menuName)
                                    }
                                }
                            }
                        }

                    }
                }
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is FindTitle }
                    bindViewHolder { data, pos, _ ->
                        val title = data as FindTitle
                        setText(R.id.find_tv_title_main,title.titleMain)
                        setText(R.id.find_mb_second,title.titleSecond)
                    }
                }
                addItem(R.layout.imusic_find_rv_song_list) {
                    isForViewType { data, _ -> data is SongListAll }
                    bindViewHolder { data, _, _ ->
                        val songListAll = data as SongListAll
                        itemView?.findViewById<RecyclerView>(R.id.find_rv_song_list)?.setup<SongList> {
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
                addItem(R.layout.imusic_find_item_title) {
                    isForViewType { data, _ -> data is FindTitle }
                    bindViewHolder { data, pos, _ ->
                        val title = data as FindTitle
                        setText(R.id.find_tv_title_main,title.titleMain)
                        setText(R.id.find_mb_second,title.titleSecond)
                    }
                }
            }
        }
        find_rv.submitList(findData)
    }

    private fun initDataSource() {
        findData.add(FindTop(R.drawable.cover3))
        val menuList = mutableListOf<FindMenu>()
        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"每日推荐"))
        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"私人FM"))
        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"歌单"))
        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"排行榜"))
        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"直播"))
        menuList.add(FindMenu(R.drawable.vector_drawable_ic_heavy_snow,"电台"))
        findData.add(FindMenuList(menuList))

        findData.add(FindTitle("你的歌单精选站","查看更多"))

        val songList = mutableListOf<SongList>()
        songList.add(SongList(R.drawable.icon1,"温柔英文歌-睡觉专用","1988万"))
        songList.add(SongList(R.drawable.icon1,"2020年Billboard公告牌音乐奖提名","14万"))
        songList.add(SongList(R.drawable.icon1,"写作业专用 清华自习室音乐 集中注意力写作业","1255万"))
        songList.add(SongList(R.drawable.icon1,"民谣不止安河桥","185万"))
        findData.add(SongListAll(songList))


        findData.add(FindTitle("刮来一阵热情嘻哈风","播放全部",true))

//        val recommendSongList = mutableListOf<RecommendSong>()
//        recommendSongList.add(RecommendSong())
    }
}