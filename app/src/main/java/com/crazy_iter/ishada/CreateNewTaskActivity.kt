package com.crazy_iter.ishada

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_create_new_task.*
import org.json.JSONObject
import java.util.*

class CreateNewTaskActivity : AppCompatActivity() {

    private fun addTask() {
        if (newTaskTitleET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Task Title", Toast.LENGTH_SHORT).show()
            return
        }
        if (newTaskDateET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Task Date", Toast.LENGTH_SHORT).show()
            return
        }
        if (newTaskTimeET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Task Time", Toast.LENGTH_SHORT).show()
            return
        }

        newTaskFL.visibility = View.VISIBLE
        val json = JSONObject()
        json.put("title", newTaskTitleET.text.toString().trim())
        json.put("ownerID", getSharedPreferences("Ishada", Context.MODE_PRIVATE)
                .getString("userID", ""))
        json.put("description", newTaskDescriptionTV.text.toString().trim())
        json.put("status", "New")
        json.put("todoDate", newTaskDateET.text.toString().trim())

        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.POST, StaticVars.NEW_TASK, json, {
            queue.cancelAll("new")
            Toast.makeText(this, "Task Created", Toast.LENGTH_SHORT).show()
            finish()
        }, {
            queue.cancelAll("new")
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
            newTaskFL.visibility = View.GONE
        })
        request.tag = "new"
        request.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_task)

        setSupportActionBar(taskTB)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.newTaskFL, LoadingFragment())
        ft.commit()
        newTaskFL.visibility = View.GONE

        newTaskBTN.setOnClickListener {
            addTask()
        }
        newTaskTimeET.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val min = currentTime.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                newTaskTimeET.setText("$hourOfDay:$minute")
            }, hour, min, false)
            timePicker.setTitle("Task TODO Time")
            timePicker.show()

        }
        newTaskDateET.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val day = currentTime.get(Calendar.DAY_OF_MONTH)
            val month = currentTime.get(Calendar.MONTH)
            val year = currentTime.get(Calendar.YEAR)
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                newTaskDateET.setText("$year-$month-$dayOfMonth")
            }, year, month, day)
            datePicker.setTitle("Task TODO Date")
            datePicker.show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle("Cancel Task")
                .setMessage("Sure to Cancel this Task?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()

    }
}
