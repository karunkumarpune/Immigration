package com.immigration.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.immigration.model.signup_model.Result

class ResponseModel {
   @SerializedName("message")
   @Expose
   var message: String? = null
   @SerializedName("result")
   @Expose
   var result: Result? = null
}