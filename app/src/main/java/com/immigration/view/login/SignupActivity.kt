package com.immigration.view.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.immigration.R
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.view.home.NavigationActivity
import com.immigration.view.term_condition.TermConditionActivity
import com.mukesh.countrypicker.CountryPicker
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private var countryCode: String? = null


    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private val TAG = SignupActivity::class.java!!.getName()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_signup)

        countryCode="+1"
        loginPreference = LoginPrefences.getInstance()
        APIService = ApiUtils.apiService


        //skip condition
        btn_signup_skip.setOnClickListener {
            startActivity(Intent(this,NavigationActivity::class.java)
                    .putExtra("session","0")) }


        initViewSubmit()

        //Term condition
        val s = getString(R.string.term_conditionss)
        term_condition.text= Html.fromHtml(s)//,Html.FROM_HTML_MODE_LEGACY)
        term_condition.setOnClickListener {
           startActivity(Intent(this, TermConditionActivity::class.java)) }

        //again login
        signup_btn_login.setOnClickListener { startActivity(Intent(this,LoginActivity::class.java))
            finish() }

        //click country_code
        country_code.setOnClickListener { openCountryCodeDialog() }
    }

    private fun openCountryCodeDialog() {
        val picker = CountryPicker.newInstance("Select Country")  // dialog title
        picker.setListener { name, code, dialCode, _ ->
            picker.dismiss()
            country_code.text=dialCode.toString()
            countryCode = dialCode.toString()
        }
        picker.show(this.supportFragmentManager, "COUNTRY_PICKER")

    }
    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }
    private fun initViewSubmit() {

        signup_btn.setOnClickListener { v ->

            val mobile=txt_signup_mob.text.toString()
            val email=txt_signup_email.text.toString()
            val pass=txt_signup_pass.text.toString()
            val conf_pass=txt_signup_conf_pass.text.toString()

            if(mobile.isEmpty()){
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_1), Color.RED)
                txt_signup_mob.requestFocus()
                hideSoftKeyboad(v)
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_2), Color.RED)
                txt_signup_email.requestFocus()
                hideSoftKeyboad(v)
            }else if(pass.isEmpty()){
                Utils.showToast(this@SignupActivity, getString(R.string.login_validation_3), Color.RED)
                txt_signup_pass.requestFocus()
                hideSoftKeyboad(v)
            }else if(conf_pass.isEmpty()){
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_4), Color.RED)
                txt_signup_conf_pass.requestFocus()
                hideSoftKeyboad(v)
            }else if(pass !=conf_pass){
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_5), Color.RED)
                txt_signup_conf_pass.requestFocus()
                hideSoftKeyboad(v)
            }else if(!txt_signup_check.isChecked){
                Utils.showToast(this@SignupActivity, getString(R.string.signup_validation_6), Color.RED)
            }else{
                initJsonOperation(countryCode!!,mobile,email,conf_pass)
            }
        }

    }

    private fun initJsonOperation(code:String,mob:String,email:String,pass:String) {
        Utils.log(TAG!!,"signup data : $code,$mob,$email,$pass ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()
        Handler().postDelayed({pb.dismiss()
            LoginPrefences.getInstance().addData(this@SignupActivity, mob, pass,email,"","")
            startActivity(Intent(this,OTPActivity::class.java)
                    .putExtra("session_otp","0")
            )
        },2000)

    }
}
