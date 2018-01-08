package com.immigration.controller.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.controller.home.NavigationActivity
import com.immigration.utils.CustomProgressBar

class SplashActivity : AppCompatActivity() {

    private lateinit var pb: CustomProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, NavigationActivity::class.java))
        }, 100)

    }
}
