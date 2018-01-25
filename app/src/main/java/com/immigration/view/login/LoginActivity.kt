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
import android.widget.Toast
import com.immigration.R
import com.immigration.appdata.Constant
import com.immigration.appdata.Constant.key_contact
import com.immigration.appdata.Constant.key_password
import com.immigration.appdata.Constant.validationMobLenth
import com.immigration.appdata.Constant.validationPassLenth
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.utils.Utils.hideSoftKeyboad
import com.immigration.view.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_login.*
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
   private val TAG = LoginActivity::class.java.name
   private var snackbarMessage: String? = null
   private var tagMessage: String? = null
   
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
      
      initViewSubmit()
      // click skip
      btn_login_skip.setOnClickListener {
         startActivity(Intent(this, NavigationActivity::class.java)
          .putExtra("session", "0"))
      }
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
         when {
            mob.isEmpty() -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.login_validation_1)
            }
            mob.length < validationMobLenth -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.login_validation_2)
            }
            pass.isEmpty() -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.login_validation_3)
            }
            pass.length < validationPassLenth -> {
               hideSoftKeyboad(applicationContext, v)
               snackbarMessage = getString(R.string.login_validation_valid)
            }
            else -> {
               hideSoftKeyboad(applicationContext, v)
               initJsonOperation(mob, pass)
            }
         }
         if (snackbarMessage != null) {
            Utils.showToastSnackbar(this@LoginActivity, snackbarMessage!!, Color.WHITE)
         }
      }
   }
   
   
   private fun initJsonOperation(mob: String, pass: String) {
      tagMessage = "login data : mob: $mob, pass: $pass"
      pb = CustomProgressBar(this)
      pb.setCancelable(false)
      pb.show()
      val requestBody = HashMap<String, String>()
      requestBody.put(key_contact, mob)
      requestBody.put(key_password, pass)
      
      APIService!!.login(requestBody)
       .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
          override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
             pb.dismiss()
             val status = response!!.code()
             when (status) {
                200 -> {
                   Utils.showToast(applicationContext, response.body().message.toString())
                   val accessToken = response.body().result.accessToken
                   val userId = response.body().result.userId
                   var email = response.body().result.email
                   var countryCode = response.body().result.countryCode
                   var contact = response.body().result.contact
                   var firstName = response.body().result.firstName
                   var lastName = response.body().result.lastName
                   var profilePic = response.body().result.profilePic
                   
                   Constant.accessTokenValues = accessToken
                   Constant.countryCodeValues = countryCode
                   Constant.contactValues = contact
                   
                   
                   if (profilePic == null) {
                      profilePic = Constant.DefaultImage
                   } else {
                      profilePic = Constant.BASE_URL_Image + profilePic
                   }
                   if (firstName == null) {
                      firstName = ""
                   }
                   if (lastName == null) {
                      lastName = ""
                   }
                   if (email == null) {
                      email = ""
                   }
                   if (countryCode == null) {
                      countryCode = ""
                   }
                   if (contact == null) {
                      contact = ""
                   }
                   val isProfileCreated = response.body().result.isProfileCreated
                   val isVerified = response.body().result.isVerified
                   
                   if (isVerified == "0") {
                      startActivity(Intent(this@LoginActivity, OTPActivity::class.java)
                       .putExtra("session_otp", "0")
                       .putExtra("user_id", userId.toString())
                       .putExtra("contact", mob))
                   } else if (isProfileCreated == "0") {
                      startActivity(Intent(this@LoginActivity, EditProfileActivity::class.java)
                       .putExtra("session_edit_profile", "0")
                       .putExtra("otp_email", email))
                      finish()
                   } else {
                      Utils.log(TAG, "Login onResponse  isSuccessful:$userId, $email ,$countryCode, $contact  $firstName ,$lastName , $accessToken , $profilePic ")
                      LoginPrefences.getInstance().addData(this@LoginActivity,
                       accessToken, userId.toString(), countryCode, contact, pass, email, firstName, lastName, profilePic)
                      startActivity(Intent(this@LoginActivity, NavigationActivity::class.java)
                       .putExtra("session", "1")
                       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                       .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                      finish()
                   }
                }
             }
             snackbarMessage = Utils.responseStatus(this@LoginActivity, status, response)
             if (snackbarMessage != null) {
                Utils.showToastSnackbar(this@LoginActivity, snackbarMessage!!, Color.WHITE)
             }
          }
          
          override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
             pb.dismiss()
             tagMessage = "login Throwable-:   $t"
             Utils.showToastSnackbar(this@LoginActivity, getString(R.string.no_internet).toString(), Color.RED)
          }
       })
      Utils.log(TAG, tagMessage!!)
   }
   
   //OTP ReadPermissions
   private fun ReadPermissions() {
      // if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
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
