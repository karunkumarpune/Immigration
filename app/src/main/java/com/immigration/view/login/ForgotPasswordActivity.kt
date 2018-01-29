package com.immigration.view.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.appdata.Constant.countryCodeValues
import com.immigration.appdata.Constant.key_contact
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ForgotPasswordActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private val TAG = ForgotPasswordActivity::class.java.name
   
    private var userAccessToken: String? = null
    private var loginPreference: LoginPrefences? = null
    private var snackbarMessage: String? = null
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.moveLeftToRight(this)
        setContentView(R.layout.activity_forgot_password)
    
        loginPreference = LoginPrefences.getInstance()
        APIService = ApiUtils.apiService
        userAccessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(applicationContext))
    
    
        forgot_btn_click_back.setOnClickListener {
            onBackPressed()
            Utils.moveLeftToRight(this)
        }

        btn_forgot_pass.setOnClickListener {v->
            val mob = forgot_et.text.toString()
            if (mob.isEmpty()) {
                Utils.hideSoftKeyboad(applicationContext, v)
                Utils.showToastSnackbar(this@ForgotPasswordActivity, getString(R.string.login_validation_1), Color.WHITE)
                forgot_et.requestFocus()
            }else if (mob.length <5) {
                Utils.hideSoftKeyboad(applicationContext, v)
                Utils.showToastSnackbar(this@ForgotPasswordActivity, getString(R.string.login_validation_2), Color.WHITE)
                forgot_et.requestFocus()
            }else {
                Utils.hideSoftKeyboad(applicationContext, v)
                initJsonOperation(mob)
            }
       }
    }

    private fun initJsonOperation(mob:String) {
        Utils.log(TAG, "Forgot data : $mob ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        val requestBody = HashMap<String, String>()
        requestBody.put(key_contact, mob)

        APIService!!.forgotPassword(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                val status = response!!.code()
                when (status) {
                    200 -> {
                        Utils.showToast(applicationContext, response.body()!!.message.toString())
                        val mess = response.body()!!.message.toString()
                        val userId = response.body()!!.result!!.userId?:""
                        countryCodeValues= response.body()!!.result!!.countryCode.toString()
                        Utils.showToastSnackbar(this@ForgotPasswordActivity, mess, Color.WHITE)
    
                        startActivity(Intent(this@ForgotPasswordActivity, OTPActivity::class.java)
                         .putExtra("session_otp", "1")
                         .putExtra("user_id", userId.toString())
                         .putExtra("contact", mob)
                        ) }
                    401 -> {
                        Utils.showToast(applicationContext, Utils.errorHandler(response))
                        Utils.invalidToken(this@ForgotPasswordActivity, loginPreference, LoginActivity())
                    }
                }
                snackbarMessage = Utils.responseStatus(this@ForgotPasswordActivity, status, response)
                if (snackbarMessage != null) {
                    Utils.showToastSnackbar(this@ForgotPasswordActivity, snackbarMessage!!, Color.WHITE)
                }
            }
    
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG, "otp:  Throwable-:   $t")
                Utils.showToastSnackbar(this@ForgotPasswordActivity, getString(R.string.no_internet).toString(), Color.RED)
            }
        })
    }
}