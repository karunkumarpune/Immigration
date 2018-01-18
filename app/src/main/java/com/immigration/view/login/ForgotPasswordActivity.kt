package com.immigration.view.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.restservices.APIService
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private val TAG = ForgotPasswordActivity::class.java!!.getName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_forgot_password)


        forgot_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btn_forgot_pass.setOnClickListener {
            startActivity(Intent(this, OTPActivity::class.java)
                    .putExtra("session_otp","1")
            ) }

        initViewSubmit()
    }

    private fun initViewSubmit() {
        btn_forgot_pass.setOnClickListener { v ->
            val mob = forgot_et.text.toString()
            if (mob.isEmpty()) {
                Utils.showToast(this@ForgotPasswordActivity, getString(R.string.login_validation_1), Color.RED)
                forgot_et.requestFocus()
            }else {
                initJsonOperation(mob)
            }
        }
    }

    private fun initJsonOperation(mob:String) {
        Utils.log(TAG!!, "Forgot data : $mob ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        Handler().postDelayed({pb.dismiss()
            startActivity(Intent(this, OTPActivity::class.java)
                    .putExtra("session_otp","1")
                    .putExtra("user_id","58")
            )
        },2000)


    }


}
