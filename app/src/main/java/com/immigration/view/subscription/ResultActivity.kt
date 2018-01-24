package com.immigration.view.subscription

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.immigration.R
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    private var session_sub:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_result)

        session_sub=intent.getStringExtra("session_sub")

        sub_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }






        btn_pdf_option_1.setOnClickListener {
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            showAlert()
        }
 btn_pdf_option_2.setOnClickListener {
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            showAlert()
        }
 btn_pdf_option_3.setOnClickListener {
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            showAlert()
        }
 btn_pdf_option_4.setOnClickListener {
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            showAlert()
        }
 btn_pdf_option_5.setOnClickListener {
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            showAlert()
        }
 btn_pdf_option_6.setOnClickListener {
           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            showAlert()
        }


       /* if(session_sub.equals("1")) {
            subscribe_img.setImageResource(R.drawable.happy)
            sub_text.text = "congratulations"
            sub_descriop.text =resources.getString(R.string.txt_subscription_1)
        }else {
            subscribe_img.setImageResource(R.drawable.sorry)
            sub_text.text = "Sorry"
            sub_descriop.text = resources.getString(R.string.txt_subscription_sorray)
        }*/
    }

    fun showAlert() {

        // get prompts.xml view
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.custom_alert_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
     //   val userInput = promptsView.findViewById(R.id.btn_ok) as Button
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        DialogInterface.OnClickListener {
                            _, _ ->
                            // get user input and set it to result
                            // edit text
                           // result.setText(userInput.text)
                        })
               // .setNegativeButton("Cancel",
                  //      DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()

    }

}
