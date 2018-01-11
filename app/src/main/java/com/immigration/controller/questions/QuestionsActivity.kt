package com.immigration.controller.questions

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
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


    var listAdapter: ExpandableListAdapter? = null
    var expListView: ExpandableListView? = null

    var isCheck: Boolean = false
    internal var url = "https://raw.githubusercontent.com/karunkumarpune/Expandeble/master/question.json"

    private var ExpAdapter: ExpandListAdapter? = null
    private var ExpandList: ExpandableListView? = null
    private lateinit var pb: CustomProgressBar
    private lateinit var ch_list: ArrayList<Answer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_questions)
        pb = CustomProgressBar(this);
        pb.setCancelable(false)
        pb.show()

        txt_title.text = intent.getStringExtra("option")
        initView()
        isCheck()

    }

    private fun initView() {

        ExpandList = findViewById<View>(R.id.simple_expandable_listview) as ExpandableListView?

        home_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


        btn_submit_qus.setOnClickListener {
            startActivity(Intent(this@QuestionsActivity, SubscriptionActivity::class.java)
                    .putExtra("session_sub", "0")
            )
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


        makejsonobjreq()
    }

    private fun makejsonobjreq() {

        AndroidNetworking.get(url).build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject) {
                //   Log.d("TAGS",response.toString())
                pb.dismiss()
                val list = ArrayList<Question>()
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
                            ch.flag = false
                            // ch.image = jo.getString("flag")

                            ch_list.add(ch)
                        } // for loop end
                        gru.items = ch_list
                        list.add(gru)
                    } // while loop end


                    /*  ExpandList!!.setOnChildClickListener({ parent, v, groupPosition, childPosition, id ->
                        Log.d("TAGS","ExpandList " +id  +parent + " "+v  +" ")

                       if(id==id) {

                           isCheck = true
                           isCheck()
                       }
                        true
                    })*/


                    ExpAdapter = ExpandListAdapter(this@QuestionsActivity, list)
                    ExpAdapter!!.notifyDataSetChanged();
                    ExpandList!!.setAdapter(ExpAdapter)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onError(anError: ANError) {
                pb.dismiss()
            }
        })
        var selected: String = ""

        ExpandList!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val rssItem = parent.getItemAtPosition(position) as QuestionsActivity
            Toast.makeText(this@QuestionsActivity, "hii" + rssItem, Toast.LENGTH_SHORT).show()
        }

        /*ExpandList!!.setOnChildClickListener({ expandableListView: ExpandableListView, view1: View, i: Int, i1: Int, l: Long ->
            Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show()
            true
        })*/


        /* ExpandList!!.selectedPosition(ExpandableListView.OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Log.d("TAGS", " groupPosition:  $groupPosition childPosition $childPosition ")

            selected = ExpAdapter!!.getChild(groupPosition, childPosition) as String

            true
        })*/
    //    Log.d("TAGS", " groupPosition:  ${ExpAdapter!!.getChild()}  ")

    }





    private fun isCheck() {

        Log.d("TAGS", isCheck.toString())
            if(isCheck) {
                btn_submit_qus.setOnClickListener {
                    startActivity(Intent(this@QuestionsActivity, SubscriptionActivity::class.java)
                            .putExtra("session_sub", "0")
                    )
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }else
                {
                    btn_submit_qus.setOnClickListener {
                        startActivity(Intent(this@QuestionsActivity, SubscriptionActivity::class.java)
                                .putExtra("session_sub", "1")
                        )
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                }

            }



    }

