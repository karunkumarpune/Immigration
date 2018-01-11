package com.immigration.controller.questions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.immigration.R
import com.immigration.controller.questions.model.Answer
import com.immigration.controller.questions.model.Question
import java.util.*






class ExpandListAdapter(private val context: Context, private val questions: ArrayList<Question>) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val chList = questions[groupPosition].items
        return chList!![childPosition]
    }



    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val answer = getChild(groupPosition, childPosition) as Answer
        if (convertView == null) {
            val infalInflater = LayoutInflater.from(parent.context)
            convertView = infalInflater.inflate(R.layout.adapter_answer_layout, null)
        }

        val answer_ = convertView!!.findViewById<View>(R.id.radiogroup) as RadioGroup

        answer_.removeAllViews()
       // for (i in 0 until childPosition) {
            val button = RadioButton(context)
           button.id = groupPosition+childPosition
        button.textSize = context.resources.getDimension(R.dimen.textsize)
        button.text = answer.answer!!.toString()
        button.scaleX = .8f;
        button.scaleY = .8f;


          //  button.isChecked = i == currentHours // Only select button with same index as currently selected number of hours
          //  button.setBackgroundResource(R.drawable.item_selector) // This is a custom button drawable, defined in XML




/*
        val activity = context as QuestionsActivity
      activity.btn_sumit.setOnClickListener {
          context.startActivity(Intent(context, SubscriptionActivity::class.java)
                  .putExtra("session_sub","1")
          )
          context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

      }*/
            answer_.addView(button)
       // }


        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val chList = questions[groupPosition].items
        return 2
    }

    override fun getGroup(groupPosition: Int): Any {
        return questions[groupPosition]
    }

    override fun getGroupCount(): Int {
        return questions.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val question = getGroup(groupPosition) as Question
        if (convertView == null) {
            val inf = LayoutInflater.from(parent.context)
            convertView = inf.inflate(R.layout.adapter_question_layout, null)
        }
        val txt_questation_num = convertView!!.findViewById<View>(R.id.txt_questation_num) as TextView
        val txt_questations = convertView!!.findViewById<View>(R.id.txt_questations) as TextView
        txt_questation_num.text = "Question ${groupPosition+1}"
        txt_questations.text = question.name


   //  txt_questations.setOnClickListener {
          // val activity = context as QuestionsActivity
           //activity.isCheck=true

       //  Toast.makeText(context,"hii",Toast.LENGTH_SHORT).show()
        //}


        return convertView
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }


}