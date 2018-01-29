package com.immigration.view.notification

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.immigration.R
import com.immigration.model.Notification
import com.immigration.utils.Utils
import com.immigration.view.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_notification.*


class NotificationActivity : AppCompatActivity() {

    lateinit var list:ArrayList<Notification>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.moveLeftToRight(this)
        setContentView(R.layout.activity_notification)


        noti_btn_click_back.setOnClickListener {
            startActivity(Intent(this@NotificationActivity, NavigationActivity::class.java)
             .putExtra("session", "1")
             .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
             .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
            Utils.moveLeftToRight(this)
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
    
    
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@NotificationActivity, NavigationActivity::class.java)
         .putExtra("session", "1")
         .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
        Utils.moveLeftToRight(this)
    }
}
