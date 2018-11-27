package com.crazy_iter.ishada

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.Adapters.MeetingsAdapter
import com.crazy_iter.ishada.Dialogs.ChooseYearMonthDialog
import com.crazy_iter.ishada.Models.ItemModel
import com.crazy_iter.ishada.Models.MeetingModel
import kotlinx.android.synthetic.main.activity_meetings.*
import java.util.*
import kotlin.collections.ArrayList

class MeetingsActivity : AppCompatActivity() {

    private val years = ArrayList<Int>()
    private val updateMeetings = ArrayList<MeetingModel>()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meetings)

        setSupportActionBar(meetingsTB)
        val perfs = getSharedPreferences("Ishada", Context.MODE_PRIVATE)
        val role = perfs.getString("role", "employee")!!
        meetingsTB.subtitle = perfs.getString("name", "")!! + " / " + role

        if (!StaticMethods.isUserAdmin(this)) {
            newMeetingFAB.visibility = View.GONE
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.meetingsFL, LoadingFragment())
        ft.commit()

        getMeetings()

        meetingsSRL.isRefreshing = false
        meetingsSRL.setOnRefreshListener {
            getMeetings()
        }

        newMeetingFAB.setOnClickListener {
            startActivity(Intent(this, NewMeetingActivity::class.java))
        }

        if (StaticMethods.isUserAdmin(this)) {
            newMeetingFAB.visibility = View.VISIBLE
        } else {
            newMeetingFAB.visibility = View.GONE
        }

    }

    private fun getMeetings() {
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(StaticVars.MEETINGS, {
            queue.cancelAll("meetings")
            StaticMethods.meetings = ArrayList()
            years.clear()
            for (i in 0 until it.length()) {
                val meetingModel = it.getJSONObject(i)

                val date = meetingModel.getString("startDate").split(" ")[0].split("-")[2].toInt()
                if (!years.contains(date)) {
                    years.add(date)
                }

                val meetingObject = MeetingModel(
                        meetingModel.getString("_id"),
                        meetingModel.getString("title"),
                        meetingModel.getString("startDate"),
                        meetingModel.getString("endDate"),
                        meetingModel.getInt("num"),
                        ArrayList(),
                        ArrayList()
                )

                for (u in 0 until meetingModel.getJSONArray("users").length()) {
                    meetingObject.addUser(meetingModel.getJSONArray("users").getString(u))
                }

                for (t in 0 until meetingModel.getJSONArray("items").length()) {
                    val itemJSON = meetingModel.getJSONArray("items").getJSONObject(t)
                    val item = ItemModel(itemJSON.getInt("num"),
                            itemJSON.getString("subject"),
                            itemJSON.getString("extra"),
                            null)
                    meetingObject.addItem(item)
                }

                StaticMethods.meetings.add(meetingObject)
            }

            setMeetings()

        }, {
            Log.e("meetings", it.toString())
            queue.cancelAll("meetings")
            getMeetings()
        })
        request.tag = "meetings"
        request.retryPolicy = DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(request)
    }

    private fun setMeetings(isUpdate: Boolean = false) {
        meetingsSRL.isRefreshing = false
        meetingsFL.visibility = View.GONE
        meetingsRV.setHasFixedSize(true)
        meetingsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        if (isUpdate) {
            meetingsRV.adapter = MeetingsAdapter(this, updateMeetings)
        } else {
            meetingsRV.adapter = MeetingsAdapter(this, StaticMethods.meetings)
        }
        meetingsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    StaticMethods.hideRightAnim(this@MeetingsActivity, meetingFABsLL)
                } else if (dy < 0) {
                    StaticMethods.showRightAnim(this@MeetingsActivity, meetingFABsLL)
                }
            }
        })

    }

    private fun filterMeetings(year: Int, month: Int) {
        updateMeetings.clear()
        for (m in StaticMethods.meetings) {
            val currentYear = m.startDate.split(" ")[0].split("-")[2].toInt()
            val currentMonth = m.startDate.split(" ")[0].split("-")[1].toInt()
            if (currentMonth == month && currentYear == year) {
                updateMeetings.add(m)
            }
        }

        setMeetings(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meeting_menu, menu)

        if (!StaticMethods.isUserAdmin(this)) {
            menu?.findItem(R.id.meetingResister)?.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.meetingDate -> {
                if (meetingsFL.visibility == View.VISIBLE) {
                    return false
                }
                val cal = Calendar.getInstance()
                val datePicker = ChooseYearMonthDialog(this, years, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
                datePicker.setOnDismissListener {
                    if (datePicker.isDone) {
                        filterMeetings(datePicker.selectedYearOut, datePicker.selectedMonthOut)
                    }
                }
                datePicker.show()
            }

            R.id.meetingResister -> startActivity(Intent(this, SignUpActivity::class.java))

            R.id.meetingLogout -> if (StaticMethods.logout(this)) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                Toast.makeText(this, "Signed out Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
