package com.crazy_iter.ishada

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.crazy_iter.ishada.Models.TaskModel
import kotlinx.android.synthetic.main.activity_task_preview.*
import org.json.JSONArray
import org.json.JSONObject

class TaskPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_preview)

        setSupportActionBar(taskTB)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.previewFL, LoadingFragment())
        ft.commit()
        previewFL.visibility = View.VISIBLE

        val task = intent.getSerializableExtra("task") as TaskModel
        previewTitleTV.text = task.title
        previewDateTV.text = task.data
        previewAssignTV.text = ""
        previewDescriptionTV.text = task.description
        when (task.status.toLowerCase()) {
            "new" -> previewStatusIV.setImageResource(R.drawable.ic_start_arrow)
            "todo" -> previewStatusIV.setImageResource(R.drawable.ic_hourglass)
            "done" -> previewStatusIV.setImageResource(R.drawable.ic_done)
            "finish" -> previewStatusIV.setImageResource(R.drawable.ic_done_all)
        }

        val users = JSONArray()
        for (u in task.taskUser) {
            users.put(u)
        }
        val usersObject = JSONObject()
        usersObject.put("users", users)

        getUsers(usersObject)

    }

    private fun getUsers(usersObject: JSONObject) {
        val queue = Volley.newRequestQueue(this)
        val usersRequest = JsonObjectRequest(Request.Method.POST, StaticVars.USERS_BY_IDs, usersObject, {
            queue.cancelAll("users")
            previewFL.visibility = View.GONE

            var users = ""
            for (i in 0 until it.getJSONArray("users").length()) {
                if (i == 0) {
                    users = it.getJSONArray("users").getJSONObject(i).getString("username")
                } else {
                    users += ", " + it.getJSONArray("users").getJSONObject(i).getString("username")
                }
            }
            previewAssignTV.text = users

        }, {
            queue.cancelAll("users")
            Log.e("users", it.toString())
            getUsers(usersObject)
        })
        usersRequest.tag = "users"
        queue.add(usersRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
