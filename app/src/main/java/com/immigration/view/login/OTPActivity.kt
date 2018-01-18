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
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.immigration.R
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import kotlinx.android.synthetic.main.activity_otp.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class OTPActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private val TAG = OTPActivity::class.java!!.getName()


    private var session_otp:String = ""
    private var userId:String = ""
    private var otp:String = ""
    private lateinit var et_otp:EditText

    // Receiver Broadcast SMS
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
                otp = intent.getStringExtra("otp")
                Utils.log(TAG!!, "OTP data : $otp")

                if (session_otp.equals("0")) {
                    initJsonOperation("1",userId,otp)
                } else{
                    initJsonOperation("2",userId,otp)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        et_otp=findViewById<EditText>(R.id.et_otp)
        APIService = ApiUtils.apiService
        session_otp = intent.getStringExtra("session_otp")
        userId = intent.getStringExtra("user_id")
        Utils.log(TAG!!, "OTP data onResume : session_otp $session_otp , $userId")


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

        APIService = ApiUtils.apiService
        session_otp = intent.getStringExtra("session_otp")
        userId = intent.getStringExtra("user_id")
        Utils.log(TAG!!, "OTP data  : session_otp $session_otp , $userId")

        initView()
        initViewEditTextListener()
        initViewSubmitOTPListener()

        otp_txt_resend.setOnClickListener {
            initJsonResend(userId)
        }

    }

    private fun initView() {

        // Click Back Button
        otp_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun initViewSubmitOTPListener() {
        btn_submit_otp.setOnClickListener {

            val edit_otp = et_otp.text.toString().trim()
            if (edit_otp.isEmpty()) {
                Utils.showToast(this@OTPActivity, getString(R.string.otp_validation_1), Color.RED)
                et_otp.requestFocus()
            } else {
                if (session_otp.equals("0")) {
                    initJsonOperation("1",userId,edit_otp)
                } else{
                    initJsonOperation("2",userId,edit_otp)
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
                    if(et_otp.text.toString().length==4){

                        if(et_otp.text.toString() == et_otp.text.toString()){
                            if (session_otp.equals("0")) {
                                val view1 = this@OTPActivity.currentFocus
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(view1.windowToken, 0)

                                initJsonOperation("1", userId, et_otp.text.toString())
                            } else {
                                val view1 = this@OTPActivity.currentFocus
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(view1.windowToken, 0)

                                initJsonOperation("2", userId, et_otp.text.toString())
                            }
                        }
                        else{
                            val view1 = this@OTPActivity.currentFocus
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view1.windowToken, 0)
                          //  Utils.showToast(this@OTPActivity, getString(R.string.action_sms), Color.RED)
                        }
                    }else{ }
                }catch (e:Exception){
                //    Utils.showToast(this@OTPActivity, getString(R.string.action_sms), Color.RED)
                }

            }
        })
    }


    private fun initJsonOperation(type:String,userIds:String,otp:String) {
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        val requestBody = HashMap<String, String>()
        requestBody.put("type",type)
        requestBody.put("userId",userIds)
        requestBody.put("otp",otp)


        APIService!!.verifyOtp(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                Utils.log(TAG!!, "OTP onResponse  code: ${response!!.raw()}")
                val status = response!!.code()

                if(response.isSuccessful){
                    if (session_otp.equals("0")) {
                        startActivity(Intent(this@OTPActivity, EditProfileActivity::class.java)
                                .putExtra("session_edit_profile","0")
                        )
                    } else
                   startActivity(Intent(this@OTPActivity, ResetPasswordActivity::class.java))
                }

                if (status != 200) {
                    when (status) {
                        201 -> {
                            val mess = response!!.body().message.toString()
                            Utils.showToast(this@OTPActivity, mess, Color.YELLOW) }
                        204 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        409 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        400 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        401 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        403 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        404 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        500 -> Utils.showToast(this@OTPActivity,resources.getString(R.string.error_status_1), Color.YELLOW)
                        else -> Utils.showToast(this@OTPActivity,resources.getString(R.string.error_status_1), Color.RED)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG!!, "OTP Throwable : $t")
                Utils.showToast(this@OTPActivity, "Sorry!No internet available", Color.RED)
            }
        })
    }


    private fun initJsonResend(userIds:String) {
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        val requestBody = HashMap<String, String>()
        requestBody.put("userId",userIds)

        APIService!!.resendOtp(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                Utils.log(TAG!!, "OTP onResponse  code: ${response!!.raw()}")
                val status = response!!.code()

                if(response.isSuccessful){
                    if (session_otp.equals("0")) {

                    } else
                        startActivity(Intent(this@OTPActivity, ResetPasswordActivity::class.java))
                }

                if (status != 200) {
                    when (status) {
                        201 -> {
                            val mess = response!!.body().message.toString()
                            Utils.showToast(this@OTPActivity, mess, Color.YELLOW) }
                        204 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        409 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        400 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        401 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        403 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        404 -> Utils.showToast(this@OTPActivity,errorHandler(response), Color.YELLOW)
                        500 -> Utils.showToast(this@OTPActivity,resources.getString(R.string.error_status_1), Color.YELLOW)
                        else -> Utils.showToast(this@OTPActivity,resources.getString(R.string.error_status_1), Color.RED)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG!!, "OTP Throwable : $t")
                Utils.showToast(this@OTPActivity, "Sorry!No internet available", Color.RED)
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
