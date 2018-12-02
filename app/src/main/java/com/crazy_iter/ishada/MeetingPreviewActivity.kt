package com.crazy_iter.ishada

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.Adapters.MeetingItemView
import com.crazy_iter.ishada.Models.ItemModel
import com.crazy_iter.ishada.Models.MeetingModel
import kotlinx.android.synthetic.main.activity_meeting_preview.*

class MeetingPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_preview)

        setSupportActionBar(meetingPreviewTB)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.meetingFL, LoadingFragment())
        ft.commit()

        getMeeting(intent.getStringExtra("id"))

    }

    private fun getMeeting(id: String) {

        meetingFL.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(StaticVars.MEETINGS + id, null, {
            queue.cancelAll("meetings")
            meetingFL.visibility = View.GONE

            val meetingObject = MeetingModel(
                    it.getString("_id"),
                    it.getString("title"),
                    it.getString("startDate"),
                    it.getString("endDate"),
                    it.getInt("num"),
                    ArrayList(),
                    ArrayList()
            )

            for (u in 0 until it.getJSONArray("users").length()) {
                meetingObject.addUser(it.getJSONArray("users").getString(u))
            }

            for (t in 0 until it.getJSONArray("items").length()) {
                val itemJSON = it.getJSONArray("items").getJSONObject(t)
                val item = ItemModel(itemJSON.getInt("num"),
                        itemJSON.getString("subject"),
                        itemJSON.getString("extra"),
                        null)
                meetingObject.addItem(item)
            }

            setMeeting(meetingObject)

        }, {
            Log.e("meetings", it.toString())
            queue.cancelAll("meetings")
            getMeeting(id)
        })
        request.tag = "meetings"
        request.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)
    }

    private fun setMeeting(meetingObject: MeetingModel) {
        meetingTitleTV.text = meetingObject.title
        meetingNumTV.text = meetingObject.num.toString()
        meetingDTV.text = meetingObject.startDate.split(" ")[0]
        meetingSTTV.text = meetingObject.startDate.split(" ")[1]
        meetingETTV.text = meetingObject.endDate.split(" ")[1]

        for (item in meetingObject.items) {
            meetingItemsLL.addView(MeetingItemView(this, item).view)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
