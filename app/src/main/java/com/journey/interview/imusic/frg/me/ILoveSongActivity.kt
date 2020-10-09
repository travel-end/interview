//package com.journey.interview.imusic.frg.me
//
//import android.view.View
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.RecyclerView
//import com.journey.interview.R
//import com.journey.interview.imusic.global.IMusicBus
//import com.journey.interview.imusic.model.ListBean
//import com.journey.interview.imusic.model.LoveSong
//import com.journey.interview.imusic.vm.ILoveSongViewModel
//import com.journey.interview.recyclerview.core.addItem
//import com.journey.interview.recyclerview.core.setText
//import com.journey.interview.recyclerview.core.setup
//import com.journey.interview.recyclerview.core.submitList
//import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
//import kotlinx.android.synthetic.main.imusic_act_love_song.*
//
//class ILoveSongActivity:BaseLifeCycleActivity<ILoveSongViewModel>() {
//    override fun layoutResId()= R.layout.imusic_act_love_song
//    override fun initView() {
//        super.initView()
//        val loveSongRv = findViewById<RecyclerView>(R.id.rv_love_song)
//        loveSongRv.setHasFixedSize(true)
//        loveSongRv.setup<LoveSong> {
//            adapter {
////                    addItem(R.layout.imusic_item_search_song_title) {
////                        isForViewType { data, position -> data is FindTitle }
////                        bindViewHolder { data, position, holder ->
////                            val d = data as FindTitle
////                            setText(R.id.tv_search_song_title,d.titleMain)
////                            setText(R.id.tv_search_song_duoxuan,d.titleSecond)
////                        }
////                    }
//                addItem(R.layout.imusic_item_search_song) {
////                        isForViewType { data, position -> data is MutableList<SingerBean> }
//                    bindViewHolder { data, position, holder ->
//                        data?.let {
////                            val sinGer = mViewModel.getSinger(data)
//                            setText(R.id.tv_song_singer,data.singer)
//                            setText(R.id.tv_song_name,data.name)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun initData() {
//        super.initData()
//        mViewModel.getAllMyLoveSong()
//    }
//
//    override fun dataObserve() {
//        super.dataObserve()
//        mViewModel.loveSongList.observe(this, Observer {
//            it?.let {list->
//                if (list.isEmpty()) {
//                    love_btn_add_song.visibility = View.VISIBLE
//                    rv_love_song.visibility = View.GONE
//                } else {
//                    rv_love_song.submitList(list)
//                }
//            }
//        })
//        IMusicBus.observeLoveSongChange(this) {
//                mViewModel.getAllMyLoveSong()
//        }
//    }
//
//    private fun test():MutableList<ListBean> {
//        val list = mutableListOf<ListBean>()
//        for (i in 0..12) {
//            val bean = ListBean()
//            bean.songname="$i zzz"
//            list.add(bean)
//        }
//        return list
//    }
//}