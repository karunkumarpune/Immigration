package com.immigration.retrofit

import com.newyear.retrofit.ApiInterface

object ApiUtils {

    val BASE_URL = "http://api.karunkumar.in"
    val apiService: ApiInterface
        get() = RetrofitClient.getClient(BASE_URL).create(ApiInterface::class.java)

}