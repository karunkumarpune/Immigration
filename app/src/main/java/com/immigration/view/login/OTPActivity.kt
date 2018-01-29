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
import android.widget.EditText
import android.widget.TextView
import com.immigration.R
import com.immigration.appdata.Constant
import com.immigration.appdata.Constant.key_otp
import com.immigration.appdata.Constant.key_type
import com.immigration.appdata.Constant.key_userId
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.utils.Utils.errorHandler
import kotlinx.android.synthetic.main.activity_otp.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class OTPActivity : AppCompatActivity() {
   private var APIService: APIService? = null
   private lateinit var pb: CustomProgressBar
   private val TAG = OTPActivity::class.java.name
   private var loginPreference: LoginPrefences? = null
   private var snackbarMessage: String? = null
   private var session_otp: String = ""
   private var userId: String = ""
   private var contact: String = ""
   private var otp: String = ""
   private lateinit var et_otp: EditText
   private lateinit var otp_txt_mobile: TextView
   // Receiver Broadcast SMS
   private val receiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
         if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            otp = intent.getStringExtra("otp")
            Utils.log(TAG, "OTP data : $otp")
            
            if (session_otp == "0") {
               initJsonOperation("1", userId, otp)
            } else {
               initJsonOperation("2", userId, otp)
            }
         }
      }
   }
   
   
   override fun onResume() {
      super.onResume()
      et_otp = findViewById<EditText>(R.id.et_otp)
      APIService = ApiUtils.apiService
      loginPreference = LoginPrefences.getInstance()
      session_otp = intent.getStringExtra("session_otp")
      userId = intent.getStringExtra("user_id")
      Utils.log(TAG, "OTP data onResume : session_otp $session_otp , $userId")
      LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
   }
   
   override fun onPause() {
      super.onPause()
      LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
   }
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Utils.moveLeftToRight(this)
      setContentView(R.layout.activity_otp)
      et_otp = findViewById<EditText>(R.id.et_otp)
      otp_txt_mobile = findViewById<EditText>(R.id.otp_txt_mobiles)
      
      loginPreference = LoginPrefences.getInstance()
      APIService = ApiUtils.apiService
      session_otp = intent.getStringExtra("session_otp")
      userId = intent.getStringExtra("user_id")
      contact = intent.getStringExtra("contact")
      Utils.log(TAG, "OTP data  : session_otp $session_otp , $userId")
      otp_txt_mobile.text = Constant.countryCodeValues + "-" + contact
      initView()
      initViewEditTextListener()
      initViewSubmitOTPListener()
      
      otp_txt_resend.setOnClickListener {
         initJsonResend(userId)
      }
   }
   
   private fun initView() {
         otp_btn_click_back.setOnClickListener {
         onBackPressed()
            Utils.moveLeftToRight(this)
   
         }
   }
   
   private fun initViewSubmitOTPListener() {
      btn_submit_otp.setOnClickListener {
         val edit_otp = et_otp.text.toString().trim()
         if (edit_otp.isEmpty()) {
            Utils.showToastSnackbar(this@OTPActivity, getString(R.string.otp_validation_1), Color.WHITE)
            et_otp.requestFocus()
         } else {
            if (session_otp == "0") {
               initJsonOperation("1", userId, edit_otp)
            } else {
               initJsonOperation("2", userId, edit_otp)
            }
         }
      }
   }
   
   private fun initViewEditTextListener() {
      et_otp.addTextChangedListener(object : TextWatcher {
         override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
         }
         
         override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
         }
         
         override fun afterTextChanged(messages: Editable) {
            try {
               if (et_otp.text.toString().length == 4) {
                  val v = this@OTPActivity.currentFocus
                  if (et_otp.text.toString() == et_otp.text.toString()) {
                     if (session_otp == "0") {
                        Utils.hideSoftKeyboad(applicationContext, v)
                        initJsonOperation("1", userId, et_otp.text.toString())
                     } else {
                        Utils.hideSoftKeyboad(applicationContext, v)
                        initJsonOperation("2", userId, et_otp.text.toString())
                     }
                  } else {
                     Utils.hideSoftKeyboad(applicationContext, v)
                     //  Utils.showToastSnackbar(this@OTPActivity, getString(R.string.action_sms), Color.RED)
                  }
               } else {
               }
            } catch (e: Exception) {
               //    Utils.showToastSnackbar(this@OTPActivity, getString(R.string.action_sms), Color.RED)
            }
         }
      })
   }
   
   private fun initJsonOperation(type: String, userIds: String, otp: String) {
      pb = CustomProgressBar(this)
      pb.setCancelable(false)
      pb.show()
      
      
      if (session_otp == "0") {
         val requestBody = HashMap<String, String>()
         requestBody.put(key_type, type)
         requestBody.put(key_userId, userIds)
         requestBody.put(key_otp, otp)
         
         APIService!!.verifyOtp(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
               pb.dismiss()
               val status = response!!.code()
               when (status) {
                  200 -> {
                     val email = response.body()!!.result!!.email
                     val countryCode = response.body()!!.result!!.countryCode
                     val contact = response.body()!!.result!!.contact
                     val accessToken = response.body()!!.result!!.accessToken
                     
                     Constant.accessTokenValues = accessToken!!
                     Constant.countryCodeValues = countryCode!!
                     Constant.contactValues = contact!!
                     
                     startActivity(Intent(this@OTPActivity, EditProfileActivity::class.java)
                      .putExtra("session_edit_profile", "0")
                      .putExtra("otp_email", email))
                     finish()
                  }
                  401 -> {
                     Utils.showToast(applicationContext, errorHandler(response))
                     Utils.invalidToken(this@OTPActivity, loginPreference, LoginActivity())
                  }
               }
               snackbarMessage = Utils.responseStatus(this@OTPActivity, status, response)
               if (snackbarMessage != null) {
                  Utils.showToastSnackbar(this@OTPActivity, snackbarMessage!!, Color.WHITE)
               }
            }
            
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
               pb.dismiss()
               Utils.log(TAG, "OTP Throwable : $t")
               Utils.showToastSnackbar(this@OTPActivity, "Sorry!No internet available", Color.RED)
            }
         })
      } else {
         val requestBody = HashMap<String, String>()
         requestBody.put(key_type, type)
         requestBody.put(key_userId, userIds)
         requestBody.put(key_otp, otp)
         
         APIService!!.verifyOtp(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
               pb.dismiss()
               val status = response!!.code()
               when (status) {
                  200 -> {
                     startActivity(Intent(this@OTPActivity, ResetPasswordActivity::class.java)
                      .putExtra("userId", userId))
                     
                  }
                  401 -> {
                     Utils.showToast(applicationContext, errorHandler(response))
                     Utils.invalidToken(this@OTPActivity, loginPreference, LoginActivity())
                  }
               }
               snackbarMessage = Utils.responseStatus(this@OTPActivity, status, response)
               if (snackbarMessage != null) {
                  Utils.showToastSnackbar(this@OTPActivity, snackbarMessage!!, Color.WHITE)
               }
            }
            
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
               pb.dismiss()
               Utils.log(TAG, "otp:  Throwable-:   $t")
               Utils.showToastSnackbar(this@OTPActivity, getString(R.string.no_internet).toString(), Color.RED)
            }
         })
      }
   }
   
   
   private fun initJsonResend(userIds: String) {
      pb = CustomProgressBar(this)
      pb.setCancelable(false)
      pb.show()
      val requestBody = HashMap<String, String>()
      requestBody.put(key_userId, userIds)
      
      APIService!!.resendOtp(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
         override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
            pb.dismiss()
            val status = response!!.code()
            when (status) {
               401 -> {
                  Utils.showToast(applicationContext, Utils.errorHandler(response))
                  Utils.invalidToken(this@OTPActivity, loginPreference, LoginActivity())
               }
            }
            snackbarMessage = Utils.responseStatus(this@OTPActivity, status, response)
            if (snackbarMessage != null) {
               Utils.showToastSnackbar(this@OTPActivity, snackbarMessage!!, Color.WHITE)
            }
         }
         
         override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
            pb.dismiss()
            Utils.log(TAG, "otp:  Throwable-:   $t")
            Utils.showToastSnackbar(this@OTPActivity, getString(R.string.no_internet).toString(), Color.RED)
         }
      })
   }
}