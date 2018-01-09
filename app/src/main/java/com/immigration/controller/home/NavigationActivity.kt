package com.immigration.controller.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.immigration.R
import com.immigration.controller.application.MyApplication
import com.immigration.controller.faq.FAQActivity
import com.immigration.controller.generateReport.GenerateReport
import com.immigration.controller.login.ChangePasswordActivity
import com.immigration.controller.login.EditProfileActivity
import com.immigration.controller.login.LoginActivity
import com.immigration.controller.notification.NotificationActivity
import com.immigration.controller.privecy.PrivecyPolicyActivity
import com.immigration.controller.questions.QuestionsActivity
import com.immigration.controller.statisticReport.StatisticReportActivity
import com.immigration.controller.subscription.SubscriptionListActivity
import com.immigration.controller.support.SupportActivitys
import com.immigration.restservices.ApiUtils
import com.immigration.utils.ConnectivityReceiver
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.Utils
import com.newyear.retrofit.ApiInterface
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle


class NavigationActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {


   override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected)
    }
    private lateinit var pb: CustomProgressBar
    private var mViewHolder: ViewHolder? = null
    private var apiInterface: ApiInterface? = null
    private lateinit var profile_pic: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_navigation)

        initView()
        apiInterface = ApiUtils.apiService
        pb = CustomProgressBar(this);
        pb.setCancelable(false)
        pb.show()

        mViewHolder = ViewHolder()
        profile_pic = findViewById<View>(R.id.profile_pic) as ImageView
        handleDrawer()



        if (ConnectivityReceiver.isConnected()) {
            try {
               // jsonParse()
            } catch (e: Exception) { }

            // showSnack(true);
        } else {
            try {
               // jsonParse()
            } catch (e: Exception) {
            }
        }

        Handler().postDelayed({
            pb.dismiss()
        },3000)
    }


    /*init View */

    private fun initView() {

        val btn_menu_option_1: TextView = findViewById(R.id.btn_menu_option_1)
        val btn_menu_option_2: TextView = findViewById(R.id.btn_menu_option_2)
        val btn_menu_option_3: TextView = findViewById(R.id.btn_menu_option_3)
        val btn_menu_option_4: TextView = findViewById(R.id.btn_menu_option_4)
        val btn_menu_option_5: TextView = findViewById(R.id.btn_menu_option_5)
        val btn_menu_option_6: TextView = findViewById(R.id.btn_menu_option_6)

        btn_menu_option_1.setOnClickListener {
            startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
                    .putExtra("option",resources.getString(R.string.send_intent_1) )
            )
        }

        btn_menu_option_2.setOnClickListener {
            startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
                    .putExtra("option",resources.getString(R.string.send_intent_2) )
            )
        }

        btn_menu_option_3.setOnClickListener {
            startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
                    .putExtra("option",resources.getString(R.string.send_intent_3) )
            )
        }

       btn_menu_option_4.setOnClickListener {
            startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
                    .putExtra("option",resources.getString(R.string.send_intent_4) )
            )
        }

        btn_menu_option_5.setOnClickListener {
            startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
                    .putExtra("option",resources.getString(R.string.send_intent_5) )
            )
        }

        btn_menu_option_6.setOnClickListener {
            startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
                    .putExtra("option",resources.getString(R.string.send_intent_6) )
            )
        }

    }


    private fun showSnack(isConnected: Boolean) {
        if (isConnected)
            Utils.showToast(this,"Good! Connected to Internet",Color.GREEN)
         else
            Utils.showToast(this,"Good! Connected to Internet",Color.RED)
    }

    //----------------------------------------------------Open Menu Intent
    private fun handleDrawer() {
        val duoDrawerToggle = DuoDrawerToggle(this,
                mViewHolder!!.mDuoDrawerLayout,
                mViewHolder!!.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)

       //--------------------------------btn_Edit--------------------
        mViewHolder!!.btn_edit.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,EditProfileActivity::class.java)

            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }
 //--------------------------------btn_1--------------------
        mViewHolder!!.txt_option_1.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,SubscriptionListActivity::class.java)
                    .putExtra("option", 1)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_2--------------------
        mViewHolder!!.txt_option_2.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,GenerateReport::class.java)
                    .putExtra("option", 2)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_3--------------------
        mViewHolder!!.txt_option_3.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,StatisticReportActivity::class.java)
                    .putExtra("option", 3)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_4--------------------
        mViewHolder!!.txt_option_4.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,NotificationActivity::class.java)
                    .putExtra("option", 4)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_5--------------------
        mViewHolder!!.txt_option_5.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,ChangePasswordActivity::class.java)
                    .putExtra("option", 5)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_6--------------------
        mViewHolder!!.txt_option_6.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,FAQActivity::class.java)
                    .putExtra("option", 6)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }


        //--------------------------------btn_7--------------------
        mViewHolder!!.txt_option_7.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,PrivecyPolicyActivity::class.java)
                    .putExtra("option", 4)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_8--------------------
        mViewHolder!!.txt_option_8.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, SupportActivitys::class.java)

            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_9--------------------
        mViewHolder!!.txt_option_9.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,LoginActivity::class.java)

            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }



        mViewHolder!!.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle)
        duoDrawerToggle.syncState()

        mViewHolder!!.mToolbar.setNavigationIcon(R.drawable.ic_lines_menu)
        mViewHolder!!.mToolbar.hideOverflowMenu()
        mViewHolder!!.mToolbar.showContextMenu()

    }

    private inner class ViewHolder internal constructor() {
        val mDuoDrawerLayout: DuoDrawerLayout = findViewById(R.id.drawer)
        val mToolbar: Toolbar = findViewById(R.id.toolbar)
        val btn_edit: ImageButton = findViewById(R.id.btn_edit)

        val txt_option_1: TextView = findViewById(R.id.txt_option_1)
        val txt_option_2: TextView = findViewById(R.id.txt_option_2)
        val txt_option_3: TextView = findViewById(R.id.txt_option_3)
        val txt_option_4: TextView = findViewById(R.id.txt_option_4)
        val txt_option_5: TextView = findViewById(R.id.txt_option_5)
        val txt_option_6: TextView = findViewById(R.id.txt_option_6)
        val txt_option_7: TextView = findViewById(R.id.txt_option_7)
        val txt_option_8: TextView = findViewById(R.id.txt_option_8)
        val txt_option_9: TextView = findViewById(R.id.txt_option_9)


    }


    override fun onBackPressed() {

        if (mViewHolder!!.mDuoDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mViewHolder!!.mDuoDrawerLayout.closeDrawer(GravityCompat.START)
        }
       /* AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage(resources.getString(R.string.txt_close_app))
                .setPositiveButton(resources.getString(R.string.txt_yes)) { _, _ -> callFinish() }
                .setNegativeButton(resources.getString(R.string.txt_No), null)
                .show()*/
    }


    override fun onStop() {
        if (mViewHolder!!.mDuoDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mViewHolder!!.mDuoDrawerLayout.closeDrawer(GravityCompat.START)
        }
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

        if (mViewHolder!!.mDuoDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mViewHolder!!.mDuoDrawerLayout.closeDrawer(GravityCompat.START)
        }

        MyApplication.getInstance().setConnectivityListener(this)
    }

    //--------------------------------------------API---Work----------



    private fun ImageLoaders(url: String) {

        try {
            Glide.with(baseContext)
                    .load(url)
                    .asGif()
                    .error(R.drawable.progress_animation)
                    .placeholder(R.drawable.progress_animation)
                    .into(profile_pic)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //------------------------------------0peration --------------------our------------

    private fun callFinish() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask()
        } else {
            this.moveTaskToBack(true);

        }
    }
}