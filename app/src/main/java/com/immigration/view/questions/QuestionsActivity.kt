package com.immigration.view.questions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.immigration.R
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import com.immigration.utils.CustomProgressBar
import kotlinx.android.synthetic.main.activity_questions.*


class QuestionsActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null


    private var rideAdapter: QuestionAdapter? = null
    private var mAPIService: APIService? = null
    private lateinit var pb: CustomProgressBar

    companion object {
        lateinit var txt_count: TextView
        var isChecks: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_questions)

        mAPIService = ApiUtils.apiService;
        pb = CustomProgressBar(this);
        pb.setCancelable(false)
        pb.show()

        txt_title.text = intent.getStringExtra("option")
        initView()

    }

    private fun initView() {
        recyclerView = findViewById(R.id.question_recy)
        txt_count = findViewById<TextView>(R.id.txt_count_ponts)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        home_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }




      //  initJsonPareses()
    }

   /* private fun initJsonPareses() {

        mAPIService!!.getQuestion().enqueue(object : Callback, retrofit2.Callback<Status> {

            override fun onResponse(call: Call<Status>?, response: Response<Status>?) {
                pb.dismiss()
                if (response!!.isSuccessful) {
                    val list = response.body().result
                    try {
                        rideAdapter = QuestionAdapter(this@QuestionsActivity, list as ArrayList<Result_mo>)
                        recyclerView!!.adapter = rideAdapter
                        rideAdapter!!.notifyDataSetChanged()

                    } catch (e: Exception) {
                    }

                }

            }

            override fun onFailure(call: Call<Status>?, t: Throwable?) {
                pb.dismiss()
                Utils.showToast(this@QuestionsActivity,"Sorry!No internet available",Color.RED)

            }
        })


        btn_submit_qus.setOnClickListener {
            if (isChecks) {
                startActivity(Intent(this@QuestionsActivity, ResultActivity::class.java)
                        .putExtra("session_sub", "1")
                )
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            } else Utils.showToast(this, getString(R.string.question_validation), Color.WHITE)

        }


    }*/

    override fun onStop() {
        super.onStop()
        isChecks=false
    }
}

