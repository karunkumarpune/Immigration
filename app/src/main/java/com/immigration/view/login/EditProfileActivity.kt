package com.immigration.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.immigration.R
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.utils.croupimage.CropPicker
import com.immigration.utils.croupimage.FileUtil
import com.immigration.view.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private var cropPicker:CropPicker?=null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private val TAG = EditProfileActivity::class.java!!.getName()

    private var session_edit_profile: String = ""


    private var userFirstName: String? = null
    private var userLastName: String? = null
    private var userEmail: String? = null
    private var userMobile: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_edit_profile)
        loginPreference = LoginPrefences.getInstance()
        APIService = ApiUtils.apiService

        session_edit_profile = intent.getStringExtra("session_edit_profile")

        userFirstName = loginPreference!!.getFName(LoginPrefences.getInstance().getLoginPreferences(this))
        userLastName = loginPreference!!.getLName(LoginPrefences.getInstance().getLoginPreferences(this))
        userEmail = loginPreference!!.getEmail(LoginPrefences.getInstance().getLoginPreferences(this))
        userMobile = loginPreference!!.getMobile(LoginPrefences.getInstance().getLoginPreferences(this))


        if (session_edit_profile.equals("0")) {

            txt_profile.text = "Create Profile"
            profile_image.setImageResource(R.drawable.ic_person)

            et_profile_mobile.setText(userMobile.toString())
            et_profile_email.setText(userEmail.toString())
            et_profile_mobile.isFocusable=false
            et_profile_email.isFocusable=false

            initViewSubmitPost(userMobile.toString(),userEmail.toString())
            initViewCropPicPost()
        } else {
            txt_profile.text = "Edit Profile"
            edit_btn_click_back.visibility = View.VISIBLE
            profile_image.setImageResource(R.drawable.logo)


            et_profile_first.setText(userFirstName.toString())
            et_profile_last.setText(userLastName.toString())
            et_profile_mobile.setText(userMobile.toString())
            et_profile_email.setText(userEmail.toString())

            initViewCropUpdate()
            initViewSubmitUpdate()
        }



        edit_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun initViewCropPicPost() {
        iv_camera.setOnClickListener {
            cropPicker = CropPicker(this)

            Utils.log(TAG!!, "Create Profile Post data : ${FileUtil.getInstance(this).createImageUri()}")


        }
    }
    private fun initViewCropUpdate() {
        iv_camera.setOnClickListener {
            cropPicker = CropPicker(this)
        }
    }
    //Edit Profile Choice Pic
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropPicker.CHOOSE_PHOTO_INTENT) {
                if (data != null && data.data != null) {
                    cropPicker!!.handleGalleryResult(data)
                } else {
                    cropPicker!!.handleCameraResult(Uri.fromFile(CropPicker.output))
                }
            } else if (requestCode == CropPicker.SELECTED_IMG_CROP) {
                val imgFile = File(CropPicker.getPath(this@EditProfileActivity, cropPicker!!.cropImageUrl!!)!!)
                Glide.with(this@EditProfileActivity).load(imgFile).asBitmap().into(profile_image)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CropPicker.SELECT_PICTURE_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                cropPicker!!.showImagePickerDialog(this@EditProfileActivity)
        }
    }
  //End Choice Pic

    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }

    private fun initViewSubmitPost(mob_:String,email_:String) {

        edit_btn_edit.setOnClickListener { v ->

            val first_name_ = et_profile_first.text.toString()
            val last_name_ = et_profile_last.text.toString()

            if (first_name_.isEmpty()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_1), Color.RED)
                et_profile_first.requestFocus()
                hideSoftKeyboad(v)
            } else if (last_name_.isEmpty()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_2), Color.RED)
                et_profile_last.requestFocus()
                hideSoftKeyboad(v)
            } else {
                initJsonOperationPost(first_name_, last_name_, mob_, email_)
            }
        }

    }
    private fun initViewSubmitUpdate() {

        edit_btn_edit.setOnClickListener { v ->

            val first_name = et_profile_first.text.toString()
            val last_name = et_profile_last.text.toString()
            val mobile = et_profile_mobile.text.toString()
            val email = et_profile_email.text.toString()


            if (first_name.isEmpty()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_1), Color.RED)
                et_profile_first.requestFocus()
                hideSoftKeyboad(v)
            } else if (last_name.isEmpty()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_2), Color.RED)
                et_profile_last.requestFocus()
                hideSoftKeyboad(v)
            } else if (mobile.isEmpty()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_3), Color.RED)
                et_profile_mobile.requestFocus()
                hideSoftKeyboad(v)
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_4), Color.RED)
                et_profile_email.requestFocus()
                hideSoftKeyboad(v)
            } else {
                initJsonOperationUpdate(first_name, last_name, mobile, email)
            }
        }

    }

    private fun initJsonOperationPost(firstName: String, lastName: String, mob: String, email: String) {
        Utils.log(TAG!!, "Create Profile Post data : $firstName,$lastName,$mob,$email")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()
        Handler().postDelayed({
            pb.dismiss()
            LoginPrefences.getInstance().addData(this@EditProfileActivity, mob, "",email,firstName,lastName)
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session", "1")
            )
        }, 2000)

    }

    private fun initJsonOperationUpdate(firstName: String, lastName: String, mob: String, email: String) {
        Utils.log(TAG!!, "Edit Profile Update data : $firstName,$lastName,$mob,$email")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()
        Handler().postDelayed({
            pb.dismiss()
            LoginPrefences.getInstance().addData(this@EditProfileActivity, mob, "",email,firstName,lastName)
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session", "1")
            )
        }, 2000)

    }



}
