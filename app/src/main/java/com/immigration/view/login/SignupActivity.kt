package com.immigration.view.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.immigration.R
import com.immigration.appdata.Constant.countryCodeValues
import com.immigration.appdata.Constant.key_contact
import com.immigration.appdata.Constant.key_countryCode
import com.immigration.appdata.Constant.key_email
import com.immigration.appdata.Constant.key_password
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.view.home.NavigationActivity
import com.immigration.view.term_condition.TermConditionActivity
import com.mukesh.countrypicker.CountryPicker
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class SignupActivity : AppCompatActivity() {

    private var countryCodeS: String? = null
    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private val TAG = SignupActivity::class.java!!.getName()

    companion object {
        var passwords_signup: String = ""
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_signup)

        countryCodeS = "+1"
        APIService = ApiUtils.apiService


        //skip condition
        btn_signup_skip.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session", "0"))
        }


        initViewSubmit()

        //Term condition
        val s = getString(R.string.term_conditionss)
        term_condition.text = Html.fromHtml(s)//,Html.FROM_HTML_MODE_LEGACY)
        term_condition.setOnClickListener {
            startActivity(Intent(this, TermConditionActivity::class.java))
        }

        //again login
        signup_btn_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        //click country_code
        country_code.setOnClickListener { openCountryCodeDialog() }
    }

    private fun openCountryCodeDialog() {
        val picker = CountryPicker.newInstance("Select Country")  // dialog title
        picker.setListener { name, code, dialCode, _ ->
            picker.dismiss()
            country_code.text = dialCode.toString()
            countryCodeS = dialCode.toString()
            countryCodeValues=dialCode.toString()
        }
        picker.show(this.supportFragmentManager, "COUNTRY_PICKER")

    }

    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }

    private fun initViewSubmit() {

        signup_btn.setOnClickListener { v ->

            val mobile = txt_signup_mob.text.toString()
            val email = txt_signup_email.text.toString()
            val pass = txt_signup_pass.text.toString()
            val conf_pass = txt_signup_conf_pass.text.toString()

            if (mobile.isEmpty()) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_1), Color.WHITE)
                txt_signup_mob.requestFocus()
            } else if (mobile.length < 5) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_2), Color.WHITE)
                txt_signup_mob.requestFocus()
            } else if (email.isEmpty()) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_email), Color.WHITE)
                txt_signup_email.requestFocus()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_2), Color.WHITE)
                txt_signup_email.requestFocus()
            } else if (pass.isEmpty()) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_3), Color.WHITE)
                txt_signup_pass.requestFocus()
            } else if (pass.length < 8) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_valid), Color.WHITE)
                txt_signup_pass.requestFocus()
            } else if (conf_pass.isEmpty()) {
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_4), Color.WHITE)
                txt_signup_conf_pass.requestFocus()
                hideSoftKeyboad(v)
            } /*else if (conf_pass.length <8) {
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_valid), Color.WHITE)
                txt_signup_conf_pass.requestFocus()
                hideSoftKeyboad(v)
            }*/ else if (pass != conf_pass) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_5), Color.WHITE)
                txt_signup_conf_pass.requestFocus()
            } else if (!txt_signup_check.isChecked) {
                hideSoftKeyboad(v)
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_6), Color.WHITE)
            } else {
                hideSoftKeyboad(v)
                initJsonOperation(countryCodeS.toString(), mobile, email, conf_pass)

            }
        }

    }

    private fun initJsonOperation(code: String, mob: String, email: String, pass: String) {
        Utils.log(TAG, "signup data : $code,$mob,$email,$pass ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        val requestBody = HashMap<String, String>()
        requestBody.put(key_email, email)
        requestBody.put(key_contact, mob)
        requestBody.put(key_countryCode, code)
        requestBody.put(key_password, pass)
        passwords_signup = pass
        APIService!!.getUser(requestBody).enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                Utils.log(TAG, "signup onResponse  code: ${response!!.raw()}")
                val status = response!!.code()

                if (status != 200) {
                    when (status) {
                        201 -> {
                            val mess = response!!.body().message.toString()
                            val userId = response!!.body().result.userId
                            Utils.log(TAG, "signup onResponse  body: $mess   $userId")

                            startActivity(Intent(this@SignupActivity, OTPActivity::class.java)
                                    .putExtra("session_otp", "0")
                                    .putExtra("user_id", userId.toString())
                                    .putExtra("contact", mob)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()

                        }
                        204 -> Utils.showToast(this@SignupActivity, errorHandler(response), Color.WHITE)
                        409 -> Utils.showToast(this@SignupActivity, errorHandler(response), Color.WHITE)
                        400 -> Utils.showToast(this@SignupActivity, errorHandler(response), Color.WHITE)
                        401 -> Utils.showToast(this@SignupActivity, errorHandler(response), Color.WHITE)
                        403 -> Utils.showToast(this@SignupActivity, errorHandler(response), Color.WHITE)
                        404 -> Utils.showToast(this@SignupActivity, errorHandler(response), Color.WHITE)
                        500 -> Utils.showToast(this@SignupActivity, resources.getString(R.string.error_status_1), Color.WHITE)
                        else -> Utils.showToast(this@SignupActivity, resources.getString(R.string.error_status_1), Color.RED)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG, "signup Throwable : $t")
                Utils.showToast(this@SignupActivity, "Sorry!No internet available", Color.RED)
            }
        })
    }


    private fun errorHandler(response: Response<ResponseModel>?): String {
        return try {
            val jObjError = JSONObject(response!!.errorBody().string())
            jObjError.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }
}