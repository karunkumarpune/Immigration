package com.immigration.view.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.immigration.R
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_otp.*




class OTPActivity : AppCompatActivity() {


    private var session_otp:String = ""
    private var message:String = ""

    private lateinit var et_otp:EditText

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
                message = intent.getStringExtra("otp")
                Log.d("OTPActivity", "OTPActivity " + message)
                //  otp = message;
                et_otp.setText(message.toString())

                try {

                    if(message=="2244"){
                        if(session_otp.equals("0")){
                            startActivity(Intent(this@OTPActivity, EditProfileActivity::class.java)
                                    .putExtra("session_edit_profile","0")
                            )
                        }else
                            startActivity(Intent(this@OTPActivity, ResetPasswordActivity::class.java))
                    }else{
                        Utils.showToast(this@OTPActivity, getString(R.string.action_sms), Color.RED)
                    }
                }catch (e:Exception){
                    Utils.showToast(this@OTPActivity, getString(R.string.action_sms), Color.RED)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        et_otp=findViewById<EditText>(R.id.et_otp)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_otp)
        et_otp=findViewById<EditText>(R.id.et_otp)
        session_otp = intent.getStringExtra("session_otp")


        et_otp.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(messages: Editable) {

                try {
                    if(et_otp.text.toString().length==4){

                        if(et_otp.text.toString() == "2244"){
                            if(session_otp == "0"){
                                startActivity(Intent(this@OTPActivity, EditProfileActivity::class.java)
                                        .putExtra("session_edit_profile","0")
                                )
                            }else
                                startActivity(Intent(this@OTPActivity, ResetPasswordActivity::class.java))
                           }
                        else{
                            val view1 = this@OTPActivity.getCurrentFocus()
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view1.windowToken, 0)
                            Utils.showToast(this@OTPActivity, getString(R.string.action_sms), Color.RED)
                        }
                    }else{ }
                }catch (e:Exception){
                    Utils.showToast(this@OTPActivity, getString(R.string.action_sms), Color.RED)
                }

            }
        })



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




}
