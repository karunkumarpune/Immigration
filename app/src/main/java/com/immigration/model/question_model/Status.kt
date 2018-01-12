package com.immigration.model.question_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Status {

    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

}