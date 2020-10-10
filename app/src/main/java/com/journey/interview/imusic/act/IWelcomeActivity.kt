package com.journey.interview.imusic.act

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.journey.interview.R
import com.journey.interview.imusic.IMainActivity
import com.journey.interview.imusic.vm.IWelcomeViewModel
import com.journey.interview.weatherapp.base.BaseActivity

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

//    override fun initStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window?.run {
//                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                statusBarColor = Color.TRANSPARENT
//            }
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                window.addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                )
//            }
//        }
//    }

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
        startActivity(Intent(this,IMainActivity::class.java))
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
            } else {
                Toast.makeText(this, "拒绝该权限无法使用该程序", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}