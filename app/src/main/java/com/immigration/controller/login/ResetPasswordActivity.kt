package com.immigration.controller.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_reset_password)
    }
}
