package com.journey.interview.imusic.act

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.Constant
import com.journey.interview.R
import com.journey.interview.imusic.room.IMusicRoomHelper
import com.journey.interview.imusic.vm.IWelcomeViewModel
import com.journey.interview.utils.PhoneUtil
import com.journey.interview.utils.SpUtil
import com.journey.interview.weatherapp.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @By Journey 2020/9/24
 * @Description
 */
class IWelcomeActivity:BaseActivity<IWelcomeViewModel>() {
    private val mPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val mRequestCode = 10
    override fun layoutResId()= R.layout.imusic_act_wel

    override fun initStatusBar() {
        ImmersionBar.with(this).transparentBar().init()
    }

    override fun initData() {
        super.initData()
        if (ContextCompat.checkSelfPermission(this,mPermission)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(mPermission),mRequestCode)
        } else {
            window.decorView.postDelayed({
                startIMusicMain()
            },1500)
        }
    }

    private fun startIMusicMain() {
        startActivity(Intent(this,
            IMainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == mRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startIMusicMain()
//                getLocalSong()
            } else {
                Toast.makeText(this, "拒绝该权限无法使用该程序", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getLocalSong() {
        val firstLaunch = SpUtil.getString(Constant.FIRST_LAUNCH)
        if (firstLaunch.isEmpty()) {
            GlobalScope.launch {
                SpUtil.saveValue(Constant.FIRST_LAUNCH,"first_launch")
                withContext(Dispatchers.IO) {
                    val localSongList = PhoneUtil.getLocalSong()
                    Log.e("JG","--->InterviewApp 本地音乐：$localSongList")
                    if (!localSongList.isNullOrEmpty()) {
                        IMusicRoomHelper.saveLocalSong(localSongList)
                    }
                }
            }
        }
    }
}