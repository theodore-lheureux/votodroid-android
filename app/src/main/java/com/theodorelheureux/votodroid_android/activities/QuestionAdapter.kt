package com.theodorelheureux.votodroid_android.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.theodorelheureux.votodroid_android.GetPaginatedQuery
import com.theodorelheureux.votodroid_android.R
import com.theodorelheureux.votodroid_android.activities.QuestionAdapter.MyViewHolder
import com.theodorelheureux.votodroid_android.interfaces.onClickInterface

class QuestionAdapter(
    var list: MutableList<GetPaginatedQuery.Question>,
    var onClickInterface: onClickInterface
) : RecyclerView.Adapter<MyViewHolder>() {
    class MyViewHolder(v: LinearLayout) : RecyclerView.ViewHolder(v) {
        var tvQuestion: TextView
        var questionContainer: LinearLayout
        var btnResult: ImageButton

        init {
            tvQuestion = v.findViewById(R.id.tvQuestion)
            questionContainer = v
            btnResult = v.findViewById(R.id.btnResultButton)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_item, parent, false) as LinearLayout
        val vh = MyViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val question = list[position]
        holder.tvQuestion.text = question.text
        holder.questionContainer.setOnClickListener { view: View? ->
            onClickInterface.setClick(
                0,
                question.id.toString(),
                question.text
            )
        }
        holder.btnResult.setOnClickListener { view: View? ->
            onClickInterface.setClick(
                1,
                question.id.toString(),
                question.text
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}