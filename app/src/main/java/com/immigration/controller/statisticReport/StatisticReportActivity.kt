package com.immigration.controller.statisticReport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.immigration.R
import com.immigration.model.StatisticReport
import kotlinx.android.synthetic.main.activity_notification.*
class StatisticReportActivity : AppCompatActivity() {


    lateinit var list:ArrayList<StatisticReport>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic_report)

        list= ArrayList()
        recy_notification.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))
        list.add(StatisticReport(getString(R.string.txt_title), getString(R.string.txt_description), "8/01/2018"))

        jsonParse()
    }

    private fun jsonParse() {

        val adp= AdapterStatisticReport(list)
        recy_notification.adapter=adp
    }


}
