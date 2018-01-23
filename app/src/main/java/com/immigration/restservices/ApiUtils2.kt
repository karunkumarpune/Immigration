package com.immigration.restservices

import com.immigration.appdata.Constant.BASE_URL2

object ApiUtils2 {

        val apiService2: APIService2
        get() = RetrofitClient2.getClient(BASE_URL2).create(APIService2::class.java)

}

//https://raw.githubusercontent.com/karunkumarpune/Expandeble/master/question_test.json