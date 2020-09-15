package com.journey.interview.weatherapp.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.journey.interview.R
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.wea_frg_home.*

/**
 * @By Journey 2020/9/15
 * @Description
 */
class HomeFragment:BaseLifeCycleFragment<HomeViewModel>() {
    override fun layoutResId()= R.layout.wea_frg_home
    override fun initView() {
        super.initView()
        home_bar.title = ""
        (requireActivity() as AppCompatActivity).setSupportActionBar(home_bar)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }
}