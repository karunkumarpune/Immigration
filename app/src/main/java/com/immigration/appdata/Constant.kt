package com.immigration.appdata


object Constant {

    const val BASE_URL = "http://worklime.com"
    const val BASE_URL2 = "https://raw.githubusercontent.com"


    const val urlSignUp = "/immigration/api/signup"

    const val urlVerifyOtp = "/immigration/api/verifyOtp"
    const val urlResendOtp = "/immigration/api/resendOtp"
    const val urlUpdateProfile = "/immigration/api/updateProfile"
    const val urlLogin= "/immigration/api/login"
    const val urlForgotPassword= "/immigration/api/forgotPassword"
    const val urlSetPassword= "/immigration/api/setPassword"
    const val urlChangePassword= "/immigration/api/changePassword"
    const val urlLogout= "/immigration/api/logout"



    //Key URL  SignUp  && Login && UpdateProfile
    const val key_accessToken ="accessToken"
    const val key_Fname ="firstName"
    const val key_Lname ="lastName"
    const val key_email ="email"
    const val key_contact ="contact"
    const val key_countryCode ="countryCode"
    const val key_password ="password"

    // access value anywhere
    var accessTokenValues=""
    var countryCodeValues=""
    var contactValues=""





}
