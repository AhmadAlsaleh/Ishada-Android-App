package com.crazy_iter.ishada.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.crazy_iter.ishada.Models.UserModel
import com.crazy_iter.ishada.R

class UsersAdapter(var context: Context, private val users: ArrayList<UserModel>)
    : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
            ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_user, p0, false))

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = users[position]
        holder.userTitleTV.text = user.username
        holder.userPositionTV.text = user.position
        holder.userCB.isChecked = user.selected
        holder.userCB.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                if (getSelectCount() > 1) {
                    user.selected = isChecked
                } else {
                    holder.userCB.isChecked = true
                    Toast.makeText(context, "Task have to Assign to One User at least", Toast.LENGTH_SHORT).show()
                }
            } else {
                user.selected = isChecked
            }
        }

    }

    private fun getSelectCount(): Int {
        var i = 0
        for (user in users) {
            if (user.selected) {
                i++
            }
        }
        return i
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userLL = itemView.findViewById<LinearLayout>(R.id.userLL)!!
        val userTitleTV = itemView.findViewById<TextView>(R.id.userTitleTV)!!
        val userPositionTV = itemView.findViewById<TextView>(R.id.userPositionTV)!!
        val userCB = itemView.findViewById<CheckBox>(R.id.userCB)!!

    }


}