package com.immigration.view.statisticReport

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.immigration.R
import com.immigration.model.StatisticReport
import kotlinx.android.synthetic.main.adapter_statistic_report_layout.view.*


class AdapterStatisticReport(val list:ArrayList<StatisticReport>): RecyclerView.Adapter<AdapterStatisticReport.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterStatisticReport.ViewHolder {
        val v=LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_statistic_report_layout,parent,false)
        return AdapterStatisticReport.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AdapterStatisticReport.ViewHolder, position: Int) {
         holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         private val context:Context=itemView.context
          fun bindItems(model:StatisticReport){

              val data_=model.date;
              val date_split=data_.split("/")
              itemView.txt_only_date.text=date_split[0]
              itemView.txt_date.text=date_split[1]+"/"+date_split[2]

              itemView.txt_title.text=model.title
              itemView.txt_points.text="245 Points"
              itemView.txt_description.text=model.descrption

            /*  val check=model.descrption
              if(check.contains(".pdf")){
                  itemView.rl_document_type_bg.setBackgroundColor(Color.RED)
                  itemView.txt_document_type.text="PDF"
                  itemView.txt_description.text=model.descrption
              }else if(check.contains(".docx")){
                  itemView.rl_document_type_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_doc_color))
                  itemView.txt_document_type.text="DOCX"
                  itemView.txt_description.text=model.descrption
              }else if(check.contains(".jpg")){
                  itemView.rl_document_type_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_png_color))
                  itemView.txt_document_type.text="JPEG"
                  itemView.txt_description.text=model.descrption
              }else if(check.contains(".png")){
                  itemView.rl_document_type_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_jpg_color))
                  itemView.txt_document_type.text="PNG"
                  itemView.txt_description.text=model.descrption
              }*/


          }
    }

}

