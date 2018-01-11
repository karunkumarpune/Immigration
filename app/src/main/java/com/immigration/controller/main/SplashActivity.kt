package com.immigration.controller.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.controller.login.LoginActivity
import com.immigration.utils.CustomProgressBar

class SplashActivity : AppCompatActivity() {

    private lateinit var pb: CustomProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            startActivity(Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }, 2000)

    }
}
