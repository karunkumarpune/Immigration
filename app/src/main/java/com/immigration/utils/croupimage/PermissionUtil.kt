package com.immigration.utils.croupimage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog

import com.immigration.R

class PermissionUtil {
    val galleryPermissions = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE")

    val cameraPermissions = arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE")

    fun verifyPermissions(context: Context, grantResults: Array<String>): Boolean {
        for (result in grantResults) {
            if (ActivityCompat.checkSelfPermission(context, result) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun checkMarshMellowPermission(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    companion object {

        fun showPermissionDialog(mContext: Context, msg: String) {
            val builder = AlertDialog.Builder(mContext, R.style.DatePicker)
            builder.setTitle("Need Permission")
            builder.setMessage(msg)
            builder.setPositiveButton(mContext.getString(R.string.invitation_yes)) { dialogInterface, i ->
                dialogInterface.dismiss()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", mContext.packageName, null)
                intent.data = uri
                mContext.startActivity(intent)
            }

            builder.setNegativeButton(mContext.getString(R.string.invitation_del_no)) { dialogInterface, i -> dialogInterface.dismiss() }
            builder.show()
        }
    }

}