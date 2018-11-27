package com.crazy_iter.ishada

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.Adapters.AddMeetingItemView
import com.crazy_iter.ishada.Models.ItemModel
import kotlinx.android.synthetic.main.activity_new_meeting.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class NewMeetingActivity : AppCompatActivity() {

    private val items = ArrayList<ItemModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_meeting)

        setSupportActionBar(newMeetingTB)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.newMeetingFL, LoadingFragment())
        ft.commit()
        newMeetingFL.visibility = View.GONE

        items.add(ItemModel(1))
        newMeetingItemsLL.addView(AddMeetingItemView(this, this, items, items[0]).view)

        newMeetingAddItemBTN.setOnClickListener {
            items.add(ItemModel(items.size + 1))
            newMeetingItemsLL.addView(AddMeetingItemView(this, this, items, items.last()).view)
        }

        newMeetingDIV.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                newMeetingDET.setText("$dayOfMonth-$month-$year")
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        }

        newMeetingSTIV.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                newMeetingSTET.setText("$hourOfDay:$minute")
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
        }

        newMeetingETIV.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                newMeetingETET.setText("$hourOfDay:$minute")
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
        }

    }

    fun setItems() {
        newMeetingItemsLL.removeAllViewsInLayout()
        newMeetingItemsLL.removeAllViews()

        for (i in items) {
            newMeetingItemsLL.addView(AddMeetingItemView(this, this, items, i).view)
        }

    }

    private fun checkInput(): Boolean {
        if (newMeetingNumET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Meeting Number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newMeetingTitleET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Meeting Title", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newMeetingDET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Meeting Date", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newMeetingSTET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Meeting Start Time", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newMeetingETET.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Meeting End Time", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_meeting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.addMeetingMenu -> {
                if (!checkInput()) {
                    return false
                }
                val newMeetingObject = JSONObject()
                newMeetingObject.put("title", newMeetingTitleET.text.toString().trim())
                newMeetingObject.put("startDate", newMeetingDET.text.toString().trim() + " " + newMeetingSTET.text.toString().trim())
                newMeetingObject.put("endDate", newMeetingTitleET.text.toString().trim() + " " + newMeetingETET.text.toString().trim())
                newMeetingObject.put("num", newMeetingTitleET.text.toString().trim().toInt())

                val itemsJSON = JSONArray()
                for (i in items) {
                    val itemObject = JSONObject()
                    itemObject.put("num", i.num)
                    itemObject.put("subject", i.subject)
                    itemObject.put("extra", i.extra)
                    itemsJSON.put(itemObject)
                }

                newMeetingObject.put("items", itemsJSON)

                sendRequest(newMeetingObject)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendRequest(newMeetingObject: JSONObject) {
        newMeetingFL.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.POST, StaticVars.NEW_MEETING, newMeetingObject, {
            queue.cancelAll("new")
            Toast.makeText(this, "Meeting has been Added Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }, {
            queue.cancelAll("new")
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
            newMeetingFL.visibility = View.GONE
        })

        request.tag = "new"
        request.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)

    }

}
