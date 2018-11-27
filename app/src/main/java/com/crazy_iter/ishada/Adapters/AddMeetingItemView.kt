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
class AddMeetingItemView(context: Context, activity: NewMeetingActivity,
                         private val items: ArrayList<ItemModel>, private val item: ItemModel) : View(context) {

    val view = inflate(context, R.layout.item_meeting_item, null)!!

    init {
        val num = view.findViewById<TextView>(R.id.meetingItemNumTV)
        val subject = view.findViewById<EditText>(R.id.meetingItemSubjectET)
        val extra = view.findViewById<EditText>(R.id.meetingItemExtraET)
        val remove = view.findViewById<ImageView>(R.id.meetingItemRemoveIV)

        num.text = item.num.toString()
        subject.setText(item.subject)
        extra.setText(item.extra)

        remove.setOnClickListener {
            items.remove(item)
            for (i in 1 .. items.size) {
                items[i - 1].num = i
            }
            activity.setItems()
        }

        subject.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.subject = s?.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        extra.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.extra = s?.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

}