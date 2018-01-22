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
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.view.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class LoginActivity : AppCompatActivity() {

    //OTP
    companion object {
        private val permissionsRequired = arrayOf(Manifest.permission.READ_SMS)
       // private val permissionsRequired = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS)
        private val REQUEST_PERMISSION_SETTING = 101
        private val PERMISSION_CALLBACK_CONSTANT = 100
    }

    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private val TAG = LoginActivity::class.java!!.getName()



    override fun onStart() {
        super.onStart()
        loginPreference = LoginPrefences.getInstance();
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        loginPreference = LoginPrefences.getInstance()
        APIService = ApiUtils.apiService

        if (!loginPreference!!.hasValue(loginPreference!!.getLoginPreferences(this@LoginActivity))) {
            startActivity(Intent(this@LoginActivity, NavigationActivity::class.java)
                    .putExtra("session", "1")
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                 finish()
        }

        setContentView(R.layout.activity_login)
        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
        ReadPermissions()


        Log.d("TAG_TEST", "CHECK: " + (!loginPreference!!.hasValue(loginPreference!!.getLoginPreferences(this@LoginActivity))).toString())

        //  val userPass = loginPreference!!.getPRE_Pass(LoginPrefences.getInstance().getLoginPreferences(this))
        // val userMOB = loginPreference!!.getPRE_Mobile(LoginPrefences.getInstance().getLoginPreferences(this))

        // Log.d("TAG_TEST", "DATA TEST: LOGIN: $userPass $userMOB")



        initViewSubmit()
        // click skip
        btn_login_skip.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session", "0")) }

        //Click Signup
        login_btn_signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        //Forgot Password
        btn_forgot_pass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun initViewSubmit() {
        btn_login.setOnClickListener { v ->
            val mob = login_et_mobile.text.toString()
            val pass = login_et_pass.text.toString()

            if (mob.isEmpty()) {
                hideSoftKeyboad(v)
                Utils.showToast(this@LoginActivity, getString(R.string.login_validation_1), Color.RED)
                login_et_mobile.requestFocus()
            } else if (mob.length <5) {
                hideSoftKeyboad(v)
                Utils.showToast(this@LoginActivity, getString(R.string.login_validation_2), Color.RED)
                login_et_mobile.requestFocus()
            } else if (pass.isEmpty()) {
                hideSoftKeyboad(v)
                Utils.showToast(this@LoginActivity, getString(R.string.login_validation_3), Color.RED)
                login_et_pass.requestFocus()
            }else if (pass.length <8) {
                hideSoftKeyboad(v)
                Utils.showToast(this@LoginActivity, getString(R.string.login_validation_valid), Color.RED)
                login_et_pass.requestFocus()
            } else {
                hideSoftKeyboad(v)
                initJsonOperation(mob,pass)
            }
        }
    }
    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }

    private fun initJsonOperation(mob:String,pass:String) {
        Utils.log(TAG!!, "Login data : $mob,$pass ")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()


        val requestBody = HashMap<String, String>()
        requestBody.put("contact",mob)
        requestBody.put("password",pass)

        APIService!!.login(requestBody)
                .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
                    override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                        pb.dismiss()
                        Utils.log(TAG!!, "Login onResponse  code: ${response!!.raw()}")
                        val status = response!!.code()

                        if(response.isSuccessful){
                            var img = ""

                            val accessToken=response.body().result.accessToken
                            val userId=response.body().result.userId
                            val email=response.body().result.email
                            val countryCode=response.body().result.countryCode
                            val contact=response.body().result.contact
                            val firstName=response.body().result.firstName
                            val lastName=response.body().result.lastName


                            var profilePic = response.body().result.profilePic

                            if (profilePic == null) {
                                img = "https://oncopedia.pro/w/images/thumb/a/a5/UserPlaceholder.png/250px-UserPlaceholder.png"
                            } else {
                                img ="http://worklime.com/immigration/images/"+ profilePic
                            }

                            Utils.log(TAG!!, "Login onResponse  isSuccessful:$userId, $email ,$countryCode, $contact  $firstName ,$lastName , $accessToken , $profilePic ")
                            LoginPrefences.getInstance().addData(this@LoginActivity,
                                    accessToken,
                                    userId.toString(),
                                    countryCode,
                                    contact,pass,
                                    email,firstName,lastName,img)
                            startActivity(Intent(this@LoginActivity, NavigationActivity::class.java)
                                    .putExtra("session", "1")
                            )
                        }

                        if (status != 200) {
                            when (status) {
                                201 -> {
                                    val mess = response!!.body().message.toString()
                                    Utils.showToast(this@LoginActivity, mess, Color.YELLOW) }
                                204 -> Utils.showToast(this@LoginActivity,errorHandler(response), Color.YELLOW)
                                409 -> Utils.showToast(this@LoginActivity,errorHandler(response), Color.YELLOW)
                                400 -> Utils.showToast(this@LoginActivity,errorHandler(response), Color.YELLOW)
                                401 -> Utils.showToast(this@LoginActivity,errorHandler(response), Color.YELLOW)
                                403 -> Utils.showToast(this@LoginActivity,errorHandler(response), Color.YELLOW)
                                404 -> Utils.showToast(this@LoginActivity,errorHandler(response), Color.YELLOW)
                                500 -> Utils.showToast(this@LoginActivity,resources.getString(R.string.error_status_1), Color.YELLOW)
                                else -> Utils.showToast(this@LoginActivity,resources.getString(R.string.error_status_1), Color.RED)
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                        pb.dismiss()
                        Utils.log(TAG!!, "Login Throwable : $t")
                        Utils.showToast(this@LoginActivity, "Sorry!No internet available", Color.RED)
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


    //OTP ReadPermissions
    private fun ReadPermissions() {
       // if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED ) {
          //  if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setTitle("Need Permissions")
                builder.setMessage("This app needs SMS permissions.")
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
                builder.setTitle("Need Permissions")
                builder.setMessage("This app needs SMS permissions.")
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
