package com.immigration.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Example(result: List<Result_mo>) {

    @SerializedName("result")
    @Expose
    var result: List<Result_mo>? = null

    init {
        this.result = result
    }

}