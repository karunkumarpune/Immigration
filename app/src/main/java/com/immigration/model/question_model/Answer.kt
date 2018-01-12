package com.immigration.model.question_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Answer {

    @SerializedName("option")
    @Expose
    var option: String? = null

}