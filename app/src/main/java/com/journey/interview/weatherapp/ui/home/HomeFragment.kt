package com.journey.interview.weatherapp.ui.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.journey.interview.R
import com.journey.interview.weatherapp.base.BaseLifeCycleFragment
import kotlinx.android.synthetic.main.wea_frg_home.*

/**
 * @By Journey 2020/9/15
 * @Description  显示具体城市的具体天气情况
 */
class HomeFragment:BaseLifeCycleFragment<HomeViewModel>() {
    override fun layoutResId()= R.layout.wea_frg_home
    override fun initView() {
        super.initView()
        home_bar.title = "Weather"
        (requireActivity() as AppCompatActivity).setSupportActionBar(home_bar)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_city->{
                // Destination id == 0 can only be used in conjunction with a valid navOptions.popUpTo
                Navigation.findNavController(home_bar).navigate(R.id.action_homeFragment_to_choosePlaceFragment)
            }
            R.id.action_more->{

            }
        }
        return super.onOptionsItemSelected(item)
    }
}