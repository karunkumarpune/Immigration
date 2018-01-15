package com.immigration.utils.croupimage

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.TextView
import com.immigration.R
import java.io.File
import java.io.IOException


class CropPicker(mContext: Activity) {
    private var cropPictureUrl: Uri? = null
    var cropImageUrl: Uri? = null
        private set

    private val ASPECT_X = 1
    private val ASPECT_Y = 1
    private val OUT_PUT_X = 300
    private val OUT_PUT_Y = 300
    private val SCALE = true
    internal var mContext: Context

    init {
        this.mContext = mContext
        val permissionUtil = PermissionUtil()
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(mContext, permissionUtil.cameraPermissions) && permissionUtil.verifyPermissions(mContext, permissionUtil.galleryPermissions)) {
                showImagePickerDialog(mContext)
            } else {
                ActivityCompat.requestPermissions(mContext, permissionUtil.cameraPermissions, SELECT_PICTURE_CAMERA)
            }
        } else {
            showImagePickerDialog(mContext)
        }
    }

    fun showImagePickerDialog(activity: Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)
        // ...Irrelevant code for customizing the buttons and title
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.image_chooser_dialog, null)
        dialogBuilder.setView(dialogView)

        val tv_camera = dialogView.findViewById<View>(R.id.tv_camera) as TextView
        val tv_gallery = dialogView.findViewById<View>(R.id.tv_gallery) as TextView

        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        tv_camera.setOnClickListener {
            var pictureChooseIntent: Intent? = null
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            output = File(dir, System.currentTimeMillis().toString() + "camera_pic.jpeg")
            pictureChooseIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output))
            activity.startActivityForResult(pictureChooseIntent, CHOOSE_PHOTO_INTENT)
            alertDialog.dismiss()
        }


        tv_gallery.setOnClickListener {
            var pictureChooseIntent: Intent? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pictureChooseIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pictureChooseIntent!!.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

            } else {
                pictureChooseIntent = Intent(Intent.ACTION_GET_CONTENT)
            }
            pictureChooseIntent!!.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            pictureChooseIntent!!.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pictureChooseIntent!!.type = "image/*"
            activity.startActivityForResult(pictureChooseIntent, CHOOSE_PHOTO_INTENT)
            alertDialog.dismiss()
        }

    }

    private fun cropImage(sourceImage: Uri?, destinationImage: Uri?) {
        var intent = Intent("com.android.camera.action.CROP")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        } else {
            intent = Intent(Intent.ACTION_GET_CONTENT)
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)


        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        intent.type = "image/*"

        val list = mContext.packageManager.queryIntentActivities(intent, 0)
        val size = list.size
        if (size == 0) {
            cropImageUrl = sourceImage
            intent.putExtra(MediaStore.EXTRA_OUTPUT, sourceImage)
            (mContext as Activity).startActivityForResult(intent, SELECTED_IMG_CROP)
            return
        } else {
            intent.setDataAndType(sourceImage, "image/*")
            intent.putExtra("aspectX", ASPECT_X)
            intent.putExtra("aspectY", ASPECT_Y)
            intent.putExtra("outputY", OUT_PUT_Y)
            intent.putExtra("outputX", OUT_PUT_X)
            intent.putExtra("scale", SCALE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationImage)
            cropImageUrl = destinationImage
            if (size == 1) {
                val i = Intent(intent)
                val res = list[0]
                i.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                (mContext as Activity).startActivityForResult(intent, SELECTED_IMG_CROP)
            } else {
                val i = Intent(intent)
                i.putExtra(Intent.EXTRA_INITIAL_INTENTS, list.toTypedArray<Parcelable>())
                (mContext as Activity).startActivityForResult(intent, SELECTED_IMG_CROP)
            }
        }
    }

    fun handleGalleryResult(data: Intent) {
        try {
            cropPictureUrl = Uri.fromFile(FileUtil.getInstance(mContext)
                    .createImageTempFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)))
            cropImage(data.data, cropPictureUrl)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun handleCameraResult(cameraImageUri: Uri) {
        try {
            cropPictureUrl = Uri.fromFile(FileUtil.getInstance(mContext)
                    .createImageTempFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)))
            cropImage(cameraImageUri, cropPictureUrl)
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    companion object {
        var CHOOSE_PHOTO_INTENT = 101
        var SELECTED_IMG_CROP = 102
        var SELECT_PICTURE_CAMERA = 103
        var output: File? = null


        fun getPath(context: Context, uri: Uri): String? {

            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)

            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                          selectionArgs: Array<String>?): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                if (cursor != null)
                    cursor.close()
            }
            return null
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }
    }
}
