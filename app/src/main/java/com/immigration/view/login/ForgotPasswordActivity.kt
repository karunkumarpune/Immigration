package com.immigration.view.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.immigration.R
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ForgotPasswordActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private val TAG = ForgotPasswordActivity::class.java!!.getName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_forgot_password)

        APIService = ApiUtils.apiService

        forgot_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btn_forgot_pass.setOnClickListener {
            val mob = forgot_et.text.toString()
            if (mob.isEmpty()) {
                Utils.showToast(this@ForgotPasswordActivity, getString(R.string.login_validation_1), Color.RED)
                forgot_et.requestFocus()
            }else if (mob.length <5) {
                Utils.showToast(this@ForgotPasswordActivity, getString(R.string.login_validation_2), Color.RED)
                forgot_et.requestFocus()
            }else {
                val view1 = this@ForgotPasswordActivity.currentFocus
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view1.windowToken, 0)
                initJsonOperation(mob)
            }
       }
    }

    private fun initJsonOperation(mob:String) {
        Utils.log(TAG!!, "Forgot data : $mob ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        val requestBody = HashMap<String, String>()
        requestBody.put("contact", mob)

        APIService!!.forgotPassword(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                Utils.log(TAG!!, "Forgot Password onResponse  code: ${response!!.raw()}")
                val status = response!!.code()

                if(response.isSuccessful){
                    val mess = response!!.body().message.toString()
                    val userId = response!!.body().result.userId
                    Utils.log(TAG!!, "Forgot Password onResponse  body: $mess   $userId")
                    Utils.showToast(this@ForgotPasswordActivity, mess, Color.YELLOW)

                    startActivity(Intent(this@ForgotPasswordActivity,OTPActivity::class.java)
                            .putExtra("session_otp","1")
                            .putExtra("user_id",userId.toString())
                    )
                }



                if (status != 200) {
                    when (status) {
                        201 -> {
                            val mess = response!!.body().message.toString()
                            Utils.showToast(this@ForgotPasswordActivity, mess, Color.YELLOW)
                        }
                        204 -> Utils.showToast(this@ForgotPasswordActivity,errorHandler(response), Color.YELLOW)
                        409 -> Utils.showToast(this@ForgotPasswordActivity,errorHandler(response), Color.YELLOW)
                        400 -> Utils.showToast(this@ForgotPasswordActivity,errorHandler(response), Color.YELLOW)
                        401 -> Utils.showToast(this@ForgotPasswordActivity,errorHandler(response), Color.YELLOW)
                        403 -> Utils.showToast(this@ForgotPasswordActivity,errorHandler(response), Color.YELLOW)
                        404 -> Utils.showToast(this@ForgotPasswordActivity,errorHandler(response), Color.YELLOW)
                        500 -> Utils.showToast(this@ForgotPasswordActivity,resources.getString(R.string.error_status_1), Color.YELLOW)
                        else -> Utils.showToast(this@ForgotPasswordActivity,resources.getString(R.string.error_status_1), Color.RED)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG!!, "Forgot Password Throwable : $t")
                Utils.showToast(this@ForgotPasswordActivity, "Sorry!No internet available", Color.RED)
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