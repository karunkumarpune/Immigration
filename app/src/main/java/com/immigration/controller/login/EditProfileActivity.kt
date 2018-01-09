package com.immigration.controller.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.controller.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_edit_profile)


        edit_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        signup_btn_edit.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }
    }
}
