package com.immigration.model.question_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {

    @SerializedName("question_title")
    @Expose
    var questionTitle: String? = null
    @SerializedName("answer")
    @Expose
    var answer: List<Answer>? = null

}