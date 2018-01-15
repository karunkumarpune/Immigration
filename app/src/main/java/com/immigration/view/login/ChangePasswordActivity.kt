package com.immigration.view.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.immigration.R
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.restservices.APIService
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.view.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private val TAG = ChangePasswordActivity::class.java!!.getName()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_change_password)

        change_pass_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        initViewSubmit()
    }


    private fun initViewSubmit() {
        btn_submit.setOnClickListener { v ->
            val old_pass = txt_et_old_pass.text.toString()
            val pass = txt_et_new.text.toString()
            val conf_pass = txt_et_confirm.text.toString()

            if(old_pass.isEmpty()){
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.edit_profile_validation_5), Color.RED)
                txt_et_old_pass.requestFocus()
                hideSoftKeyboad(v)
            } else if(pass.isEmpty()){
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.login_validation_3), Color.RED)
                txt_et_new.requestFocus()
                hideSoftKeyboad(v)
            }else if(conf_pass.isEmpty()){
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.signup_validation_4), Color.RED)
                txt_et_confirm.requestFocus()
                hideSoftKeyboad(v)
            }else if(pass !=conf_pass){
                Utils.showToast(this@ChangePasswordActivity, getString(R.string.signup_validation_5), Color.RED)
                txt_et_confirm.requestFocus()
                hideSoftKeyboad(v)
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

        Handler().postDelayed({pb.dismiss()
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session","1"))


        },2000)


    }
}
