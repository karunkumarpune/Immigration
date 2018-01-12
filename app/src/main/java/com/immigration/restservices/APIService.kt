package com.immigration.restservices

import retrofit2.Call
import retrofit2.http.GET
import com.immigration.model.question_model.Status


interface APIService {


    //https://raw.githubusercontent.com/karunkumarpune/Expandeble/master/question_test.json

    //https://code.tutsplus.com/tutorials/sending-data-with-retrofit-2-http-client-for-android--cms-27845

    // http://www.androidhub4you.com/p/blog-page_27.html

    @GET("/karunkumarpune/Expandeble/master/question_test.json")
    fun getQuestion(): Call<Status>


/*

    @get:GET("/avatar_1.json")
    val avatar1: Call<JSONObject>

    @FormUrlEncoded
    @POST("/")
    fun Save(@Field("answer") name:String,
             @Field("Date") Date:String):Call<JSONObject>
*/



}