package com.immigration.view.notification

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.immigration.R
import com.immigration.model.Notification
import kotlinx.android.synthetic.main.adapter_notification_layout.view.*


class AdapterNotification(val list:ArrayList<Notification>): RecyclerView.Adapter<AdapterNotification.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):AdapterNotification.ViewHolder {
        val v=LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_notification_layout,parent,false)
        return AdapterNotification.ViewHolder(v)
    }

    override fun onBindViewHolder(holder:AdapterNotification.ViewHolder, position: Int) {
         holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

          fun bindItems(model:Notification){

              itemView.txt_title.text=model.title
              itemView.txt_date.text=model.date
              itemView.txt_description.text=model.descrption
          }
    }

}

