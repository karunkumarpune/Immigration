package com.immigration.controller.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.controller.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_login)


        btn_login_skip.setOnClickListener {
            startActivity(Intent(this,NavigationActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

        btn_login.setOnClickListener {
            startActivity(Intent(this,NavigationActivity::class.java))
        }

        login_btn_signup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }

        btn_forgot_pass.setOnClickListener {
          // Utils.showToast(this,"hiii",Color.CYAN)
           startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }
}
