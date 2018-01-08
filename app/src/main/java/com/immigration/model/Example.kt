package com.immigration.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Example(result: List<Result>) {

    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

    init {
        this.result = result
    }

}