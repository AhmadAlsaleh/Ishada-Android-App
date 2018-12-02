package com.crazy_iter.ishada.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.crazy_iter.ishada.Models.ItemModel
import com.crazy_iter.ishada.NewMeetingActivity
import com.crazy_iter.ishada.R

@SuppressLint("ViewConstructor")
class MeetingItemView(context: Context, item: ItemModel) : View(context) {

    val view = inflate(context, R.layout.item_meeting_view, null)!!

    init {
        val num = view.findViewById<TextView>(R.id.meetingItemNumTV)
        val subject = view.findViewById<TextView>(R.id.meetingItemSubjectTV)
        val extra = view.findViewById<TextView>(R.id.meetingItemExtraTV)

        num.text = item.num.toString()
        subject.text = item.subject
        extra.text = item.extra
    }

}