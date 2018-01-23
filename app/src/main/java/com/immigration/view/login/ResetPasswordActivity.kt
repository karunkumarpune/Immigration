package com.immigration.view.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.immigration.R
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ResetPasswordActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private val TAG = ResetPasswordActivity::class.java!!.name
    private var userId:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_reset_password)

        userId = intent.getStringExtra("userId")
        APIService = ApiUtils.apiService


        reset_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        initViewSubmit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this@ResetPasswordActivity, ForgotPasswordActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()

    }

    private fun initViewSubmit() {
        btn_reset.setOnClickListener { v ->
            val pass = txt_et_new.text.toString()
            val conf_pass = txt_et_confirm.text.toString()

            if(pass.isEmpty()){
                hideSoftKeyboad(v)
                Utils.showToast(this@ResetPasswordActivity, getString(R.string.login_validation_reset), Color.WHITE)
                txt_et_new.requestFocus()
            }else if(pass.length <8){
                hideSoftKeyboad(v)
                Utils.showToast(this@ResetPasswordActivity, getString(R.string.login_validation_valid), Color.WHITE)
                txt_et_new.requestFocus()
            }else if(conf_pass.isEmpty()){
                hideSoftKeyboad(v)
                Utils.showToast(this@ResetPasswordActivity, getString(R.string.signup_validation_4), Color.WHITE)
                txt_et_confirm.requestFocus()
            }/*else if(conf_pass.length <8){
                hideSoftKeyboad(v)
                Utils.showToast(this@ResetPasswordActivity, getString(R.string.login_validation_valid), Color.WHITE)
                txt_et_confirm.requestFocus()
            }*/else if(pass !=conf_pass){
                hideSoftKeyboad(v)
                Utils.showToast(this@ResetPasswordActivity, getString(R.string.signup_validation_5), Color.WHITE)
                txt_et_confirm.requestFocus()
            } else {
                hideSoftKeyboad(v)
                initJsonOperation(pass)
            }
        }
    }
    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }
    private fun initJsonOperation(pass:String) {
        Utils.log(TAG!!, "Reset data :$pass ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        val requestBody = HashMap<String, String>()
        requestBody.put("password", pass)
        requestBody.put("userId", userId)
        APIService!!.setPassword(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                Utils.log(TAG!!, "Reset Password onResponse  code: ${response!!.raw()}")
                val status = response!!.code()

                if(response.isSuccessful){
                    val mess = response!!.body().message.toString()
                    Utils.log(TAG!!, "Reset Password onResponse  body: $mess ")
                    Utils.showToast(this@ResetPasswordActivity, mess, Color.WHITE)
                    startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }

                if (status != 200) {
                    when (status) {
                        201 -> {
                            val mess = response!!.body().message.toString()
                            Utils.showToast(this@ResetPasswordActivity, mess, Color.WHITE)
                        }
                        204 -> Utils.showToast(this@ResetPasswordActivity,errorHandler(response), Color.WHITE)
                        409 -> Utils.showToast(this@ResetPasswordActivity,errorHandler(response), Color.WHITE)
                        400 -> Utils.showToast(this@ResetPasswordActivity,errorHandler(response), Color.WHITE)
                        401 -> Utils.showToast(this@ResetPasswordActivity,errorHandler(response), Color.WHITE)
                        403 -> Utils.showToast(this@ResetPasswordActivity,errorHandler(response), Color.WHITE)
                        404 -> Utils.showToast(this@ResetPasswordActivity,errorHandler(response), Color.WHITE)
                        500 -> Utils.showToast(this@ResetPasswordActivity,resources.getString(R.string.error_status_1), Color.WHITE)
                        else -> Utils.showToast(this@ResetPasswordActivity,resources.getString(R.string.error_status_1), Color.RED)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG!!, "Reset Password Throwable : $t")
                Utils.showToast(this@ResetPasswordActivity, "Sorry!No internet available", Color.RED)
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
