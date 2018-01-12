package com.immigration.view.login

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.immigration.R
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.utils.Utils
import com.immigration.view.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {


    //OTP
    companion object {

        private val permissionsRequired = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS)

        private val REQUEST_PERMISSION_SETTING = 101

        private val PERMISSION_CALLBACK_CONSTANT = 100
    }
    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false



    private var loginPreference: LoginPrefences? = null


    override fun onStart() {
        super.onStart()
        loginPreference = LoginPrefences.getInstance();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_login)

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
        ReadPermissions()



        loginPreference = LoginPrefences.getInstance()
        if (!loginPreference!!.hasValue(loginPreference!!.getLoginPreferences(this@LoginActivity))) {
            startActivity(Intent(this@LoginActivity, NavigationActivity::class.java)
                    .putExtra("session", "1")
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }

        Log.d("TAG_TEST", "CHECK: " + (!loginPreference!!.hasValue(loginPreference!!.getLoginPreferences(this@LoginActivity))).toString())

        //  val userPass = loginPreference!!.getPRE_Pass(LoginPrefences.getInstance().getLoginPreferences(this))
        // val userMOB = loginPreference!!.getPRE_Mobile(LoginPrefences.getInstance().getLoginPreferences(this))

        // Log.d("TAG_TEST", "DATA TEST: LOGIN: $userPass $userMOB")


        btn_login_skip.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session", "0")
            )
        }

        btn_login.setOnClickListener { v ->
            val mob = login_et_mobile.text.toString()
            val pass = login_et_pass.text.toString()

            if (mob.isEmpty()) {
                Utils.showToast(this@LoginActivity, getString(R.string.login_validation_1), Color.RED)
                login_et_mobile.requestFocus()
                hideSoftKeyboad(v)
            } else if (mob.length != 10) {
                Utils.showToast(this@LoginActivity, getString(R.string.login_validation_2), Color.RED)
                login_et_mobile.requestFocus()
                hideSoftKeyboad(v)
            } else if (pass.isEmpty()) {
                Utils.showToast(this@LoginActivity, getString(R.string.login_validation_3), Color.RED)
                login_et_pass.requestFocus()
            } else {
                hideSoftKeyboad(v)

                LoginPrefences.getInstance().addData(this@LoginActivity, mob, pass)
                startActivity(Intent(this@LoginActivity, NavigationActivity::class.java)
                        .putExtra("session", "1")
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        finish()
            }
        }

        login_btn_signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        btn_forgot_pass.setOnClickListener {
            // Utils.showToast(this,"hiii",Color.CYAN)
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }


    }

    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }


    //OTP
    private fun ReadPermissions() {
        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Storage permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(this@LoginActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else if (permissionStatus!!.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Storage permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    sentToSettings = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(baseContext, "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show()
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
            val editor = permissionStatus!!.edit()
            editor.putBoolean(permissionsRequired[0], true)
            editor.commit()
        } else {
            //You already have the permission, just go ahead.
            // proceedAfterPermission();
        }
    }
}
