package com.immigration.view.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.immigration.R
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.view.home.NavigationActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.net.URI
import java.net.URISyntaxException
import javax.security.auth.callback.Callback

class EditProfileActivity : AppCompatActivity() {

    private var APIService: APIService? = null
    private lateinit var pb: CustomProgressBar
    private var loginPreference: LoginPrefences? = null
    private val TAG = EditProfileActivity::class.java!!.getName()
    var userImage: ByteArray? = null

    private val SELECT_PICTURE = 170
    private var TAKE_PIC = 291
    private val REQUEST_CODE_STORAGE_PERMS = 501
    private var outPutfileUri: Uri? = null
    private var bitImg: Bitmap? = null
    private var file: File? = null
    private var resultUri: Uri? = null
    private var isCheckImage: Boolean? = null


    private var session_edit_profile: String = ""


    private var userFirstName: String? = null
    private var userLastName: String? = null
    private var userEmail: String? = null
    private var userMobile: String? = null
    private var userprofilePic: String? = null
    private var userAccessToken: String? = null
    private var userCountryCode: String? = null


    private var otp_email: String? = null
    private var otp_contact: String? = null
    private var accessToken: String? = null
    private var countryCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_edit_profile)
        loginPreference = LoginPrefences.getInstance()
        APIService = ApiUtils.apiService
        isCheckImage = false

        session_edit_profile = intent.getStringExtra("session_edit_profile")
        otp_email = intent.getStringExtra("otp_email")
        otp_contact = intent.getStringExtra("otp_contact")
        accessToken = intent.getStringExtra("accessToken")
        countryCode = intent.getStringExtra("countryCode")

        userAccessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(this))
        userCountryCode = loginPreference!!.getCountryCode(LoginPrefences.getInstance().getLoginPreferences(this))
        userFirstName = loginPreference!!.getFName(LoginPrefences.getInstance().getLoginPreferences(this))
        userLastName = loginPreference!!.getLName(LoginPrefences.getInstance().getLoginPreferences(this))
        userEmail = loginPreference!!.getEmail(LoginPrefences.getInstance().getLoginPreferences(this))
        userMobile = loginPreference!!.getMobile(LoginPrefences.getInstance().getLoginPreferences(this))
        userprofilePic = loginPreference!!.getProfilePic(LoginPrefences.getInstance().getLoginPreferences(this))



        if (session_edit_profile.equals("0")) {

            txt_profile.text = "Create Profile"
            profile_image.setImageResource(R.drawable.ic_person)

            et_profile_mobile.setText(otp_contact)
            et_profile_email.setText(otp_email)
            et_profile_mobile.isFocusable = false
            et_profile_email.isFocusable = false

            initViewCropPicPost()
            initViewSubmitPost(otp_contact.toString(), otp_email.toString())


        } else {
            txt_profile.text = "Edit Profile"
            edit_btn_click_back.visibility = View.VISIBLE

            try {
                Glide.with(baseContext)
                        .load(userprofilePic)
                        .asBitmap()
                        .error(R.drawable.progress_animation)
                        .placeholder(R.drawable.progress_animation)
                        .into(profile_image)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            et_profile_email.isFocusable = false
            et_profile_mobile.isFocusable = false
            et_profile_first.setText(userFirstName.toString())
            et_profile_last.setText(userLastName.toString())
            et_profile_mobile.setText(userMobile.toString())
            et_profile_email.setText(userEmail.toString())

            initViewCropPicPost()
            initViewSubmitPost(userMobile.toString(), userEmail.toString())

        }



        edit_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun initViewCropPicPost() {
        iv_camera.setOnClickListener {
            AskPermissions()
            // Utils.log(TAG!!, "Create Profile Post data : ${FileUtil.getInstance(this).createImageUri()}")


        }
    }

    private fun initViewCropUpdate() {
        iv_camera.setOnClickListener {

        }
    }


    private fun hideSoftKeyboad(v: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

    }

    private fun initViewSubmitPost(mob_: String, email_: String) {

        edit_btn_edit.setOnClickListener { v ->

            val first_name_ = et_profile_first.text.toString().trim()
            val last_name_ = et_profile_last.text.toString().trim()

            if (first_name_.isEmpty()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_1), Color.RED)
                et_profile_first.requestFocus()
                hideSoftKeyboad(v)
            } else if (last_name_.isEmpty()) {
                Utils.showToast(this@EditProfileActivity, getString(R.string.edit_profile_validation_2), Color.RED)
                et_profile_last.requestFocus()
                hideSoftKeyboad(v)
            } else {
                try {
                    initJsonOperationPost(first_name_, last_name_, mob_, email_)
                } catch (e: Exception) {

                }
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
                // initJsonOperationUpdate(first_name, last_name, mobile, email)
            }
        }

    }
    //Camera

    //ASK Runtime permisstion camera open
    private fun AskPermissions() {
        if (applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (!hasPermissions()) {
                requestNecessaryPermissions()
            } else {
                selectImage()
            }
        } else {
            Toast.makeText(applicationContext, "Camera not supported", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("WrongConstant")
    private fun hasPermissions(): Boolean {
        var res = 0
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        for (perms in permissions) {
            res = applicationContext.checkCallingOrSelfPermission(perms)
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestNecessaryPermissions() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grandResults: IntArray) {
        var allowed = true
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMS -> for (res in grandResults) {
                allowed = allowed && res == PackageManager.PERMISSION_GRANTED
            }
            else -> allowed = false
        }
        if (allowed) {
            selectImage()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(applicationContext, "Camera Permissions denied", Toast.LENGTH_SHORT).show()
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(applicationContext, "Storage Permissions denied", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    //ASK Runtime permisstion camera end

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                dialog.dismiss()
                takeImageFromCamera()
            } else if (options[item] == "Choose From Gallery") {
                dialog.dismiss()
                choosefromgallery()

            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()

    }

    private fun takeImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(Environment.getExternalStorageDirectory(), System.currentTimeMillis().toString() + ".jpg")
        outPutfileUri = Uri.fromFile(file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri)
        startActivityForResult(intent, TAKE_PIC)
    }

    private fun choosefromgallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_PICTURE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TAKE_PIC && resultCode == Activity.RESULT_OK) {
            if (null != outPutfileUri) {
                crop_Method(outPutfileUri!!)
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data!!.data
                if (null != selectedImageUri) {
                    crop_Method(selectedImageUri)
                }
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.uri
                val path = resultUri.toString()
                var bitmap: Bitmap? = null
                try {
                    file = File(URI(path))
                    isCheckImage = true
                    profile_image.setImageURI(resultUri)
                    //  this.path = file.getPath();
                    // this method is used to get pic name
                    //  getPicName(path, resultUri, file);
                    Log.d(" profilePicPath", "file " + file!!.path)

                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                    if (bitmap != null) {
                        val d = BitmapDrawable(resources, bitmap)
                        bitImg = bitmap
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }

        }

    }

    private fun crop_Method(imageUri: Uri) {
        CropImage.activity(imageUri).start(this@EditProfileActivity)
    }

    //Helper method.
    private fun prepareFilePart(partName: String, fileUri: Uri?): MultipartBody.Part {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //FileUtils class is present inside com.app.siy.utils.FileUtils

        var part: MultipartBody.Part? = null
        val file = FileUtils.getFile(this, fileUri)       //File file = new File(fileUri.getPath());  // will not work.
        var requestFile: RequestBody? = null

        try {
            if (file != null) {
                // create RequestBody instance from file
                requestFile = RequestBody.create(MediaType.parse("image/*"), file!!
                )

                part = MultipartBody.Part.createFormData(partName, file!!.getName(), requestFile!!)
            } else {
                //AppUtils.log("File is null");
            }
        } catch (e: Exception) {
            //AppUtils.log("Exception while creating Part " + e.getMessage());
        }

        // MultipartBody.Part is used to send also the actual file name
        return part!!
    }


    //API Parse
    private fun initJsonOperationPost(firstName: String, lastName: String, mob: String, email: String) {
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()

        //firstName.replace("\"","")
        val fname = RequestBody.create(MediaType.parse("text/plain"), firstName)
        val lname = RequestBody.create(MediaType.parse("text/plain"), lastName)
        val cnt_code = RequestBody.create(MediaType.parse("text/plain"), countryCode.toString())
        val mobile = RequestBody.create(MediaType.parse("text/plain"), mob)
        if (!this!!.isCheckImage!!) {

            if (session_edit_profile.equals("1")) {
                val cnt_codes = RequestBody.create(MediaType.parse("text/plain"), userCountryCode)
                APIService!!.postImage(this!!.userAccessToken!!, null, fname, lname, mobile, cnt_codes)
                        .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
                            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                                pb.dismiss()
                                Utils.log(TAG!!, "EditProfile onResponse  code: ${response!!.raw()}")
                                val status = response!!.code()

                                if (response.isSuccessful) {

                                    var img = ""

                                    val accessToken = response.body().result.accessToken
                                    val userId = response.body().result.userId
                                    val email = response.body().result.email
                                    val countryCode = response.body().result.countryCode
                                    val contact = response.body().result.contact

                                    var profilePic = response.body().result.profilePic

                                    if (profilePic == null) {
                                        img = "https://oncopedia.pro/w/images/thumb/a/a5/UserPlaceholder.png/250px-UserPlaceholder.png"
                                    } else {
                                        img ="http://worklime.com/immigration/images/"+ profilePic
                                    }

                                    val firstName = response.body().result.firstName
                                    val lastName = response.body().result.lastName


                                    Utils.log(TAG!!, "EditProfile onResponse  isSuccessful:$userId, $email ,$countryCode, $contact  $firstName ,$lastName , $accessToken , $img ")
                                    LoginPrefences.getInstance().addData(this@EditProfileActivity,
                                            accessToken,
                                            userId.toString(),
                                            countryCode,
                                            contact, SignupActivity.passwords_signup,
                                            email, firstName, lastName, img)
                                    startActivity(Intent(this@EditProfileActivity, NavigationActivity::class.java)
                                            .putExtra("session", "1")
                                    )
                                }

                                if (status != 200) {
                                    when (status) {
                                        201 -> {
                                            val mess = response!!.body().message.toString()
                                            Utils.showToast(this@EditProfileActivity, mess, Color.YELLOW)
                                        }
                                        204 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        409 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        400 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        401 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        403 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        404 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        500 -> Utils.showToast(this@EditProfileActivity, resources.getString(R.string.error_status_1), Color.YELLOW)
                                        else -> Utils.showToast(this@EditProfileActivity, resources.getString(R.string.error_status_1), Color.RED)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                                pb.dismiss()
                                Utils.log(TAG!!, "EditProfile Throwable : $t")
                                Utils.showToast(this@EditProfileActivity, "Sorry!No internet available", Color.RED)
                            }
                        })


            }else {

                APIService!!.postImage(this!!.accessToken!!, null, fname, lname, mobile, cnt_code)
                        .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
                            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                                pb.dismiss()
                                Utils.log(TAG!!, "EditProfile onResponse  code: ${response!!.raw()}")
                                val status = response!!.code()

                                if (response.isSuccessful) {

                                    var img = ""

                                    val accessToken = response.body().result.accessToken
                                    val userId = response.body().result.userId
                                    val email = response.body().result.email
                                    val countryCode = response.body().result.countryCode
                                    val contact = response.body().result.contact

                                    var profilePic = response.body().result.profilePic

                                    if (profilePic == null) {
                                        img = "https://oncopedia.pro/w/images/thumb/a/a5/UserPlaceholder.png/250px-UserPlaceholder.png"
                                    } else {
                                        img ="http://worklime.com/immigration/images/"+ profilePic
                                    }

                                    val firstName = response.body().result.firstName
                                    val lastName = response.body().result.lastName


                                    Utils.log(TAG!!, "EditProfile onResponse  isSuccessful:$userId, $email ,$countryCode, $contact  $firstName ,$lastName , $accessToken , $img ")
                                    LoginPrefences.getInstance().addData(this@EditProfileActivity,
                                            accessToken,
                                            userId.toString(),
                                            countryCode,
                                            contact, SignupActivity.passwords_signup,
                                            email, firstName, lastName, img)
                                    startActivity(Intent(this@EditProfileActivity, NavigationActivity::class.java)
                                            .putExtra("session", "1")
                                    )
                                }

                                if (status != 200) {
                                    when (status) {
                                        201 -> {
                                            val mess = response!!.body().message.toString()
                                            Utils.showToast(this@EditProfileActivity, mess, Color.YELLOW)
                                        }
                                        204 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        409 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        400 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        401 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        403 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        404 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                        500 -> Utils.showToast(this@EditProfileActivity, resources.getString(R.string.error_status_1), Color.YELLOW)
                                        else -> Utils.showToast(this@EditProfileActivity, resources.getString(R.string.error_status_1), Color.RED)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                                pb.dismiss()
                                Utils.log(TAG!!, "EditProfile Throwable : $t")
                                Utils.showToast(this@EditProfileActivity, "Sorry!No internet available", Color.RED)
                            }
                        })
            }

        } else {

            val cnt_codes = RequestBody.create(MediaType.parse("text/plain"), userCountryCode)
            val imageBody = prepareFilePart("profilePic", this!!.resultUri!!)
            APIService!!.postImage(this!!.userAccessToken!!, imageBody, fname, lname, mobile, cnt_codes)
                    .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
                        override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                            pb.dismiss()
                            Utils.log(TAG!!, "EditProfile onResponse  code: ${response!!.raw()}")
                            val status = response!!.code()

                            if (response.isSuccessful) {

                                var img = ""

                                val accessToken = response.body().result.accessToken
                                val userId = response.body().result.userId
                                val email = response.body().result.email
                                val countryCode = response.body().result.countryCode
                                val contact = response.body().result.contact

                                var profilePic = response.body().result.profilePic

                                if (profilePic == null) {
                                    img = "https://oncopedia.pro/w/images/thumb/a/a5/UserPlaceholder.png/250px-UserPlaceholder.png"
                                } else {
                                    img ="http://worklime.com/immigration/images/"+ profilePic
                                }

                                val firstName = response.body().result.firstName
                                val lastName = response.body().result.lastName


                                Utils.log(TAG!!, "EditProfile onResponse  isSuccessful:$userId, $email ,$countryCode, $contact  $firstName ,$lastName , $accessToken , $img ")
                                LoginPrefences.getInstance().addData(this@EditProfileActivity,
                                        accessToken,
                                        userId.toString(),
                                        countryCode,
                                        contact, SignupActivity.passwords_signup,
                                        email, firstName, lastName, img)
                                startActivity(Intent(this@EditProfileActivity, NavigationActivity::class.java)
                                        .putExtra("session", "1")
                                )
                            }

                            if (status != 200) {
                                when (status) {
                                    201 -> {
                                        val mess = response!!.body().message.toString()
                                        Utils.showToast(this@EditProfileActivity, mess, Color.YELLOW)
                                    }
                                    204 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                    409 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                    400 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                    401 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                    403 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                    404 -> Utils.showToast(this@EditProfileActivity, errorHandler(response), Color.YELLOW)
                                    500 -> Utils.showToast(this@EditProfileActivity, resources.getString(R.string.error_status_1), Color.YELLOW)
                                    else -> Utils.showToast(this@EditProfileActivity, resources.getString(R.string.error_status_1), Color.RED)
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                            pb.dismiss()
                            Utils.log(TAG!!, "EditProfile Throwable : $t")
                            Utils.showToast(this@EditProfileActivity, "Sorry!No internet available", Color.RED)
                        }
                    })

        }
    }

    private fun errorHandler(response: Response<ResponseModel>?): String {
        return try {
            val jObjError = JSONObject(response!!.errorBody().string())
            jObjError.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }


    /*private fun initJsonOperationUpdate(firstName: String, lastName: String, mob: String, email: String) {
        Utils.log(TAG!!, "Edit Profile Update data : $firstName,$lastName,$mob,$email")
        pb = CustomProgressBar(this)
        pb.setCancelable(false)
        pb.show()
        Handler().postDelayed({
            pb.dismiss()
           // LoginPrefences.getInstance().addData(this@EditProfileActivity, mob, "",email,firstName,lastName)
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session", "1")
            )
        }, 2000)

    }
*/


}
