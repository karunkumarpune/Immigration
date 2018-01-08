package com.immigration.utils

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.immigration.R


object Utils {

   //Custom Toast
    fun showToast(context: Context, message: String,color: Int): Snackbar {
        val sb = Snackbar.make((context as Activity).findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        sb.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        val textView = sb.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(color)
        sb.show()
        return sb
    }
}