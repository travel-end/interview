package com.journey.interview.customizeview.zpwdeditext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.journey.interview.R
import kotlinx.android.synthetic.main.activity_z_pwd_edittext.*

/**
 * 自定义密码输入框
 */
class ZEditTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_z_pwd_edittext)

        edit_1.setOnEditCompleteListener(object : ZEditText.OnEditCompleteListener{
            override fun onEditComplete(text: String) {
                Toast.makeText(this@ZEditTextActivity, "edit_1 text: $text", Toast.LENGTH_SHORT).show()
            }

        })

        edit_2.setOnEditCompleteListener(object : ZEditText.OnEditCompleteListener{
            override fun onEditComplete(text: String) {
                Toast.makeText(this@ZEditTextActivity, "edit_2 text: $text", Toast.LENGTH_SHORT).show()
            }

        })

        edit_3.setOnEditCompleteListener(object : ZEditText.OnEditCompleteListener{
            override fun onEditComplete(text: String) {
                Toast.makeText(this@ZEditTextActivity, "edit_3 text: $text", Toast.LENGTH_SHORT).show()
            }

        })

    }
}