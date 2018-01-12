package com.immigration.view.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.immigration.R
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_otp.*


class OTPActivity : AppCompatActivity() {

    private var session_otp:String = ""


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
                val message = intent.getStringExtra("otp")
                Log.d("message1", "message1 " + message)
                //  otp = message;
                et_otp.setText(message.toString())
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_otp)

        session_otp = intent.getStringExtra("session_otp")

        initView()
    }

    private fun initView() {

    // Click Back Button
        otp_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        //Click OTP Button
        btn_submit_otp.setOnClickListener {

            val edit_otp=et_otp.text.toString()
            if(edit_otp.isEmpty()){
                Utils.showToast(this@OTPActivity,getString(R.string.otp_validation_1), Color.RED)
                et_otp.requestFocus()
            }else{

            }




            if(session_otp.equals("0")){
                startActivity(Intent(this, EditProfileActivity::class.java)
                        .putExtra("session_edit_profile","0")
                )
            }else
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }


}
