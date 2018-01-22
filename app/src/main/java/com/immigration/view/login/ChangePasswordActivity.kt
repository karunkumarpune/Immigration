package com.immigration.view.login

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.immigration.R
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_change_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ChangePasswordActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private val TAG = ChangePasswordActivity::class.java!!.getName()
    private var userAccessToken: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_change_password)
        loginPreference = LoginPrefences.getInstance()
        APIService = ApiUtils.apiService
        userAccessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(this))

        change_pass_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        initViewSubmit()
    }


    private fun initViewSubmit() {
        btn_change_submit.setOnClickListener { v ->
            val old_pass = txt_et_old_pass.text.toString().trim()
            val pass = txt_et_new.text.toString().trim()
            val conf_pass = txt_et_confirm.text.toString().trim()

            if(old_pass.isEmpty()){
                hideSoftKeyboad(v)
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.edit_profile_validation_5), Color.RED)
                txt_et_old_pass.requestFocus()
            }else if(old_pass.length <8){
                hideSoftKeyboad(v)
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.login_validation_valid), Color.RED)
                txt_et_old_pass.requestFocus()
            } else if(pass.isEmpty()){
                hideSoftKeyboad(v)
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.login_validation_3), Color.RED)
                txt_et_new.requestFocus()
            } else if(pass.length <8){
                hideSoftKeyboad(v)
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.login_validation_valid), Color.RED)
                txt_et_new.requestFocus()
            }else if(conf_pass.isEmpty()){
                hideSoftKeyboad(v)
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.signup_validation_4), Color.RED)
                txt_et_confirm.requestFocus()
            }else if(conf_pass.length <8){
                hideSoftKeyboad(v)
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.login_validation_valid), Color.RED)
                txt_et_confirm.requestFocus()
            }else if(pass !=conf_pass){
                hideSoftKeyboad(v)
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.signup_validation_5), Color.RED)
                txt_et_confirm.requestFocus()
            } else {
                hideSoftKeyboad(v)
                initJsonOperation(old_pass,conf_pass)
            }
        }
    }
    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }
    private fun initJsonOperation(oldPass:String,pass:String) {
        Utils.log(TAG!!, "Reset data :$pass ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        val requestBody = HashMap<String, String>()
        requestBody.put("oldPassword",oldPass)
        requestBody.put("password",pass)

        APIService!!.changePasswords(this!!.userAccessToken!!,requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                Utils.log(TAG!!, "Change Pass onResponse  code: ${response!!.raw()}")
                val status = response!!.code()

                if(status==200){
                    val mess = response!!.body().message.toString()
                    Utils.log(TAG!!, "Change Pass onResponse: $mess")

                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

                if (status != 200) {
                    when (status) {
                        201 -> {
                            val mess = response!!.body().message.toString()
                            Utils.showToast(this@ChangePasswordActivity, mess, Color.YELLOW)
                        }
                        204 -> Utils.showToast(this@ChangePasswordActivity,errorHandler(response), Color.YELLOW)
                        409 -> Utils.showToast(this@ChangePasswordActivity,errorHandler(response), Color.YELLOW)
                        400 -> Utils.showToast(this@ChangePasswordActivity,errorHandler(response), Color.YELLOW)
                        401 -> Utils.showToast(this@ChangePasswordActivity,errorHandler(response), Color.YELLOW)
                        403 -> Utils.showToast(this@ChangePasswordActivity,errorHandler(response), Color.YELLOW)
                        404 -> Utils.showToast(this@ChangePasswordActivity,errorHandler(response), Color.YELLOW)
                        500 -> Utils.showToast(this@ChangePasswordActivity,resources.getString(R.string.error_status_1), Color.YELLOW)
                        else -> Utils.showToast(this@ChangePasswordActivity,resources.getString(R.string.error_status_1), Color.RED)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG!!, "Change Pass Throwable : $t")
                Utils.showToast(this@ChangePasswordActivity, "Sorry!No internet available", Color.RED)
            }
        })
    }


    private fun errorHandler(response: Response<ResponseModel>?):String{
        return try {
            val jObjError = JSONObject(response!!.errorBody().string())
            jObjError.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }
}