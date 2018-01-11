package com.immigration.controller.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import kotlinx.android.synthetic.main.activity_otp.*

class OTPActivity : AppCompatActivity() {

    private var session_otp:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_otp)

        session_otp=intent.getStringExtra("session_otp")


        otp_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        btn_submit_otp.setOnClickListener {

            if(session_otp.equals("0")){
                startActivity(Intent(this, EditProfileActivity::class.java)
                        .putExtra("session_edit_profile","0")
                )
            }else
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

    }
}
