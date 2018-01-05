package com.immigration.login_info

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)



        forgot_btn_click_back.setOnClickListener {
            onBackPressed()
        }


    }

}
