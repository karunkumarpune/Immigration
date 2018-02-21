package com.immigration.view.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.immigration.R
import com.immigration.appdata.Constant.BASE_URL_Image
import com.immigration.controller.application.AppController
import com.immigration.controller.sharedpreferences.LoginPrefences
import com.immigration.model.ResponseModel
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.ConnectivityReceiver
import com.immigration.utils.CustomProgressBar
import com.immigration.utils.TokenSharedPrefManager
import com.immigration.utils.Utils
import com.immigration.view.faq.FAQActivity
import com.immigration.view.generateReport.GenerateReport
import com.immigration.view.login.ChangePasswordActivity
import com.immigration.view.login.EditProfileActivity
import com.immigration.view.login.LoginActivity
import com.immigration.view.notification.NotificationActivity
import com.immigration.view.privecy.PrivecyPolicyActivity
import com.immigration.view.questions.QuestionsActivity
import com.immigration.view.statisticReport.StatisticReportActivity
import com.immigration.view.subscription.SubscriptionListActivity
import com.immigration.view.support.SupportActivitys
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class NavigationActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
   override fun onNetworkConnectionChanged(isConnected: Boolean) {
      showSnack(isConnected)
   }
   
   private val TIME_DELAY = 2000
   private var back_pressed: Long = 0
   private val TAG = NavigationActivity::class.java.name
   private var snackbarMessage: String? = null
   private var tagMessage: String? = null
   private var session_id: String = ""
   private var mViewHolder: ViewHolder? = null
   private var APIService: APIService? = null
   private lateinit var pb: CustomProgressBar
   private var loginPreference: LoginPrefences? = null
   private lateinit var accessToken: String
   private lateinit var userId: String
   private lateinit var email: String
   private lateinit var countryCode: String
   private lateinit var contact: String
   private lateinit var profilePic: String
   private lateinit var firstName: String
   private lateinit var lastName: String
   
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Utils.moveLeftToRight(this)
      setContentView(R.layout.activity_navigation)
      
      session_id = intent.getStringExtra("session")
      loginPreference = LoginPrefences.getInstance()
      
      if (session_id == "1") {
         try {
            accessToken = loginPreference!!.getAccessToken(LoginPrefences.getInstance().getLoginPreferences(this))
         } catch (e: Exception) {
         }
         try {
            firstName = loginPreference!!.getFName(LoginPrefences.getInstance().getLoginPreferences(this))
         } catch (e: Exception) {
         }
         try {
         } catch (e: Exception) {
         }
         try {
            lastName = loginPreference!!.getLName(LoginPrefences.getInstance().getLoginPreferences(this))
         } catch (e: Exception) {
         }
         
         try {
            contact = loginPreference!!.getMobile(LoginPrefences.getInstance().getLoginPreferences(this))
         } catch (e: Exception) {
         }
         try {
            profilePic = loginPreference!!.getProfilePic(LoginPrefences.getInstance().getLoginPreferences(this))
         } catch (e: Exception) {
         }
      }
      
      initView()
      try {
         APIService = ApiUtils.apiService
      }catch (e: Exception) { }
      
      pb = CustomProgressBar(this);
      pb.setCancelable(false)
      pb.show()
      
      mViewHolder = ViewHolder()
      
      handleDrawer()
      
      if (ConnectivityReceiver.isConnected) {
         try {
            // jsonParse()
         } catch (e: Exception) {
         }
         // showSnack(true);
      } else {
         try {
            // jsonParse()
         } catch (e: Exception) {
         }
      }
      
      Handler().postDelayed({
         pb.dismiss()
      }, 1000)
   }
   /*init View */
   private fun initView() {
      val btn_menu_option_1: LinearLayout = findViewById(R.id.btn_menu_option_1)
      val btn_menu_option_2: LinearLayout = findViewById(R.id.btn_menu_option_2)
      val btn_menu_option_3: LinearLayout = findViewById(R.id.btn_menu_option_3)
      val btn_menu_option_4: LinearLayout = findViewById(R.id.btn_menu_option_4)
      val btn_menu_option_5: LinearLayout = findViewById(R.id.btn_menu_option_5)
      val btn_menu_option_6: LinearLayout = findViewById(R.id.btn_menu_option_6)
      
      btn_menu_option_1.setOnClickListener {
         startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
          .putExtra("option", resources.getString(R.string.send_intent_1))
         )
      }
      
      btn_menu_option_2.setOnClickListener {
         startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
          .putExtra("option", resources.getString(R.string.send_intent_2))
         )
      }
      
      btn_menu_option_3.setOnClickListener {
         startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
          .putExtra("option", resources.getString(R.string.send_intent_3))
         )
      }
      
      btn_menu_option_4.setOnClickListener {
         startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
          .putExtra("option", resources.getString(R.string.send_intent_4))
         )
      }
      
      btn_menu_option_5.setOnClickListener {
         startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
          .putExtra("option", resources.getString(R.string.send_intent_5))
         )
      }
      
      btn_menu_option_6.setOnClickListener {
         startActivity(Intent(this@NavigationActivity, QuestionsActivity::class.java)
          .putExtra("option", resources.getString(R.string.send_intent_6))
         )
      }
   }
   
   
   private fun showSnack(isConnected: Boolean) {
      if (isConnected)
         Utils.showToastSnackbar(this, "Good! Connected to Internet", Color.GREEN)
      else
         Utils.showToastSnackbar(this, "Sorry!No internet available", Color.RED)
   }
   
   //----------------------------------------------------Open Menu Intent
   private fun handleDrawer() {
      val duoDrawerToggle = DuoDrawerToggle(this,
       mViewHolder!!.mDuoDrawerLayout,
       mViewHolder!!.mToolbar,
       R.string.navigation_drawer_open,
       R.string.navigation_drawer_close)
      
      if (session_id == "0") {
         //--------------------------------btn_6--------------------
         mViewHolder!!.txt_noti_count.visibility = View.GONE
         mViewHolder!!.txt_option_4.visibility = View.GONE
         
         mViewHolder!!.profile_pic.visibility = View.VISIBLE
         mViewHolder!!.txt_profile_name.visibility = View.VISIBLE
         mViewHolder!!.txt_profile_name.text = resources.getString(R.string.app_names)
         
         
         mViewHolder!!.txt_option_6.visibility = View.VISIBLE
         mViewHolder!!.view_id_6.visibility = View.VISIBLE
         mViewHolder!!.txt_option_6.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, FAQActivity::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_7--------------------
         mViewHolder!!.txt_option_7.visibility = View.VISIBLE
         mViewHolder!!.view_id_7.visibility = View.VISIBLE
         mViewHolder!!.txt_option_7.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, PrivecyPolicyActivity::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_8--------------------
         mViewHolder!!.txt_option_8.visibility = View.VISIBLE
         mViewHolder!!.view_id_8.visibility = View.VISIBLE
         mViewHolder!!.txt_option_8.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, SupportActivitys::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_9--------------------
         mViewHolder!!.txt_option_9.visibility = View.VISIBLE
         mViewHolder!!.view_id_9.visibility = View.VISIBLE
         mViewHolder!!.txt_option_9.text = "Login"
         mViewHolder!!.txt_option_9.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, LoginActivity::class.java))
            loginPreference!!.removeData(loginPreference!!.getLoginPreferences(this@NavigationActivity));
            finish()
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
      } else {
         mViewHolder!!.profile_pic.visibility = View.VISIBLE
         try {
            Glide.with(baseContext)
             .load(BASE_URL_Image + profilePic)
             .asBitmap()
             .error(R.drawable.user_holder)
             .placeholder(R.drawable.user_holder)
             .into(mViewHolder!!.profile_pic)
         } catch (e: Exception) {
            e.printStackTrace()
         }
         //--------------------------------btn_Edit--------------------
         mViewHolder!!.profile_pic.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, EditProfileActivity::class.java)
             .putExtra("session_edit_profile", "1")
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         
         mViewHolder!!.txt_profile_name.visibility = View.VISIBLE
         mViewHolder!!.txt_profile_name.text = firstName + " " + lastName
         //--------------------------------btn_Edit--------------------
         // mViewHolder!!.btn_edit.visibility=View.VISIBLE
         mViewHolder!!.btn_edit.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, EditProfileActivity::class.java))
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_1--------------------
         mViewHolder!!.txt_option_1.visibility = View.VISIBLE
         mViewHolder!!.view_id_1.visibility = View.VISIBLE
         mViewHolder!!.txt_option_1.text = resources.getString(R.string.txt_subscription)
         mViewHolder!!.txt_option_1.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, SubscriptionListActivity::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_2--------------------
         mViewHolder!!.txt_option_2.visibility = View.VISIBLE
         mViewHolder!!.view_id_2.visibility = View.VISIBLE
         mViewHolder!!.txt_option_2.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, GenerateReport::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_3--------------------
         mViewHolder!!.txt_option_3.visibility = View.VISIBLE
         mViewHolder!!.view_id_3.visibility = View.VISIBLE
         mViewHolder!!.txt_option_3.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, StatisticReportActivity::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_4--------------------
         mViewHolder!!.txt_option_4.visibility = View.VISIBLE
         mViewHolder!!.txt_noti_count.visibility = View.VISIBLE
         mViewHolder!!.view_id_4.visibility = View.VISIBLE
         mViewHolder!!.noti_relative_click.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, NotificationActivity::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_5--------------------
         mViewHolder!!.txt_option_5.visibility = View.VISIBLE
         mViewHolder!!.view_id_5.visibility = View.VISIBLE
         mViewHolder!!.txt_option_5.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, ChangePasswordActivity::class.java))
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_6--------------------
         mViewHolder!!.txt_option_6.visibility = View.VISIBLE
         mViewHolder!!.view_id_6.visibility = View.VISIBLE
         mViewHolder!!.txt_option_6.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, FAQActivity::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_7--------------------
         mViewHolder!!.txt_option_7.visibility = View.VISIBLE
         mViewHolder!!.view_id_7.visibility = View.VISIBLE
         mViewHolder!!.txt_option_7.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, PrivecyPolicyActivity::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_8--------------------
         mViewHolder!!.txt_option_8.visibility = View.VISIBLE
         mViewHolder!!.view_id_8.visibility = View.VISIBLE
         mViewHolder!!.txt_option_8.setOnClickListener { _ ->
            startActivity(Intent(this@NavigationActivity, SupportActivitys::class.java)
            )
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
         //--------------------------------btn_9--------------------
         mViewHolder!!.txt_option_9.visibility = View.VISIBLE
         mViewHolder!!.view_id_9.visibility = View.VISIBLE
         
         
         
         
         mViewHolder!!.txt_option_9.setOnClickListener { _ ->
            AlertDialog.Builder(this)
             .setIcon(android.R.drawable.ic_dialog_alert)
             .setTitle("")
             .setMessage(resources.getString(R.string.txt_close_app))
             .setPositiveButton(resources.getString(R.string.txt_yes)) { _, _ ->
                LogOut(accessToken)
                TokenSharedPrefManager.getInstance(this).getDeviceTokenClear()
                startActivity(Intent(this, LoginActivity::class.java))
                loginPreference!!.removeData(loginPreference!!.getLoginPreferences(this@NavigationActivity));
                finish()
                Utils.moveLeftToRight(this)
             }
             .setNegativeButton(resources.getString(R.string.txt_No), null)
             .show()
            
            
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
         }
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
      val profile_pic: ImageView = findViewById(R.id.profile_pic)
      val btn_edit: ImageButton = findViewById(R.id.btn_edit)
      val txt_profile_name: TextView = findViewById(R.id.txt_profile_name)
      val noti_relative_click: LinearLayout = findViewById(R.id.noti_relative_click)
      val txt_noti_count: TextView = findViewById(R.id.txt_noti_count)
      val view_id_1: View = findViewById(R.id.view_id_1)
      val view_id_2: View = findViewById(R.id.view_id_2)
      val view_id_3: View = findViewById(R.id.view_id_3)
      val view_id_4: View = findViewById(R.id.view_id_4)
      val view_id_5: View = findViewById(R.id.view_id_5)
      val view_id_6: View = findViewById(R.id.view_id_6)
      val view_id_7: View = findViewById(R.id.view_id_7)
      val view_id_8: View = findViewById(R.id.view_id_8)
      val view_id_9: View = findViewById(R.id.view_id_9)
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
      
      if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
         super.onBackPressed()
      } else {
         Toast.makeText(baseContext, getString(R.string.press_back), Toast.LENGTH_SHORT).show()
      }
      back_pressed = System.currentTimeMillis()
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
      
      AppController.getInstance().setConnectivityListener(this)
   }
   //------------------------------------0peration --------------------our------------
   /*private fun callFinish() {
       if (android.os.Build.VERSION.SDK_INT >= 21) {
           finishAndRemoveTask()
       } else {
           this.moveTaskToBack(true);

       }
   }*/
   private fun LogOut(accessToken: String) {
      APIService!!.logout(accessToken)
       .enqueue(object : Callback, retrofit2.Callback<ResponseModel> {
          override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
             pb.dismiss()
             val status = response!!.code()
             when (status) {
                200 -> {
                   Utils.showToast(applicationContext, response.body()!!.message.toString())
                }
                401 -> {
                   Utils.showToast(applicationContext, Utils.errorHandler(response))
                   Utils.invalidToken(this@NavigationActivity, loginPreference, LoginActivity())
                }
             }
             snackbarMessage = Utils.responseStatus(this@NavigationActivity, status, response)
             if (snackbarMessage != null) {
                Utils.showToastSnackbar(this@NavigationActivity, snackbarMessage!!, Color.WHITE)
             }
          }
          
          override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
             pb.dismiss()
             Utils.log(TAG, "logout:  Throwable-:   $t")
             Utils.showToastSnackbar(this@NavigationActivity, getString(R.string.no_internet).toString(), Color.RED)
          }
       })
   }
}