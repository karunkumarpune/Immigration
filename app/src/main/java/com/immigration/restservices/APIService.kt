package com.immigration.restservices

import com.immigration.model.ResponseModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface APIService {


    //https://raw.githubusercontent.com/karunkumarpune/Expandeble/master/question_test.json

    //https://code.tutsplus.com/tutorials/sending-data-with-retrofit-2-http-client-for-android--cms-27845

    // http://www.androidhub4you.com/p/blog-page_27.html

    /*@GET("/karunkumarpune/Expandeble/master/question_test.json")
      fun getQuestion(): Call<Status>
  */
    @Headers("Content-Type: application/json")
    @POST("/immigration/api/signup")
    fun getUser(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST("/immigration/api/verifyOtp")
    fun verifyOtp(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST("/immigration/api/resendOtp")
    fun resendOtp(@Body body: Map<String, String>): Call<ResponseModel>


    @Multipart
    @POST("/immigration/api/updateProfile")
    fun postImage(@Header ("accessToken") accessToken:String,
                  @Part image: MultipartBody.Part,
                  @Part("firstName") firstName: String,
                  @Part("lastName") lastName: String,
                  @Part("contact") contact: String,
                  @Part("countryCode") countryCode: String
                 ): Call<ResponseModel>





/*

    @get:GET("/avatar_1.json")
    val avatar1: Call<JSONObject>

    @FormUrlEncoded
    @POST("/")
    fun Save(@Field("answer") name:String,
             @Field("Date") Date:String):Call<JSONObject>
*/


}