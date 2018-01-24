package com.immigration.restservices


import com.immigration.appdata.Constant.key_Fname
import com.immigration.appdata.Constant.key_Lname
import com.immigration.appdata.Constant.key_accessToken
import com.immigration.appdata.Constant.key_contact
import com.immigration.appdata.Constant.key_countryCode
import com.immigration.appdata.Constant.urlChangePassword
import com.immigration.appdata.Constant.urlForgotPassword
import com.immigration.appdata.Constant.urlLogin
import com.immigration.appdata.Constant.urlLogout
import com.immigration.appdata.Constant.urlResendOtp
import com.immigration.appdata.Constant.urlSetPassword
import com.immigration.appdata.Constant.urlSignUp
import com.immigration.appdata.Constant.urlUpdateProfile
import com.immigration.appdata.Constant.urlVerifyOtp
import com.immigration.model.ResponseModel
import com.immigration.model.question_model.Status
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface APIService {


    //https://raw.githubusercontent.com/karunkumarpune/Expandeble/master/question_test.json

    //https://code.tutsplus.com/tutorials/sending-data-with-retrofit-2-http-client-for-android--cms-27845

    // http://www.androidhub4you.com/p/blog-page_27.html


    @GET("/karunkumarpune/Expandeble/master/question_test.json")
    fun getQuestion(): Call<Status>

    @Headers("Content-Type: application/json")
    @POST(urlSignUp)
    fun getUser(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlVerifyOtp)
    fun verifyOtp(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlResendOtp)
    fun resendOtp(@Body body: Map<String, String>): Call<ResponseModel>


    @Multipart
    @POST(urlUpdateProfile)
    fun postImage(@Header (key_accessToken) accessToken:String,
                  @Part image: MultipartBody.Part?,
                  @Part(key_Fname) firstName: RequestBody,
                  @Part(key_Lname) lastName: RequestBody,
                  @Part(key_contact) contact: RequestBody,
                  @Part(key_countryCode) countryCode: RequestBody
                 ): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlLogin)
    fun login(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlForgotPassword)
    fun forgotPassword(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlSetPassword)
    fun setPassword(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlChangePassword)
    fun changePasswords(@Header ("accessToken") accessToken:String,
                       @Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @GET(urlLogout)
    fun logout(@Header ("accessToken") accessToken:String): Call<ResponseModel>



/*



    @FormUrlEncoded
    @POST("/")
    fun Save(@Field("answer") name:String,
             @Field("Date") Date:String):Call<JSONObject>
*/


}