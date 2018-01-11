package com.immigration.controller.questions

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.immigration.R
import com.immigration.controller.questions.model.Answer
import com.immigration.controller.questions.model.Question
import com.immigration.controller.subscription.SubscriptionActivity
import com.immigration.utils.CustomProgressBar
import kotlinx.android.synthetic.main.activity_questions.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class QuestionsActivity : AppCompatActivity() {


    internal var url = "https://raw.githubusercontent.com/karunkumarpune/Expandeble/master/question.json"

    private var ExpAdapter: ExpandListAdapter? = null
    private var ExpandList: ExpandableListView? = null
    private lateinit var pb: CustomProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_questions)
        pb = CustomProgressBar(this);
        pb.setCancelable(false)
        pb.show()



        txt_title.text=intent.getStringExtra("option")
        initView()

    }

    private fun initView() {

        ExpandList = findViewById<View>(R.id.simple_expandable_listview) as ExpandableListView?

        home_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btn_submit_qus.setOnClickListener {
            startActivity(Intent(this, SubscriptionActivity::class.java))

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }



        makejsonobjreq()
    }

    private fun makejsonobjreq() {

        AndroidNetworking.get(url).build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject) {
                Log.d("TAGS",response.toString())
                pb.dismiss()
                val list = ArrayList<Question>()
                var ch_list: ArrayList<Answer>

                try {
                    val key = response.keys()
                    while (key.hasNext()) {
                        val k = key.next()

                        val gru = Question()
                        gru.name = k
                        ch_list = ArrayList()

                        val ja = response.getJSONArray(k)

                        for (i in 0 until ja.length()) {

                            val jo = ja.getJSONObject(i)

                            val ch = Answer()
                            ch.answer = jo.getString("name")
                           // ch.image = jo.getString("flag")

                            ch_list.add(ch)
                        } // for loop end
                        gru.items = ch_list
                        list.add(gru)
                    } // while loop end

                    ExpAdapter = ExpandListAdapter(this@QuestionsActivity, list)
                    ExpandList!!.setAdapter(ExpAdapter)



                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onError(anError: ANError) {
                pb.dismiss()
            }
        })
    }
}
