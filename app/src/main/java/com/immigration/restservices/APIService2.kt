package com.immigration.restservices


import com.immigration.model.question_model.Status
import retrofit2.Call
import retrofit2.http.GET


interface APIService2  {
     @GET("/karunkumarpune/Expandeble/master/question_test.json")
     fun getQuestion(): Call<Status>
}