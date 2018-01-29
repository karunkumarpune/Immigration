package com.immigration.view.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.immigration.R
import com.immigration.appdata.Constant
import com.immigration.appdata.Constant.BASE_URL_Image
import com.immigration.appdata.Constant.DefaultImage
import com.immigration.appdata.Constant.key_profilePic
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.immigration.utils.Utils.errorHandler
import com.immigration.utils.Utils.hideSoftKeyboad
import com.immigration.view.home.NavigationActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

class EditProfileActivity : AppCompatActivity() {
   private var file: Uri? = null
   private var fileStored: String? = null
   private var mCurrentPhotoPath: String? = null
   private var APIService: APIService? = null
   private lateinit var pb: CustomProgressBar
   private var loginPreference: LoginPrefences? = null
   private val TAG = EditProfileActivity::class.java.getName()
   private val SELECT_PICTURE = 170
   private var REQUEST_IMAGE_CAPTURE = 291
   private val REQUEST_CODE_STORAGE_PERMS = 501
   private var resultUri: Uri? = null
   private var croupresultUri: Uri? = null
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
   private var snackbarMessage: String? = null
   
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Utils.moveLeftToRight(this)
      setContentView(R.layout.activity_edit_profile)
      loginPreference = LoginPrefences.getInstance()
      APIService = ApiUtils.apiService
      isCheckImage = false
      
      session_edit_profile = intent.getStringExtra("session_edit_profile")
      otp_email = intent.getStringExtra("otp_email")
      
      userAccessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(this))
      userCountryCode = loginPreference!!.getCountryCode(LoginPrefences.getInstance().getLoginPreferences(this))
      userFirstName = loginPreference!!.getFName(LoginPrefences.getInstance().getLoginPreferences(this))
      userLastName = loginPreference!!.getLName(LoginPrefences.getInstance().getLoginPreferences(this))
      userEmail = loginPreference!!.getEmail(LoginPrefences.getInstance().getLoginPreferences(this))
      userMobile = loginPreference!!.getMobile(LoginPrefences.getInstance().getLoginPreferences(this))
      userprofilePic = loginPreference!!.getProfilePic(LoginPrefences.getInstance().getLoginPreferences(this))
      
      
      if (session_edit_profile == "0") {
         userAccessToken=Constant.accessTokenValues
         txt_profile.text = "Create Profile"
         profile_image.setImageResource(R.drawable.ic_person)
         linearLayout_visable_mob.isFocusable = false
         et_profile_email.isFocusable = false
         et_profile_mobile.isFocusable = false
         
         et_profile_mobile.setText(Constant.contactValues)
         et_profile_email.setText(otp_email)
         initViewCropPicPost()
         initViewSubmitPost(Constant.contactValues, otp_email.toString())
         tv_country_code.text = Constant.countryCodeValues
      } else {
         txt_profile.text = "Edit Profile"
         edit_btn_click_back.visibility = View.VISIBLE
         tv_country_code.text = userCountryCode
         
         try {
            Glide.with(baseContext)
             .load(BASE_URL_Image + userprofilePic)
             .asBitmap()
             .error(R.drawable.user_holder)
             .placeholder(R.drawable.user_holder)
             .into(profile_image)
         } catch (e: Exception) {
            e.printStackTrace()
         }
         linearLayout_visable_mob.isFocusable = false
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
         Utils.moveLeftToRight(this)
      }
   }
   
   private fun initViewCropPicPost() {
      iv_camera.setOnClickListener {
         AskPermissions()
      }
   }
   
   
   private fun initViewSubmitPost(mob_: String, email_: String) {
      edit_btn_edit.setOnClickListener { v ->
         val first_name_ = et_profile_first.text.toString().trim()
         val last_name_ = et_profile_last.text.toString().trim()
         
         if (first_name_.isEmpty()) {
            hideSoftKeyboad(applicationContext,v)
            Utils.showToastSnackbar(this@EditProfileActivity, getString(R.string.edit_profile_validation_1), Color.WHITE)
            et_profile_first.requestFocus()
         } /*else if (last_name_.isEmpty()) {
                Utils.showToastSnackbar(this@EditProfileActivity, getString(R.string.edit_profile_validation_2), Color.WHITE)
                et_profile_last.requestFocus()
                hideSoftKeyboad(v)
            }*/ else {
            try {
               initJsonOperationPost(first_name_, last_name_)
            } catch (e: Exception) {
            }
         }
      }
   }
   
   
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
      var res: Int
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
            openCamera()
         } else if (options[item] == "Choose From Gallery") {
            dialog.dismiss()
            chooseFromGallery()
         } else if (options[item] == "Cancel") {
            dialog.dismiss()
         }
      }
      builder.show()
   }
   
   private fun openCamera() {
      val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      file = Uri.fromFile(getFile())
      intent.putExtra(MediaStore.EXTRA_OUTPUT, file)
      if (intent.resolveActivity(this.packageManager) != null) {
         startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
      }
   }
   
   private fun chooseFromGallery() {
      val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
      startActivityForResult(intent, SELECT_PICTURE)
   }
   
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {;
         try {
            //   val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, file)
            //   val ei = ExifInterface(file!!.path)
            //val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            /* var rotatedBitmap: Bitmap?
             rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
             }*/
            //profile_image.setImageBitmap(getResizedBitmap(rotatedBitmap,400))
            // val uri=  getImageContentUri(this,getFile())
            if (null != file) {
               crop_Method(file!!)
            }
         } catch (e: Exception) {
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
            var bitmap: Bitmap?
            try {
               isCheckImage = true
            } catch (e: URISyntaxException) {
               e.printStackTrace()
            }
            
            try {
               bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
               if (bitmap != null) {
                  profile_image.setImageURI(getImageUri1(bitmap))
                  croupresultUri = getImageUri1(bitmap)
                  deleteFiles()
               }
            } catch (e: Exception) {
               e.printStackTrace()
            }
         } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Utils.log(TAG, "EditProfile Crop Image error: ${result.error}")
         }
      }
   }
   
   
   private fun getFile(): File {
      val folder = Environment.getExternalStoragePublicDirectory("/immigration/images")// the file path
      if (!folder.exists()) {
         folder.mkdirs()
      }
      val timeStamp = SimpleDateFormat("yyyyMMdd").format(Date())
      val imageFileName = "pic" + timeStamp
      var image_file: File? = null
      try {
         image_file = File(folder, imageFileName + ".jpg")
      } catch (e: IOException) {
         e.printStackTrace()
      }
      mCurrentPhotoPath = image_file!!.absolutePath
      return image_file
   }
   
   private fun getImageUri1(inImage: Bitmap): Uri? {
      var uri: Uri? = null
      try {
         val options = BitmapFactory.Options()
         // Calculate inSampleSize
         options.inSampleSize = calculateInSampleSize(options, 400, 400)
         // Decode bitmap with inSampleSize set
         options.inJustDecodeBounds = false
         val newBitmap = Bitmap.createScaledBitmap(inImage, 400, 400, true)
         val file = File(filesDir, "Image" + Random().nextInt() + ".jpeg")
         val out = openFileOutput(file.name, MODE_PRIVATE)
         newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
         out.flush()
         out.close()
         //get absolute path
         val realPath = file.absolutePath
         val f = File(realPath)
         uri = Uri.fromFile(f)
      } catch (e: Exception) {
         Log.e("Your Error Message", e.message)
      }
      return uri
   }
   
   private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
      val height = options.outHeight
      val width = options.outWidth
      var inSampleSize = 1
      
      if (height > reqHeight || width > reqWidth) {
         val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
         val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
         inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
      }
      val totalPixels = (width * height).toFloat()
      val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
      while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
         inSampleSize++
      }
      
      return inSampleSize
   }
   
   /* private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
       var width = image.width
       var height = image.height
       val bitmapRatio = width.toFloat() / height.toFloat()
       if (bitmapRatio < 1 && width > maxSize) {
          width = maxSize
          height = (width / bitmapRatio).toInt()
       } else if (height > maxSize) {
          height = maxSize
          width = (height * bitmapRatio).toInt()
       }
       return Bitmap.createScaledBitmap(image, width, height, true)
    }*/
   private fun crop_Method(imageUri: Uri) {
      CropImage.activity(imageUri).start(this@EditProfileActivity)
   }
   
   private fun deleteFiles() {
      val tempFile = File(mCurrentPhotoPath)
      if (tempFile.exists())
         tempFile.delete()
   }
   
   
   //Helper method maltipart Image.
   private fun prepareFilePart(partName: String, fileUri: Uri?): MultipartBody.Part {
      // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
      // use the FileUtils to get the actual file by uri
      //FileUtils class is present inside com.app.siy.utils.FileUtils
      var part: MultipartBody.Part? = null
      val file = FileUtils.getFile(this, fileUri)
      //File file = new File(fileUri.getPath());  // will not work.
      var requestFile: RequestBody?
      try {
         if (file != null) {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            part = MultipartBody.Part.createFormData(partName, file.name, requestFile!!)
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
   private fun initJsonOperationPost(firstName: String, lastName: String) {
      var res_userId: String
      var res_email: String
      var res_countryCode: String
      var res_contact: String
      var res_firstName: String
      var res_lastName: String
      var res_profilePic: String
      
      pb = CustomProgressBar(this)
      pb.setCancelable(true)
      pb.show()
      val fname = RequestBody.create(MediaType.parse("text/plain"), firstName)
      val lname = RequestBody.create(MediaType.parse("text/plain"), lastName)
      val cnt_code = RequestBody.create(MediaType.parse("text/plain"), Constant.countryCodeValues)
      val mobile = RequestBody.create(MediaType.parse("text/plain"), Constant.contactValues)
      if (!this.isCheckImage!!) {
         if (session_edit_profile == "1") {
            val cnt_codes = RequestBody.create(MediaType.parse("text/plain"), userCountryCode!!)
            APIService!!.postImage(this.userAccessToken!!, null, fname, lname, mobile, cnt_codes)
             .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                   pb.dismiss()
                   val status = response!!.code()
                   when (status) {
                      200 -> {
                         Utils.showToast(applicationContext, response.body()!!.message.toString())
                         res_userId = response.body()!!.result!!.userId ?: "0"
                         res_email = response.body()!!.result!!.email ?: ""
                         res_countryCode = response.body()!!.result!!.countryCode ?: ""
                         res_contact = response.body()!!.result!!.contact ?: ""
                         res_firstName = response.body()!!.result!!.firstName ?: ""
                         res_lastName = response.body()!!.result!!.lastName ?: ""
                         res_profilePic = response.body()!!.result!!.profilePic ?: DefaultImage
                         
                         LoginPrefences.getInstance().addData(this@EditProfileActivity,
                          userAccessToken, res_userId, res_countryCode,
                          res_contact, SignupActivity.passwords_signup,
                          res_email, res_firstName,res_lastName,res_profilePic)
                         startActivity(Intent(this@EditProfileActivity, NavigationActivity::class.java)
                          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                          .putExtra("session", "1"))
                         finish()
                      }
                      201 -> {
                         Utils.showToast(applicationContext, response.body()!!.message.toString())
                      }
                      401 -> {
                         Utils.showToast(applicationContext, errorHandler(response))
                         Utils.invalidToken(this@EditProfileActivity, loginPreference, LoginActivity())
                      }
                   }
                   snackbarMessage = Utils.responseStatus(this@EditProfileActivity, status, response)
                   if (snackbarMessage != null) {
                      Utils.showToastSnackbar(this@EditProfileActivity, snackbarMessage!!, Color.WHITE)
                   }
                }
                
                override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                   pb.dismiss()
                   Utils.log(TAG, "otp:  Throwable-:   $t")
                   Utils.showToastSnackbar(this@EditProfileActivity, getString(R.string.no_internet).toString(), Color.RED)
                }
             })
         } else {
            APIService!!.postImage(this!!.userAccessToken!!, null, fname, lname, mobile, cnt_code)
             .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                   pb.dismiss()
                   val status = response!!.code()
                   when (status) {
                      200 -> {
                         Utils.showToast(applicationContext, response.body()!!.message.toString())
                         res_userId = response.body()!!.result!!.userId ?: "0"
                         res_email = response.body()!!.result!!.email ?: ""
                         res_countryCode = response.body()!!.result!!.countryCode ?: ""
                         res_contact = response.body()!!.result!!.contact ?: ""
                         res_firstName = response.body()!!.result!!.firstName ?: ""
                         res_lastName = response.body()!!.result!!.lastName ?: ""
                         res_profilePic = response.body()!!.result!!.profilePic ?: DefaultImage
   
                         LoginPrefences.getInstance().addData(this@EditProfileActivity,
                          userAccessToken, res_userId, res_countryCode,
                          res_contact, SignupActivity.passwords_signup,
                          res_email, res_firstName,res_lastName,res_profilePic)
                         startActivity(Intent(this@EditProfileActivity, NavigationActivity::class.java)
                          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                          .putExtra("session", "1"))
                         finish()
                      }
                      201 -> {
                         Utils.showToast(applicationContext, response.body()!!.message.toString())
                      }
                      401 -> {
                         Utils.showToast(applicationContext, errorHandler(response))
                         Utils.invalidToken(this@EditProfileActivity, loginPreference, LoginActivity())
                      }
                   }
                   snackbarMessage = Utils.responseStatus(this@EditProfileActivity, status, response)
                   if (snackbarMessage != null) {
                      Utils.showToastSnackbar(this@EditProfileActivity, snackbarMessage!!, Color.WHITE)
                   }
                }
                
                override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                   pb.dismiss()
                   Utils.log(TAG, "otp:  Throwable-:   $t")
                   Utils.showToastSnackbar(this@EditProfileActivity, getString(R.string.no_internet).toString(), Color.RED)
                }
             })
         }
      } else {
         val cnt_codes = RequestBody.create(MediaType.parse("text/plain"), Constant.countryCodeValues)
         val imageBody = prepareFilePart(key_profilePic, this.croupresultUri!!)
         APIService!!.postImage(this!!.userAccessToken!!, imageBody, fname, lname, mobile, cnt_codes)
          .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
             override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                pb.dismiss()
                Utils.log(TAG, "EditProfile onResponse  code: ${response!!.raw()}")
                val status = response.code()
                when (status) {
                   200 -> {
                      Utils.showToast(applicationContext, response.body()!!.message.toString())
                      res_userId = response.body()!!.result!!.userId ?: "0"
                      res_email = response.body()!!.result!!.email ?: ""
                      res_countryCode = response.body()!!.result!!.countryCode ?: ""
                      res_contact = response.body()!!.result!!.contact ?: ""
                      res_firstName = response.body()!!.result!!.firstName ?: ""
                      res_lastName = response.body()!!.result!!.lastName ?: ""
                      res_profilePic = response.body()!!.result!!.profilePic ?: DefaultImage
   
                      LoginPrefences.getInstance().addData(this@EditProfileActivity,
                       userAccessToken, res_userId, res_countryCode,
                       res_contact, SignupActivity.passwords_signup,
                       res_email, res_firstName,res_lastName,res_profilePic)
                      startActivity(Intent(this@EditProfileActivity, NavigationActivity::class.java)
                       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                       .putExtra("session", "1"))
                      finish()
                   }
                   201 -> {
                      Utils.showToast(applicationContext, response.body()!!.message.toString())
                   }
                   401 -> {
                      Utils.showToast(applicationContext, errorHandler(response))
                      Utils.invalidToken(this@EditProfileActivity, loginPreference, LoginActivity())
                   }
                }
                snackbarMessage = Utils.responseStatus(this@EditProfileActivity, status, response)
                if (snackbarMessage != null) {
                   Utils.showToastSnackbar(this@EditProfileActivity, snackbarMessage!!, Color.WHITE)
                }
             }
             
             override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                pb.dismiss()
                Utils.log(TAG, "otp:  Throwable-:   $t")
                Utils.showToastSnackbar(this@EditProfileActivity, getString(R.string.no_internet).toString(), Color.RED)
             }
          })
      }
   }
   
   override fun onBackPressed() {
      super.onBackPressed()
      if (session_edit_profile == "0") {
         startActivity(Intent(this@EditProfileActivity, LoginActivity::class.java))
         Utils.moveLeftToRight(this)
         finish()
      } else {
         Utils.moveLeftToRight(this)
      }
   }
}