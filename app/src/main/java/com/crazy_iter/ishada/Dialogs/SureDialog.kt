package com.crazy_iter.ishada.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.Models.TaskModel
import com.crazy_iter.ishada.R
import com.crazy_iter.ishada.StaticVars
import kotlinx.android.synthetic.main.dialog_sure.*
import org.json.JSONObject

class SureDialog(context: Context, var message: String, var taskID: String, var newStatus: String)
    : Dialog(context) {

    var isDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sure)
        setCanceledOnTouchOutside(false)

        sureMessageTV.text = message
        sureCancelBTN.setOnClickListener {
            isDone = false
            dismiss()
        }
        sureDoneBTN.setOnClickListener {
            isDone = true
            sureCancelBTN.visibility = View.GONE
            sureDoneBTN.visibility = View.GONE
            surePB.visibility = View.VISIBLE

            changeStatus()

        }

    }

    private fun changeStatus() {
        val json = JSONObject()
        json.put("taskID", taskID)
        json.put("status", newStatus)
        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.POST, StaticVars.CHANGE_STATUS, json, {
            queue.cancelAll("change")
            Toast.makeText(context, "Task status has changed to $newStatus", Toast.LENGTH_SHORT).show()
            dismiss()
        }, {
            queue.cancelAll("change")
            Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show()
            sureCancelBTN.visibility = View.VISIBLE
            sureDoneBTN.visibility = View.VISIBLE
            surePB.visibility = View.GONE
        })
        request.tag = "change"
        request.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)
    }

}