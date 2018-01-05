package com.immigration

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.immigration.connectvity_check.ConnectivityReceiver
import com.immigration.connectvity_check.MyApplication
import com.immigration.login_info.LoginActivity
import com.immigration.retrofit.ApiUtils
import com.immigration.retrofit.model.Example
import com.newyear.retrofit.ApiInterface
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import java.util.*


class NavigationActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected)
    }

    private lateinit var progressBar: ProgressBar
    private var mViewHolder: ViewHolder? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var snackbar: Snackbar? = null
    private var apiInterface: ApiInterface? = null
    private lateinit var profile_pic: ImageView
    private lateinit var list: List<Example>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_navigation)
        apiInterface = ApiUtils.apiService
        list = ArrayList()
        mViewHolder = ViewHolder()

        coordinatorLayout = findViewById(R.id.main_content)
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
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
            progressBar.visibility = View.GONE
            showSnack(false)
        }
    }


    private fun showSnack(isConnected: Boolean) {
        var message: String
        val color: Int
        if (isConnected) {
            //jsonParse()
            message = "Good! Connected to Internet"
            color = Color.GREEN
        } else {
            message = "Sorry! Not connected to internet"
            color = Color.RED
        }
        snackbar = Snackbar.make(this.coordinatorLayout!!, message, Snackbar.LENGTH_LONG)
        val sbView = snackbar!!.view
        val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(color)
        snackbar!!.show()


    }

    //----------------------------------------------------Open Menu Intent
    private fun handleDrawer() {
        val duoDrawerToggle = DuoDrawerToggle(this,
                mViewHolder!!.mDuoDrawerLayout,
                mViewHolder!!.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)

//--------------------------------btn_1--------------------
        mViewHolder!!.btn_menu_option_1.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,LoginActivity::class.java)
                    .putExtra("option", 1)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_2--------------------
        mViewHolder!!.btn_menu_option_2.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,LoginActivity::class.java)
                    .putExtra("option", 2)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_3--------------------
        mViewHolder!!.btn_menu_option_3.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,LoginActivity::class.java)
                    .putExtra("option", 3)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_4--------------------
        mViewHolder!!.btn_menu_option_4.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,LoginActivity::class.java)
                    .putExtra("option", 4)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_5--------------------
        mViewHolder!!.btn_menu_option_5.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,LoginActivity::class.java)
                    .putExtra("option", 5)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
        }

        //--------------------------------btn_6--------------------
        mViewHolder!!.btn_menu_option_6.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity,LoginActivity::class.java)
                    .putExtra("option", 6)
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
        val btn_menu_option_1: TextView = findViewById(R.id.btn_menu_option_1)
        val btn_menu_option_2: TextView = findViewById(R.id.btn_menu_option_2)
        val btn_menu_option_3: TextView = findViewById(R.id.btn_menu_option_3)
        val btn_menu_option_4: TextView = findViewById(R.id.btn_menu_option_4)
        val btn_menu_option_5: TextView = findViewById(R.id.btn_menu_option_5)
        val btn_menu_option_6: TextView = findViewById(R.id.btn_menu_option_6)

    }


    override fun onBackPressed() {

        if (mViewHolder!!.mDuoDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mViewHolder!!.mDuoDrawerLayout.closeDrawer(GravityCompat.START)
        }
        AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage(resources.getString(R.string.txt_close_app))
                .setPositiveButton(resources.getString(R.string.txt_yes)) { _, _ -> callFinish() }
                .setNegativeButton(resources.getString(R.string.txt_No), null)
                .show()
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
            finish()
        }
    }
}