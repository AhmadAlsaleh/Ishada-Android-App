package com.crazy_iter.ishada.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.crazy_iter.ishada.Models.MeetingModel
import com.crazy_iter.ishada.R

class MeetingsAdapter(private val context: Context, private val meetings: ArrayList<MeetingModel>)
    : RecyclerView.Adapter<MeetingsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
            ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_meeting, p0, false))

    override fun getItemCount() = meetings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meeting = meetings[position]
        holder.meetingTitleTV.text = meeting.title
        holder.meetingDateTV.text = meeting.startDate
        holder.meetingNumberTV.text = meeting.num.toString()

        holder.meetingLL.setOnClickListener {
            Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show()
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val meetingLL = itemView.findViewById<LinearLayout>(R.id.meetingLL)!!
        val meetingTitleTV = itemView.findViewById<TextView>(R.id.meetingTitleTV)!!
        val meetingDateTV = itemView.findViewById<TextView>(R.id.meetingDateTV)!!
        val meetingNumberTV = itemView.findViewById<TextView>(R.id.meetingNumberTV)!!

    }

}