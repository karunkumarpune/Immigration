/*
package com.immigration.view.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import com.immigration.R
import com.theartofdev.edmodo.cropper.CropImage

import java.io.File
import java.net.URI
import java.net.URISyntaxException


class Pro : AppCompatActivity() {

    private var imageview: ImageView? = null
    private var btnSelectImage: ImageView? = null
    private val REQUEST_CODE_STORAGE_PERMS = 401
    private var outPutfileUri: Uri? = null
    private var bitImg: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageview = findViewById<View>(R.id.profile_image)
        btnSelectImage = findViewById<View>(R.id.iv_camera)


        btnSelectImage!!.setOnClickListener { v ->

            AskPermissions()

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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TAKE_PIC && resultCode == Activity.RESULT_OK) {
            if (null != outPutfileUri) {
                crop_Method(outPutfileUri)
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data.data
                if (null != selectedImageUri) {
                    crop_Method(selectedImageUri)
                }
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                var file: File? = null
                val path = resultUri.toString()
                var bitmap: Bitmap? = null
                try {
                    file = File(URI(path))
                    imageview!!.setImageURI(resultUri)
                    //  this.path = file.getPath();
                    // this method is used to get pic name
                    //  getPicName(path, resultUri, file);
                    Log.d(" profilePicPath", "file " + file.path)

                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                    if (bitmap != null) {
                        val d = BitmapDrawable(resources, bitmap)
                        bitImg = bitmap
                        //                        user_pic_round.setImageDrawable(d);
                        //    invoiceBitmap = bitmap;
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }

        }

    }


    fun crop_Method(imageUri: Uri) {
        CropImage.activity(imageUri).start(this@Pro)
    }


    */
/*
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                imageview.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                imageview.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*//*



    fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    companion object {
        private val SELECT_PICTURE = 100
        internal var TAKE_PIC = 1
    }


}
*/
