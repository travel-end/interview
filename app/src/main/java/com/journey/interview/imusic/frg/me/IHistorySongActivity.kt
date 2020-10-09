//package com.journey.interview.imusic.frg.me
//
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.RecyclerView
//import com.journey.interview.R
//import com.journey.interview.imusic.model.HistorySong
//import com.journey.interview.imusic.vm.IHistorySongViewModel
//import com.journey.interview.recyclerview.core.addItem
//import com.journey.interview.recyclerview.core.setText
//import com.journey.interview.recyclerview.core.setup
//import com.journey.interview.recyclerview.core.submitList
//import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
//import kotlinx.android.synthetic.main.imusic_act_history.*
//
///**
// * 最近播放的曲目
// */
//class IHistorySongActivity:BaseLifeCycleActivity<IHistorySongViewModel>() {
//    private lateinit var mRvHistory:RecyclerView
//    override fun layoutResId()=R.layout.imusic_act_history
//    override fun initView() {
//        super.initView()
//        mRvHistory = rv_history_songs
//        mRvHistory.setup<HistorySong> {
//            adapter {
//                addItem(R.layout.imusic_item_search_song) {
//                    bindViewHolder { data, position, holder ->
//                        setText(R.id.tv_song_name,data?.name)
//                    }
//                }
//            }
//        }
//        mViewModel.getAllHistorySongs()
//    }
//
//    override fun dataObserve() {
//        super.dataObserve()
//        mViewModel.historySongsResult.observe(this,Observer{
//            it?.let {list->
//                if (list.isNotEmpty()) {
//                    mRvHistory.submitList(list)
//                }
//            }
//        })
//    }
//}