package com.immigration.login_info

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signup_btn_login.setOnClickListener {
            startActivity(Intent(this,EditProfileActivity::class.java))
        }
        signup_btn.setOnClickListener {
            startActivity(Intent(this,ChangePasswordActivity::class.java))
        }
    }
}
