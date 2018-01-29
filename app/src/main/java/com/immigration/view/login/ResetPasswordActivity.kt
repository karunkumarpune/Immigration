package com.immigration.view.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.appdata.Constant.key_password
import com.immigration.appdata.Constant.key_userId
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.utils.Utils.hideSoftKeyboad
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ResetPasswordActivity : AppCompatActivity() {
   private var APIService: APIService? = null
   private lateinit var pb: CustomProgressBar
   private val TAG = ResetPasswordActivity::class.java.name
   private var userId: String = ""
   private var userAccessToken: String? = null
   private var loginPreference: LoginPrefences? = null
   private var snackbarMessage: String? = null
   
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Utils.moveLeftToRight(this)
      setContentView(R.layout.activity_reset_password)
      loginPreference = LoginPrefences.getInstance()
      APIService = ApiUtils.apiService
      userAccessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(applicationContext))
      userId = intent.getStringExtra("userId")
      
      
      reset_btn_click_back.setOnClickListener {
         onBackPressed()
         Utils.moveLeftToRight(this)
      }
      
      initViewSubmit()
   }
   
   override fun onBackPressed() {
      super.onBackPressed()
      
      startActivity(Intent(this@ResetPasswordActivity, ForgotPasswordActivity::class.java)
       .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
      finish()
      Utils.moveLeftToRight(this)
   }
   
   private fun initViewSubmit() {
      btn_reset.setOnClickListener { v ->
         val pass = txt_et_new.text.toString()
         val conf_pass = txt_et_confirm.text.toString()
         
         if (pass.isEmpty()) {
            hideSoftKeyboad(this, v)
            Utils.showToastSnackbar(this@ResetPasswordActivity, getString(R.string.login_validation_reset), Color.WHITE)
            txt_et_new.requestFocus()
         } else if (pass.length < 8) {
            hideSoftKeyboad(this, v)
            Utils.showToastSnackbar(this@ResetPasswordActivity, getString(R.string.login_validation_valid), Color.WHITE)
            txt_et_new.requestFocus()
         } else if (conf_pass.isEmpty()) {
            hideSoftKeyboad(this, v)
            Utils.showToastSnackbar(this@ResetPasswordActivity, getString(R.string.signup_validation_4), Color.WHITE)
            txt_et_confirm.requestFocus()
         }/*else if(conf_pass.length <8){
                hideSoftKeyboad(v)
                Utils.showToastSnackbar(this@ResetPasswordActivity, getString(R.string.login_validation_valid), Color.WHITE)
                txt_et_confirm.requestFocus()
            }*/ else if (pass != conf_pass) {
            hideSoftKeyboad(this, v)
            Utils.showToastSnackbar(this@ResetPasswordActivity, getString(R.string.signup_validation_5), Color.WHITE)
            txt_et_confirm.requestFocus()
         } else {
            hideSoftKeyboad(this, v)
            initJsonOperation(pass)
         }
      }
   }
   
   private fun initJsonOperation(pass: String) {
      pb = CustomProgressBar(this)
      pb.setCancelable(false)
      pb.show()
      val requestBody = HashMap<String, String>()
      requestBody.put(key_password, pass)
      requestBody.put(key_userId, userId)
      APIService!!.setPassword(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
         override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
            pb.dismiss()
            val status = response!!.code()
            when (status) {
               200 -> {
                  Utils.showToast(applicationContext, response.body()!!.message.toString())
                  startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                   .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                  finish()
               }
               401 -> {
                  Utils.showToast(applicationContext, Utils.errorHandler(response))
                  Utils.invalidToken(this@ResetPasswordActivity, loginPreference, LoginActivity())
               }
            }
            snackbarMessage = Utils.responseStatus(this@ResetPasswordActivity, status, response)
            if (snackbarMessage != null) {
               Utils.showToastSnackbar(this@ResetPasswordActivity, snackbarMessage!!, Color.WHITE)
            }
         }
         
         override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
            pb.dismiss()
            Utils.log(TAG, "otp:  Throwable-:   $t")
            Utils.showToastSnackbar(this@ResetPasswordActivity, getString(R.string.no_internet).toString(), Color.RED)
         }
      })
   }
}
