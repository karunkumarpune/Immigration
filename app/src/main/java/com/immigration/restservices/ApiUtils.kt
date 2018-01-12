package com.immigration.restservices

object ApiUtils {

    val BASE_URL = "https://raw.githubusercontent.com"
    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL).create(APIService::class.java)

}