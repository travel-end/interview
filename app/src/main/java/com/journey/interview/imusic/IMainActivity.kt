package com.journey.interview.imusic

import androidx.navigation.Navigation
import com.google.android.material.shape.ShapeAppearanceModel
import com.journey.interview.R
import com.journey.interview.imusic.vm.IMainViewModel
import com.journey.interview.weatherapp.base.BaseLifeCycleActivity
import kotlinx.android.synthetic.main.imusic_act_main.*
import kotlinx.android.synthetic.main.imusic_main_player.view.*

/**
 * @By Journey 2020/9/25
 * @Description
 */
class IMainActivity : BaseLifeCycleActivity<IMainViewModel>() {
    override fun layoutResId() = R.layout.imusic_act_main
    override fun initView() {
        super.initView()
        bottom_player.player_song_icon.shapeAppearanceModel =
            ShapeAppearanceModel.Builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL)
                .build()
    }


//    override fun onSupportNavigateUp(): Boolean {
//        return Navigation.findNavController(this, R.id.nav_imusic).navigateUp()
//    }
}