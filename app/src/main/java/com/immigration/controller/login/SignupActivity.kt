package com.immigration.controller.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import com.immigration.controller.home.NavigationActivity
import com.immigration.controller.term_condition.TermConditionActivity
import com.mukesh.countrypicker.CountryPicker
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_signup)


        btn_signup_skip.setOnClickListener {
            startActivity(Intent(this,NavigationActivity::class.java)
                    .putExtra("session","0")
            )
        }


        signup_btn.setOnClickListener {
            startActivity(Intent(this,OTPActivity::class.java)
                    .putExtra("session_otp","0")
            )
        }


        term_condition.setOnClickListener {
           startActivity(Intent(this, TermConditionActivity::class.java))
        }

        signup_btn_login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        country_code.setOnClickListener {
            openCountryCodeDialog()
        }


    }

    fun openCountryCodeDialog() {
        val picker = CountryPicker.newInstance("Select Country")  // dialog title
        picker.setListener { name, code, dialCode, _ ->
            picker.dismiss()
            country_code.text=dialCode.toString()
          //  countryCode = dialCode
           // edtCoutryCode.setText(dialCode)
        }
        picker.show(this.getSupportFragmentManager(), "COUNTRY_PICKER")

    }
}
