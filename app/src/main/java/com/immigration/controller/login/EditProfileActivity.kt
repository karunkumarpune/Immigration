package com.immigration.controller.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.immigration.R
import com.immigration.controller.home.NavigationActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private var session_edit_profile:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_edit_profile)

        session_edit_profile=intent.getStringExtra("session_edit_profile")


        if(session_edit_profile.equals("0")){
            txt_profile.text="Create Profile"
            profile_image.setImageResource(R.drawable.ic_person)
        }else{
            edit_btn_click_back.visibility= View.VISIBLE
            txt_profile.text="Edit Profile"
            profile_image.setImageResource(R.drawable.logo)


        }


        edit_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        signup_btn_edit.setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java)
                    .putExtra("session","1")
            )
        }
    }
}
