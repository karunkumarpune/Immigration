package com.immigration.view.login

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.appdata.Constant.key_oldPassword
import com.immigration.appdata.Constant.key_password
import com.immigration.appdata.Constant.validationPassLenth
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.utils.Utils.errorHandler
import com.immigration.utils.Utils.hideSoftKeyboad
import com.immigration.utils.Utils.invalidToken
import com.immigration.utils.Utils.moveLeftToRight
import com.immigration.utils.Utils.responseStatus
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ChangePasswordActivity : AppCompatActivity() {
   private var APIService: APIService? = null
   private lateinit var pb: CustomProgressBar
   private val TAG = ChangePasswordActivity::class.java.name
  
   private var userAccessToken: String? = null
   private var loginPreference: LoginPrefences? = null
   private var snackbarMessage: String? = null
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      moveLeftToRight(this)
      setContentView(R.layout.activity_change_password)
      
      loginPreference = LoginPrefences.getInstance()
      APIService = ApiUtils.apiService
      userAccessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(applicationContext))
      
      change_pass_btn_click_back.setOnClickListener {
         onBackPressed()
         moveLeftToRight(this)
      }
      
      initViewSubmit()
   }
   
   override fun onBackPressed() {
      super.onBackPressed()
      moveLeftToRight(this)
   }
   
   private fun initViewSubmit() {
      btn_change_submit.setOnClickListener { v ->
         val old_pass = txt_et_old_pass.text.toString().trim()
         val pass = txt_et_new.text.toString().trim()
         val conf_pass = txt_et_confirm.text.toString().trim()
         when {
            old_pass.isEmpty() -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.edit_profile_validation_5)
               txt_et_old_pass.requestFocus()
            }
            old_pass.length < validationPassLenth -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.login_validation_valid)
               txt_et_old_pass.requestFocus()
            }
            pass.isEmpty() -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.login_validation_reset)
               txt_et_new.requestFocus()
            }
            pass.length < validationPassLenth -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.login_validation_valid)
               txt_et_new.requestFocus()
            }
            conf_pass.isEmpty() -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.signup_validation_4)
               txt_et_confirm.requestFocus()
            }
            pass != conf_pass -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.signup_validation_5)
               txt_et_confirm.requestFocus()
            }
            else -> {
               hideSoftKeyboad(applicationContext, v)
               initJsonOperation(old_pass, conf_pass)
            }
         }
         if (snackbarMessage != null) {
            Utils.showToastSnackbar(this@ChangePasswordActivity, snackbarMessage!!, Color.WHITE)
         }
      }
   }
   
   private fun initJsonOperation(oldPass: String, pass: String) {
      Utils.log(TAG, "change password  oldPass: $oldPass ,newPass $pass")
      pb = CustomProgressBar(this)
      pb.setCancelable(false)
      pb.show()
      val requestBody = HashMap<String, String>()
      requestBody.put(key_oldPassword, oldPass)
      requestBody.put(key_password, pass)
      
      APIService!!.changePasswords(this.userAccessToken!!, requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
         override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
            pb.dismiss()
            val status = response!!.code()
            when (status) {
               200 -> {
                  Utils.showToast(applicationContext, response.body()!!.message.toString())
                  onBackPressed()
                  moveLeftToRight(this@ChangePasswordActivity)
               }
               401 -> {
                  Utils.showToast(applicationContext, errorHandler(response))
                  invalidToken(this@ChangePasswordActivity, loginPreference, LoginActivity())
               }
            }
            snackbarMessage = responseStatus(this@ChangePasswordActivity, status, response)
            if (snackbarMessage != null) {
               Utils.showToastSnackbar(this@ChangePasswordActivity, snackbarMessage!!, Color.WHITE)
            }
         }
         
         override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
            pb.dismiss()
            Utils.log(TAG, "otp:  Throwable-:   $t")
            Utils.showToastSnackbar(this@ChangePasswordActivity, getString(R.string.no_internet).toString(), Color.RED)
         }
      })
   }
}