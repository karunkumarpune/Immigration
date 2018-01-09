package com.immigration.controller.subscription

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import kotlinx.android.synthetic.main.activity_subscription_list.*

class SubscriptionListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_subscription_list)


        subs_li_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        btn_subscribe1.setOnClickListener {
           // onBackPressed()

        }

        btn_subscribe2.setOnClickListener {
           // onBackPressed()
        }


    }
}
