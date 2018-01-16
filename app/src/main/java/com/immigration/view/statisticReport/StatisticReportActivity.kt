package com.immigration.view.statisticReport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.immigration.R
import com.immigration.model.StatisticReport
import kotlinx.android.synthetic.main.activity_statistic_report.*

class StatisticReportActivity : AppCompatActivity() {


    lateinit var list:ArrayList<StatisticReport>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        setContentView(R.layout.activity_statistic_report)


        stati_btn_click_back.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }


        list= ArrayList()
        recy_notification.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description1), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description2), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description3), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description4), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description5), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description6), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description7), "8/01/2018"))

        jsonParse()
    }

    private fun jsonParse() {

        val adp= AdapterStatisticReport(list)
        recy_notification.adapter=adp
    }


}
