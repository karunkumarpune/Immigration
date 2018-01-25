package com.immigration.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.immigration.R
import com.immigration.appdata.Constant
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.view.login.LoginActivity
import org.json.JSONObject
import retrofit2.Response


object Utils {

    private val SHOW_LOG = true

    //Custom showToastSnackbar
    fun showToastSnackbar(context: Context, message: String, color: Int): Snackbar {
        val sb = Snackbar.make((context as Activity).findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        sb.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        val textView = sb.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(color)
        sb.show()
        return sb
    }

    //Custom show Toast
    fun showToast(context: Context, message: CharSequence): Toast {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
        return toast
    }

    //Custome hide keyboard
    fun hideSoftKeyboad(context: Context,v: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }


    fun log(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun moveLeftToRight(context: Context) {
         val contexts =context as Activity
        contexts.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
    
    //API Is null Check
    
    fun isNullCheck(response: Response<ResponseModel>?){
    
        val accessToken = response!!.body().result.accessToken
        val userId = response.body().result.userId
        var email = response.body().result.email
        var countryCode = response.body().result.countryCode
        var contact = response.body().result.contact
        var firstName = response.body().result.firstName
        var lastName = response.body().result.lastName
        var profilePic = response.body().result.profilePic
    
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
    
    }
    
    
    
    // API Response Status check
    fun responseStatus(context: Context,status:Int,response: Response<ResponseModel>?):String{
        val contexts =context as Activity
        var snackbarMessage:String
        snackbarMessage = when (status) {
            204 -> errorHandler(response)
            409 -> errorHandler(response)
            400 -> errorHandler(response)
            403 -> errorHandler(response)
            404 -> errorHandler(response)
            500 -> contexts.resources.getString(R.string.error_status_1)
            else -> contexts.resources.getString(R.string.error_status_1)
        }
            return snackbarMessage
    }
    
    
    // API Response 401
    fun invalidToken(context: Context,loginPreference:LoginPrefences?,loginActivity: LoginActivity){
        val contexts =context as Activity
        loginPreference!!.removeData(loginPreference.getLoginPreferences(contexts));
        contexts.startActivity(Intent(contexts, loginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        contexts.finish()
        contexts.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
    
   //Error Response
    fun errorHandler(response: Response<ResponseModel>?): String {
       val errorResponseKey="message"
       return try {
           val jObjError = JSONObject(response!!.errorBody().string())
           jObjError.getString(errorResponseKey)
       } catch (e: Exception) { e.message!! }
   }



}