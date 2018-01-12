package com.immigration.view.subscription

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.immigration.R
import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : AppCompatActivity() {

    private var session_sub:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_subscription)

        session_sub=intent.getStringExtra("session_sub")

        sub_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        btn_sumit.setOnClickListener {
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            onBackPressed()
        }


        if(session_sub.equals("1")) {
            subscribe_img.setImageResource(R.drawable.happy)
            sub_text.text = "congratulations"
            sub_descriop.text =resources.getString(R.string.txt_subscription_1)
        }else {
            subscribe_img.setImageResource(R.drawable.sorry)
            sub_text.text = "Sorry"
            sub_descriop.text = resources.getString(R.string.txt_subscription_sorray)
        }
    }
}
