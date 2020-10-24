package com.journey.interview.imusic.util

import android.util.Log
import android.view.MenuItem
import com.journey.interview.R
import com.journey.interview.imusic.news.INewMainActivity

/**
 * @By Journey 2020/10/24
 * @Description
 */
class NavigationManager constructor(private val activity: INewMainActivity) {
    fun onNavigationItemSelected(item:MenuItem):Boolean {
        when(item.itemId) {
            R.id.action_personalized_dress->{
                Log.e("JG","action_personalized_dress")
                // todo
                return true
            }
            R.id.action_help_feedback->{
                Log.e("JG","action_help_feedback")
                // todo
                return true
            }
            R.id.action_timed_off->{
                Log.e("JG","action_timed_off")
                // todo
                return true
            }
            R.id.action_about_me->{
                Log.e("JG","action_about_me")
                // todo
                return true
            }
            R.id.action_night_mode->{
                Log.e("JG","action_night_mode")
                // todo
                return true
            }
        }
        return false
    }
}