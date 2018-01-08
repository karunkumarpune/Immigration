package com.newyear.retrofit


import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {


    @get:GET("/avatar_1.json")
    val avatar1: Call<JSONObject>

    @FormUrlEncoded
    @POST("/")
    fun Save(@Field("name") name:String,
             @Field("Date") Date:String):Call<JSONObject>



}