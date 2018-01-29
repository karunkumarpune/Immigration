package com.immigration.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.TokenSharedPrefManager
import com.immigration.utils.Utils
import com.immigration.view.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var pb: CustomProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        Utils.log("SplashActivity", "getDeviceToken:  ${TokenSharedPrefManager.getInstance(this@SplashActivity).deviceToken}")
        
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                     finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }, 2000)

    }
}
