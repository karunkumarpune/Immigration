package com.immigration.view.questions

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.immigration.R
import com.immigration.model.question_model.Result
import java.util.*


class QuestionAdapter(val context: Context, val list: ArrayList<Result>) : RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {
    var flags = true
    var count = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_question_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val res = list[position]


        holder.txt_questation_num.text = "Question ${position + 1}"
        holder.txt_question.text = res.questionTitle

        //----------------------------------Runtime Inflater------------------------------
        holder.click_hide_show.setOnClickListener({

            if (flags) {
                val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var view: View? = null
                holder.llRootLayout.removeAllViews()

                view = layoutInflater.inflate(R.layout.adapter_answer_layout, holder.llRootLayout, false)
                val click_hide_show_ans = view!!.findViewById<View>(R.id.linearLayout_bind_radio) as LinearLayout
                click_hide_show_ans.visibility = View.VISIBLE

                val rb = arrayOfNulls<RadioButton>(5)
                val rg = RadioGroup(context)
                rg.orientation = RadioGroup.VERTICAL
                for ((index, i) in (res.answer)!!.withIndex()) {
                    rb[index] = RadioButton(context)
                    rg.addView(rb[index])
                    rb[index]!!.text = i.option
                    rb[index]!!.scaleX = .8f;
                    rb[index]!!.scaleY = .8f;
                    rb[index]!!.textSize = context.resources.getDimension(R.dimen.textsize)

                    rb[index]!!.setOnClickListener {
                        if (!rb[index]!!.isChecked) {
                            count--
                        }
                        QuestionsActivity.isChecks=true
                        QuestionsActivity.txt_count.text= count.toString()
                        Toast.makeText(context," ${i.option}",Toast.LENGTH_SHORT).show()
                    }


                }

                count++

                holder.llRootLayout.addView(view)
                click_hide_show_ans.addView(rg)


                flags = false
            } else {
                val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var view: View?
                holder.llRootLayout.removeAllViews()

                view = layoutInflater.inflate(R.layout.adapter_answer_layout, holder.llRootLayout, false)
                val click_hide_show_ans = view!!.findViewById<View>(R.id.linearLayout_bind_radio) as LinearLayout
                click_hide_show_ans.visibility = View.GONE


                val rb = arrayOfNulls<RadioButton>(6)
                val rg = RadioGroup(context)
                rg.orientation = RadioGroup.VERTICAL
                for ((index, i) in (res.answer)!!.withIndex()) {
                    rb[index] = RadioButton(context)
                    rg.addView(rb[index])
                    rb[index]!!.text = i.option


                }
                holder.llRootLayout.addView(view)
                click_hide_show_ans.addView(rg)

                flags = true
            }
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llRootLayout: LinearLayout = view.findViewById(R.id.llRootLayout)
        val click_hide_show: RelativeLayout = view.findViewById(R.id.click_hide_show)
        val txt_questation_num: TextView = view.findViewById(R.id.txt_questation_num)
        val txt_question: TextView = view.findViewById(R.id.txt_question)
    }

}