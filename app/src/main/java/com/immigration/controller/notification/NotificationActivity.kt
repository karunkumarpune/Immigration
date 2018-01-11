package com.immigration.controller.notification

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.immigration.R
import com.immigration.model.Notification
import kotlinx.android.synthetic.main.activity_notification.*


class NotificationActivity : AppCompatActivity() {

    lateinit var list:ArrayList<Notification>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_notification)


        noti_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        list= ArrayList()
        recy_notification.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false)

        list.add(Notification(getString(R.string.txt_title), getString(R.string.txt_description), "6:45 PM"))
        list.add(Notification(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(Notification(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(Notification(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(Notification(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(Notification(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))

        jsonParse()
    }

    private fun jsonParse() {

        val adp=AdapterNotification(list)
        recy_notification.adapter=adp
    }


}
